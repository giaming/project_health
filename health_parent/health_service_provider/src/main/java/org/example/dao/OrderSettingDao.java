package org.example.dao;

import org.example.pojo.OrderSetting;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2021/12/18 19:03
 */
//@Repository
public interface OrderSettingDao {
    /**
     * 判断当前日期是否进行了预约
     * @param orderDate
     * @return
     */
    long findCountByOrderDate(Date orderDate);

    /**
     * 已经进行了预约设置，执行更新操作
     * @param orderSetting
     */
    void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 没有进行预约设置，执行插入操作
     * @param orderSetting
     */
    void addFileData(OrderSetting orderSetting);

    /**
     * 根据月份查询对应的预约设置
     * @param map
     * @return
     */
    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);

    /**
     * 根据预约日期查询预约设置信息
     * @param orderDate
     * @return
     */
    OrderSetting findByOrderDate(Date orderDate);

    /**
     * 更新预约人数
     * @param orderSetting
     */
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
