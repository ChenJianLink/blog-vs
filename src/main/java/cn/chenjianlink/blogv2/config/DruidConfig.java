package cn.chenjianlink.blogv2.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * druid数据库连接池配置类
 *
 * @author chenjian
 */
@Configuration
public class DruidConfig {

    @Value("${spring.datasource.druid.loginUsername}")
    private String userName;
    @Value("${spring.datasource.druid.loginPassword}")
    private String password;

    //配置Druid的监控

    /**
     * 配置一个管理后台的Servlet
     *
     * @return ServletRegistrationBean
     */
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String, String> initParams = new HashMap<>(5);
        initParams.put("loginUsername", userName);
        initParams.put("loginPassword", password);
        //默认就是允许所有访问
        initParams.put("allow", "");
        bean.setInitParameters(initParams);
        return bean;
    }


    /**
     * 配置一个web监控的filter
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean webStatFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());
        Map<String, String> initParams = new HashMap<>(2);
        initParams.put("exclusions", "*.js,*gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        bean.setInitParameters(initParams);
        bean.setUrlPatterns(Arrays.asList("/*"));
        return bean;
    }
}
