package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void add(List<OrderSetting> orderSettingList);

    List<Map> getOrderSettingByMonth(String startday, String endday);

    void changeNumberByDate(OrderSetting orderSetting);
}
