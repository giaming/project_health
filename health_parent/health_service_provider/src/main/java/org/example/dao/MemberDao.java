package org.example.dao;

import com.github.pagehelper.Page;
import org.example.pojo.Member;

import java.util.List;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2022/1/4 9:26
 */

public interface MemberDao {
    /**
     * 查找全部
     * @return
     */
    List<Member> findAll();

    /**
     * 根据条件查询
     * @param queryString
     * @return
     */
    Page<Member> selectByCondition(String queryString);

    /**
     * 新增会员
     * @param member
     */
    void add(Member member);

    /**
     * 删除会员
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 根据会员id查询
     * @param id
     * @return
     */
    Member findById(Integer id);

    /**
     * 根据电话号码查询会员
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 编辑会员
     * @param member
     */
    void edit(Member member);

    /**
     * 根据日期统计会员数，统计指定日期之前的会员数
     * @param date
     * @return
     */
    Integer findMemberCountBeforeDate(String date);

    /**
     * 根据日期统计会员数
     * @param date
     * @return
     */
    Integer findMemberCountByDate(String date);

    /**
     * 统计指定日期之后的会员数
     * @param date
     * @return
     */
    Integer findMemberCountAfterDate(String date);

    /**
     * 总会员数
     * @return
     */
    Integer findMemberTotalCount();
}
