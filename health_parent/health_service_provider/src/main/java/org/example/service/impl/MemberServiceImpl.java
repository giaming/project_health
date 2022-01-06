package org.example.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.example.dao.MemberDao;
import org.example.pojo.Member;
import org.example.service.MemberService;
import org.example.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2022/1/4 17:15
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberDao memberDao;

    @Override
    public void add(Member member) {
        if (member.getPassword() != null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 根据月份统计会员数量
     * @param month
     * @return
     */
    @Override
    public List<Integer> findMemberCountByMonth(List<String> month) {
        List<Integer> list = new ArrayList<>();
        for (String m : month) {
            try {
                String date_str = m + "-10";
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                calendar.setTime(sdf.parse(date_str));
                calendar.add(Calendar.MONTH, 1);
                calendar.set(Calendar.DAY_OF_MONTH, 0);
                // 获取当月最后一天
                String lastDayOfMonth = sdf.format(calendar.getTime());
                Integer count = memberDao.findMemberCountBeforeDate(lastDayOfMonth);
                list.add(count);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
