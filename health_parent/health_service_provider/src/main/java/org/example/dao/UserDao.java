package org.example.dao;

import org.example.pojo.User;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2022/1/5 15:34
 */
public interface UserDao {
    /**
     * 通过用户名查询
     * @param username
     * @return
     */
    User findByUserName(String username);
}
