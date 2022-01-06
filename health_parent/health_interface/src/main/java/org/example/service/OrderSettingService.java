package org.example.service;

import org.example.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2021/12/18 19:02
 */
public interface OrderSettingService {

    /**
     * 数据批量导入到数据库
     * @param list
     */
    void addFileData(List<OrderSetting> list);

    /**
     * 根据月份查询对应的预约设置数据
     * @param date
     * @return
     */
    List<Map> getOrderSettingByMonth(String date);

    /**
     * 根据日期设置对应的预约设置数据
     * @param orderSetting
     */
    void editNumberByDate(OrderSetting orderSetting);
}
