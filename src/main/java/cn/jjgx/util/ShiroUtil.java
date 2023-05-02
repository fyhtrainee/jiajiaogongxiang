package cn.jjgx.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

public class ShiroUtil {

    public Long getId(){
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        return (Long)session.getAttribute("id");
    }

}
