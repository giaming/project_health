package org.example.service;

import org.example.pojo.Member;

import java.util.List;
import java.util.Map;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2022/1/4 17:11
 */
public interface MemberService {
    void add(Member member);

    Member findByTelephone(String telephone);

    /**
     * 会员数量统计
     * @param month
     * @return
     */
    List<Integer> findMemberCountByMonth(List<String> month);
}
