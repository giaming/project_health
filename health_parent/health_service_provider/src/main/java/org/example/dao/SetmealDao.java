package org.example.dao;

import com.github.pagehelper.Page;
import org.example.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2021/12/16 20:51
 */
public interface SetmealDao {
    /**
     * 从套餐中查出信息然后进行分页展示
     * @param queryString
     * @return
     */
    Page<Setmeal> findByCondition(String queryString);

    /**
     * 增加套餐
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 设置套餐和检查组多对多关系
     * @param map
     */
    void setSetmealAndCheckGroup(Map<String, Integer> map);

    /**
     *
     * @param id
     * @return
     */
    List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    void deleteAssociation(Integer id);

    void edit(Setmeal setmeal);

    void deleteById(Integer id);

    int findSetmealAndCheckGroupCountBySetMealId(Integer id);

    Setmeal findById(Integer id);

    /**
     * 移动端获取所有的套餐信息
     * @return
     */
    List<Setmeal> findAll();

    List<Map<String, Object>> findSetmealCount();
}
