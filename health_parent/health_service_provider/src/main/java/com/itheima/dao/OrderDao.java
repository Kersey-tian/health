package com.itheima.dao;

import com.itheima.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    List<Order> findByCondition(Order order);
    void add(Order order);

    Map findByIdDetail(Integer id);

    Integer findOrderCountByDate(String today);

    Integer findOrderCountAfterDate(String thisWeekMonday);

    Integer findVisitsCountByDate(String today);

    Integer findVisitsCountAfterDate(String thisWeekMonday);

    List<Map> findHotSetmeal();
}
