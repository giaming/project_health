package org.example.dao;

import org.example.pojo.Role;

import java.util.Set;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2022/1/5 15:35
 */
public interface RoleDao {
    /**
     * 通过用户id查询
     * @param userId
     * @return
     */
    Set<Role> findByUserId(Integer userId);
}
