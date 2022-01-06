package org.example.service;

import org.example.entity.PageResult;
import org.example.entity.QueryPageBean;
import org.example.pojo.CheckGroup;
import org.example.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2021/12/16 20:49
 */
/*由于项目采用的是分布式的框架，所以不管是后台管理界面还是移动端界面获取的服务是在同一个接口里面的方法*/
public interface SetmealService {
    /**
     * 从套餐中查出信息然后进行分页展示
     * @param queryPageBean
     * @return
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 新增套餐
     * @param setmeal
     * @param checkgroupIds
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 使用套餐id，查询检查组的id集合，返回list
     * @param id
     * @return
     */
    List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    /**
     * 编辑套餐
     * @param setmeal
     * @param checkgroupIds
     */
    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 根据套餐id查询
     * @param id
     * @return
     */
    Setmeal findById(Integer id);

    /**
     * 根据id删除套餐
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 获取所有套餐信息
     * @return
     */
    List<Setmeal> findAll();

    /**
     * 统计套餐数量
     * @return
     */
    List<Map<String, Object>> findSetmealCount();
}
