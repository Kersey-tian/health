package com.itheima.dao;


import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    void add(CheckItem checkItem);

    Page<CheckItem> selectByCondition(String queryString);

    void delete(Integer id);

    Long findCountByCheckItemId(Integer id);

    List<CheckItem> findAll();

    void update(CheckItem checkItem);

    CheckItem findById(Integer id);
}
