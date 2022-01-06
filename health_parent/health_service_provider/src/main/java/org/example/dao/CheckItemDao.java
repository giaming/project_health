package org.example.dao;

import com.github.pagehelper.Page;
import org.example.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    void add(CheckItem checkItem);
    Page<CheckItem> findByCondition(String queryString);
    long selectCountByCheckItemId(Integer checkItemId);
    void deleteById(Integer id);
    CheckItem findById(Integer id);
    void edit(CheckItem checkItem);
    List<CheckItem> findAll();
    List<Integer> findCheckItemIdsByCheckGroupId(Integer checkgroupId);

    /**
     * 根据用户套餐的id得到checkGroup的id，然后根据checkgroup的id得到checkitem的基本信息
     * @param id
     * @return
     */
    CheckItem findCheckItemByCheckGroupId(Integer id);
}