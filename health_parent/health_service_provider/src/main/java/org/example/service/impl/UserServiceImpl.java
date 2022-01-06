package org.example.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.example.dao.PermissionDao;
import org.example.dao.RoleDao;
import org.example.dao.UserDao;
import org.example.pojo.Permission;
import org.example.pojo.Role;
import org.example.pojo.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2022/1/5 15:33
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;


    @Override
    public User findByUserName(String username) {
        // 角色需要关联角色再关联给用户
        User user = userDao.findByUserName(username);
        if(user == null) {
            return null;
        }
        // 根据用户id查询对应角色
        Integer userId = user.getId();
        Set<Role> roles = roleDao.findByUserId(userId);
        if(roles != null && roles.size() > 0){
            for(Role role : roles){
                Integer roleId = role.getId();
                Set<Permission> permissions = permissionDao.findByRoleId(roleId);
                if(permissions != null && permissions.size() > 0){
                    role.setPermissions(permissions);
                }
            }
            user.setRoles(roles);
        }
        return user;
    }
}
