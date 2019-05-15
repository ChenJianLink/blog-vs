package cn.chenjianlink.blogv2.shiro;

import cn.chenjianlink.blogv2.pojo.Blogger;
import cn.chenjianlink.blogv2.service.BloggerService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;

/**
 * 自定义realm
 *
 * @author chenjian
 */
public class BlogRealm extends AuthorizingRealm {

    @Resource
    private BloggerService bloggerService;

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        return null;
    }

    /**
     * 认证
     *
     * @param token 账号密码
     * @return 认证结果
     * @throws AuthenticationException 认证异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        Blogger blogger = bloggerService.findPassword();
        if (!blogger.getUserName().equals(username)) {
            //没找到帐号
            throw new UnknownAccountException();
        }
        SecurityUtils.getSubject().getSession().setAttribute("currentUser", blogger);
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(blogger.getUserName(), blogger.getPassword(), ByteSource.Util.bytes(blogger.getSalt()), getName());
        return authenticationInfo;
    }

}
