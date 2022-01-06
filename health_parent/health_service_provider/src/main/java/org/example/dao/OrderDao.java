package org.example.dao;

import org.example.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2022/1/4 9:26
 */
public interface OrderDao {
    /**
     * 添加订单
     * @param order
     */
    void add(Order order);

    /**
     * 根据条件查询
     * @param order
     * @return
     */
    List<Order> findByCondition(Order order);

    /**
     * 根据id查询信息
     * @param id
     * @return
     */
    Map findById4Detail(Integer id);

    /**
     * 根据日期统计预约数
     * @param date
     * @return
     */
    Integer findOrderCountByDate(String date);

    /**
     * 统计指定日期之后的到诊数
     * @param date
     * @return
     */
    Integer findOrderCountAfterDate(String date);

    /**
     * 根据日期统计到诊数
     * @param date
     * @return
     */
    Integer findVisitsCountByDate(String date);

    /**
     * 统计指定日期之后的到诊数
     * @param date
     * @return
     */
    Integer findVisitsCountAfterDate(String date);

    /**
     * 统计热门套餐
     * @return
     */
    List<Map> findHotSetmeal();
}
