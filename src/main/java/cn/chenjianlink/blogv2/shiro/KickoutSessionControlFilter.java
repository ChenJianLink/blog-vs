package cn.chenjianlink.blogv2.shiro;

import lombok.Setter;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 登录数量限制拦截器
 *
 * @author chenjian
 */
public class KickoutSessionControlFilter extends AccessControlFilter {

    /**
     * 踢出后到的地址
     */
    @Setter
    private String kickoutUrl;
    /**
     * 踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
     */
    @Setter
    private boolean kickoutAfter = false;
    /**
     * 同一个帐号最大会话数 默认1
     */
    @Setter
    private int maxSession = 1;

    @Setter
    private SessionManager sessionManager;

    private Cache<String, Deque<Serializable>> cache;

    private static final String KICKOUT = "kickout";

    private final transient ReentrantLock reentrantLock = new ReentrantLock();

    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro-activeSessionCache");
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，直接进行之后的流程
            return true;
        }
        ReentrantLock lock = this.reentrantLock;
        lock.lock();
        try {
            Session session = subject.getSession();
            String username = (String) subject.getPrincipal();
            Serializable sessionId = session.getId();

            //同步控制
            Deque<Serializable> deque = cache.get(username);
            if (deque == null) {
                deque = new LinkedList<Serializable>();
                cache.put(username, deque);
            }

            //如果队列里没有此sessionId，且用户没有被踢出；放入队列
            if (!deque.contains(sessionId) && session.getAttribute(KICKOUT) == null) {
                deque.push(sessionId);
            }
            //如果队列里的sessionId数超出最大会话数，开始踢人
            while (deque.size() > maxSession) {
                Serializable kickoutSessionId = null;
                //如果踢出后者
                if (kickoutAfter) {
                    kickoutSessionId = deque.removeFirst();
                } else { //否则踢出前者
                    kickoutSessionId = deque.removeLast();
                }
                try {
                    Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                    if (kickoutSession != null) {
                        //设置会话的kickout属性表示踢出了
                        kickoutSession.setAttribute(KICKOUT, true);
                    }
                } catch (Exception e) {
                    //ignore exception
                }
            }
            //如果被踢出了，直接退出，重定向到踢出后的地址
            if (session.getAttribute(KICKOUT) != null) {
                //会话被踢出了
                try {
                    subject.logout();
                } catch (Exception e) {
                    //ignore exception
                }
                saveRequest(servletRequest);
                WebUtils.issueRedirect(servletRequest, servletResponse, kickoutUrl);
                return false;
            }
            return true;
        } finally {
            lock.unlock();
        }
    }
}
