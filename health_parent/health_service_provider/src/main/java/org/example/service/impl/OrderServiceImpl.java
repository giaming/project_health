package org.example.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.example.constant.MessageConstant;
import org.example.dao.MemberDao;
import org.example.dao.OrderDao;
import org.example.dao.OrderSettingDao;
import org.example.entity.Result;
import org.example.pojo.Member;
import org.example.pojo.Order;
import org.example.pojo.OrderSetting;
import org.example.service.OrderService;
import org.example.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2022/1/4 9:18
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    /**
     * 体检预约服务
     * 1. 检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
     * 2. 检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
     * 3. 检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
     * 4. 检查当前用户是否为会员，如果是在直接完成预约，否则会员自动完成注册并进行预约
     * 5. 预约成功，更新当日已预约人数
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public Result order(Map map) throws Exception {
        // 检查当前日期是否进行了预约设置
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        System.out.println("orderSetting:"+orderSetting+"    Date:"+date);
        if (orderSetting==null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        // 检查预约日期是否预约满
        // 可预约人数
        int number = orderSetting.getNumber();
        // 已预约人数
        int reservations = orderSetting.getReservations();
        if (reservations >= number) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        // 检查当前用户是否为会员,根据手机号判断
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        // 防止重复预约
        if(member != null) {
            Integer memberId = member.getId();
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            Order order = new Order(memberId, date, null, null, setmealId);
            List<Order> list = orderDao.findByCondition(order);
            if(list != null && list.size() > 0){
                // 已经完成预约，不能重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }

        // 可以预约，设置预约人数加一
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        if (member==null){
            // 当前用户不是会员，需要添加到会员表
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        // 保存预约信息到预约表
        Order order = new Order(
                member.getId(),
                date,
                (String) map.get("orderType"),
                Order.ORDERSTATUS_NO,
                Integer.parseInt((String) map.get("setmealId")));

        orderDao.add(order);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }


    @Override
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if(map != null) {
            // 处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate", DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
