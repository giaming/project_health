package org.example.service;

import org.example.entity.PageResult;
import org.example.entity.QueryPageBean;
import org.example.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    void add(CheckGroup checkGroup, Integer[] checkitemIds);
    PageResult findPage(QueryPageBean pageBean);
    CheckGroup findById(Integer id);
    void edit(CheckGroup checkGroup, Integer[] checkitemIds);
    List<CheckGroup> findAll();

    void deleteById(Integer id);
}
