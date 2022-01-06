package org.example.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.example.dao.OrderSettingDao;
import org.example.pojo.OrderSetting;
import org.example.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2021/12/18 19:03
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    OrderSettingDao orderSettingDao;

    /**
     * 数据批量导入到数据库
     * @param list
     */
    @Override
    public void addFileData(List<OrderSetting> list) {
        if(list!=null && list.size()>0){
            for (OrderSetting orderSetting :
                    list) {
                // 判断当前日期是否已经进入了预约设置
                long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
//                System.out.println("countByOrderDate:"+countByOrderDate);
//                System.out.println("OrderDate:"+orderSetting.getOrderDate());
                if(countByOrderDate > 0){
                    // 已经进行了预约设置，执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    // 没有进行预约设置，执行插入操作
                    orderSettingDao.addFileData(orderSetting);
                }
            }
        }
    }

    /**
     * 根据月份查询对应的预约设置数据
     *
     * @param date
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String begin = date + "-1";  // 2021-12-1
        String end = date + "-31";  // 2021-12-31
        Map<String, String> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        // 调用Dao，根据日期范围查询预约设置数据
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> result = new ArrayList<>();
        if(list != null && list.size()>0){
            for (OrderSetting orderSetting :
                    list) {
                Map<String, Object> m = new HashMap<>();
                m.put("date", orderSetting.getOrderDate().getDate());  // 获取日期数字
                m.put("number", orderSetting.getNumber());
                m.put("reservations", orderSetting.getReservations());
                result.add(m);
            }
        }
        return result;
    }

    /**
     * 根据日期设置对应的预约设置数据
     *
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        Date orderDate = orderSetting.getOrderDate();
        // 根据日期查询是否已经进入了预约设置
        long count = orderSettingDao.findCountByOrderDate(orderDate);
        System.out.println(count);
        System.out.println();
        if(count>0){
            // 当前日期已经进行了预约设置，需要执行更新操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            orderSettingDao.addFileData(orderSetting);
        }

    }
}
