package org.example.controller;

import org.example.constant.MessageConstant;
import org.example.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2022/1/5 20:59
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getUserName")
    public Result getUserName(){
        try{
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, user.getUsername());
        }catch (Exception e){
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }
}
