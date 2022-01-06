package org.example.controller;

import org.example.constant.MessageConstant;
import org.example.constant.RedisMessageConstant;
import org.example.entity.Result;
import org.example.utils.TengXunSMSUtils;
import org.example.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.sql.ResultSet;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2022/1/3 22:53
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    // 体检预约时发送手机验证码
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        // 生成六位数字验证码
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        try {
            // 发送短信
            TengXunSMSUtils.sendShortMessage(TengXunSMSUtils.VALIDATE_CODE, telephone, code.toString());
        }catch (Exception e){
            e.printStackTrace();
            // 验证码发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的手机验证码为:"+code);
        // 将生成的验证码缓存到redis
        jedisPool.getResource().setex(
                telephone+ RedisMessageConstant.SENDTYPE_ORDER, 5*60, code.toString());
        // 验证码发送成功
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    /**
     * 手机快速登录时发送手机验证码
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        Integer code = ValidateCodeUtils.generateValidateCode(6);  // 生成6位数字验证码
        try{
            // 发送短信
            TengXunSMSUtils.sendShortMessage(TengXunSMSUtils.VALIDATE_CODE, telephone, code.toString());
        }catch (Exception e) {
            e.printStackTrace();
            // 验证码发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送手机号验证码为："+code);
        // 将生成的验证码缓存到redis
        jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,
                    5*60, code.toString());
        // 验证码发送成功
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

}
