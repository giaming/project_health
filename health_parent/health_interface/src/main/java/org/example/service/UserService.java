package org.example.service;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2022/1/5 15:25
 */

import org.example.pojo.User;

/**
 * 用户服务接口
 */
public interface UserService {
    User findByUserName(String username);
}
