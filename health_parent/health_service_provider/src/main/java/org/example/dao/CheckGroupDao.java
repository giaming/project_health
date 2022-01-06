package org.example.dao;

import com.github.pagehelper.Page;
import org.example.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    void add(CheckGroup checkGroup);
    void setCheckGroupAndCheckItem(Map<String, Integer> map);
    Page<CheckGroup> findByCondition(String queryString);
    CheckGroup findById(Integer id);
    void edit(CheckGroup checkGroup);
    void deleteAssociation(Integer checkgroupId);
    List<CheckGroup> findAll();

    int findCountByCheckGroupIdO(Integer id);

    int findCountByCheckGroupIdT(Integer id);

    void deleteById(Integer id);

    /**
     * 根据setmeal的id从关联表中查找出所有的checkgroup的id的集合，
     * 然后根据id一个一个去checkgroup中获取检查组的基本信息
     * @param id
     * @return
     */
    CheckGroup findCheckGroupBySetmealId(Integer id);
}
