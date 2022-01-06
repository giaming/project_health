package org.example.service;

import org.example.entity.Result;

import java.util.Map;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2022/1/4 9:01
 */
public interface OrderService {
    Result order(Map map) throws Exception;

    Map findById(Integer id) throws Exception;
}
