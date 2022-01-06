package org.example.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.example.constant.MessageConstant;
import org.example.constant.RedisConstant;
import org.example.constant.RedisMessageConstant;
import org.example.entity.Result;
import org.example.pojo.Order;
import org.example.service.OrderService;
import org.example.utils.TengXunSMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.sql.ResultSet;
import java.util.Map;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2022/1/4 8:59
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 体检预约
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map) {
        String telephone = (String) map.get("telephone");
        // 从redis中获取缓存的验证码，key为手机号+RedisMessageConstant.SENDTYPE_ORDER
        String codeinRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) map.get("validateCode");
        // 校验手机验证码
        if(codeinRedis==null || !codeinRedis.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result = null;
        // 调用体检预约服务
        try{
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.order(map);
        }catch (Exception e){
            e.printStackTrace();
            // 预约失败
            result.setFlag(false);
        }
        if (result.isFlag()) {
            // 预约成功，发送短信通知
            String orderDate = (String) map.get("orderDate");
            try {
                TengXunSMSUtils.sendShortMessage(TengXunSMSUtils.VALIDATE_CODE, telephone, orderDate);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map map = orderService.findById(id);
            // 查询预约信息成功
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        }catch (Exception e){
            e.printStackTrace();
            // 查询预约信息失败
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
