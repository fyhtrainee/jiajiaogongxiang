package cn.jjgx.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletContext;
import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Configuration
public class MailUtil {


    @Autowired
    ServletContext servletContext;

    public static Session createSession(){

        String stmp = "smtp.qq.com";

        String username = "664958302@qq.com";
        String password = "avfvhazoqgnqbddj";

        Properties properties = new Properties();
        properties.put("mail.smtp.host" , stmp);
        properties.put("mail.smtp.port" , 25);
        properties.put("mail.smtp.auth" , "true");
        properties.put("mail.smtp.starttls.enable","true");



        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        session.setDebug(true);

        return session;
    }

    /**
     *
     * 将验证码发送到指定邮箱
     * @param targetEmail 目标邮箱地址
     */
    public static void sendCaptchaToEmail(String content , String targetEmail , String captcha){

        try {
            //创建Session对象该Session对象定义了smtp发送账号和授权码和规则
            Session session = createSession();

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("664958302@qq.com"));
            msg.setRecipient(Message.RecipientType.TO , new InternetAddress(targetEmail));
            msg.setSubject(content,"utf-8");

            MimeBodyPart captchaPart = new MimeBodyPart();
            captchaPart.setContent(captcha , "text/plain;charset=utf-8");

            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(captchaPart);

            msg.setContent(mimeMultipart);

            Transport.send(msg);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    public void ScheduledCaptcha(String email , String captcha){
        ThreadFactory threadFactory = Thread::new;

        servletContext.setAttribute(email , captcha);

        //自动关闭
        try {

            ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(10, threadFactory);
            servletContext.setAttribute(email , captcha);

            stpe.schedule(() -> {
                servletContext.removeAttribute(email);
            } , 10 , TimeUnit.MINUTES);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
