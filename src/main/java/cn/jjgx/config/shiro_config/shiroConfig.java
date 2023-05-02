package cn.jjgx.config.shiro_config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.LinkedHashMap;


@Configuration
public class shiroConfig {

    @Autowired
    private UserRealm userRealm;

//    @Bean
//    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("getDefaultWebSecurityManager") DefaultWebSecurityManager SecurityManager){
//
//        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
//
//        //设置安全管理器
//        bean.setSecurityManager(SecurityManager);
//
//        //添加shiro的内置过滤器
//
//        /*
//            anon: 无需认证就可以访问
//            authc: 必须认证了才能访问
//            user: 必须拥有 记住我才能访问
//            perms: 拥有对摸个资源的权限才能访问
//            role: 拥有某个角色的访问权限才能访问
//         */
//
//
//
////        HashMap filterMap = new LinkedHashMap();
////
////
////        filterMap.put("/user/*","authc");
////        filterMap.put("/order/*","authc");
////        //filterMap.put("/user/update","perms[user:update]");
////
////        //filterMap.put("/user/*","authc");
////        bean.setFilterChainDefinitionMap(filterMap);
////
////        //设置需要认证时访问的controller
////        bean.setLoginUrl("/toLogin");
////        //设置需要授权时访问的controller
////        bean.setUnauthorizedUrl("/noAuth");
//
//        return bean;
//
//    }


    @Bean
    public DefaultShiroFilterChainDefinition defaultShiroFilterChainDefinition(){

        DefaultShiroFilterChainDefinition defaultShiroFilterChainDefinition = new DefaultShiroFilterChainDefinition();


        defaultShiroFilterChainDefinition.addPathDefinition("/login/*" ,"anon");
//        defaultShiroFilterChainDefinition.addPathDefinition("/login/UserLogin" , "anon");
        defaultShiroFilterChainDefinition.addPathDefinition("/register/*","anon");

        defaultShiroFilterChainDefinition.addPathDefinition("/**" , "authc");


        defaultShiroFilterChainDefinition.addPathDefinition("/**","user");
        return defaultShiroFilterChainDefinition;
    }

    //rememberMeCookie 用来识别用户
    @Bean
    public SimpleCookie rememberMeCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setPath("/");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(30 * 24 * 60 * 60);
        return simpleCookie;
    }

    @Bean
    public CookieRememberMeManager cookieRememberMeManager(@PathVariable("rememberMeCookie")SimpleCookie rememberMeCookie ){

        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie);
        cookieRememberMeManager.setCipherKey("1234567890987654".getBytes());
        return cookieRememberMeManager;
    }

    @Bean
    public FormAuthenticationFilter formAuthenticationFilter(){
        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        //对应前端的checkbox的name = rememberMe
        formAuthenticationFilter.setRememberMeParam("rememberMe");

        return formAuthenticationFilter;
    }

    @Bean
    public DefaultSessionManager getDefaultWebSessionManager(){
        DefaultWebSessionManager defaultSessionManager = new DefaultWebSessionManager();
        defaultSessionManager.setSessionIdUrlRewritingEnabled(false);
        return defaultSessionManager;
    }

    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@PathVariable("cookieRememberMeManager")CookieRememberMeManager cookieRememberMeManager ){

        // 创建defaultWebSecurityManager对象
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //创建加密对象，设置相关属性
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        //采用md5加密
        matcher.setHashAlgorithmName("md5");
        //设置迭代次数
        matcher.setHashIterations(2);

        //将加密对象存储到userRealm对象当中
        userRealm.setCredentialsMatcher(matcher);
        //将userRealm存储到defaultWebSecurityManager当中
        defaultWebSecurityManager.setRealm(userRealm);


        //setRememberMeManager
        defaultWebSecurityManager.setRememberMeManager(cookieRememberMeManager);

        defaultWebSecurityManager.setSessionManager(getDefaultWebSessionManager());

        return defaultWebSecurityManager;

    };



}
