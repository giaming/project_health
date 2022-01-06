package org.example.dao;

import org.example.pojo.Permission;

import java.util.Set;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2022/1/5 15:35
 */
public interface PermissionDao {
    /**
     *
     * @param roleId
     * @return
     */
    Set<Permission> findByRoleId(Integer roleId);
}
