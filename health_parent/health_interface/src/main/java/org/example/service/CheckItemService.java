package org.example.service;


import org.example.entity.PageResult;
import org.example.entity.QueryPageBean;
import org.example.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    void add(CheckItem checkItem);
    PageResult findPage(QueryPageBean queryPageBean);
    void delete(Integer id);
    CheckItem findById(Integer id);
    void edit(CheckItem checkItem);
    List<CheckItem> findAll();
    List<Integer> findCheckItemIdsByCheckGroupId(Integer checkgroupId);
}
