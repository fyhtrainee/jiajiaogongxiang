package cn.jjgx.config.shiro_config;

import cn.jjgx.mapper.UserMapperPlus;
import cn.jjgx.pojo.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserMapperPlus userMapperPlus;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {




        /*
        String id = (String)principalCollection.getPrimaryPrincipal();

        Set<String> set ;

        set.add(id);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles();
        authorizationInfo.setStringPermissions(userService.findPermissions(username));
        return authorizationInfo;*/
        return null;

    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {



        QueryWrapper<User> qw = new QueryWrapper<User>();

        String userName = token.getPrincipal().toString();
        qw.select("pwd" , "id" , "locked" , "name").eq("user_id" , userName);
        User user = userMapperPlus.selectOne(qw);

        if(user == null) {
            throw new UnknownAccountException();
        }else if(user.getLocked().equals(1)){
            throw new LockedAccountException();
        }


//        System.out.println(token.getPrincipal() + user.getName());

        return new SimpleAuthenticationInfo(
                token.getPrincipal(),
                user.getPwd(),
                ByteSource.Util.bytes(userName + user.getName()),
                userName);
    }
}
