package org.example.security;

import com.alibaba.dubbo.config.annotation.Reference;
import org.example.pojo.Permission;
import org.example.pojo.Role;
import org.example.pojo.User;
import org.example.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2022/1/5 15:23
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference  // 通过dubbo远程调用服务
    private UserService userService;

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 远程调用用户服务，根据用户名查询用户信息
        User user = userService.findByUserName(username);
        if(user == null){
            // 用户不存在
            return null;
        }
        ArrayList<GrantedAuthority> list = new ArrayList<>();

        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            // 授予角色
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                // 授权
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
//        注意:返回Security的User时,在5.0.0版本以上,建议在密码前添加{bcrypt}
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
////        org.springframework.security.core.userdetails.User userDetails =
//                new org.springframework.security.core.userdetails.User(username, "{bcrypt}"+user.getPassword(), list);
        return userDetails;
    }
}
