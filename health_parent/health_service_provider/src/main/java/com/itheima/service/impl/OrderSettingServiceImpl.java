package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class,protocol = "dubbo")
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> orderSettingList) {
        if(orderSettingList != null && orderSettingList.size() > 0){
            for (OrderSetting orderSetting : orderSettingList) {
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if(count > 0){
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    @Override
    public List<Map> getOrderSettingByMonth(String startday, String endday) {
        Map map = new HashMap<>();
        map.put("startday",startday);
        map.put("endday",endday);
        List<OrderSetting> orderSettings = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> data = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettings) {
            Map ordermap = new HashMap();
            ordermap.put("date",orderSetting.getOrderDate().getDate());
            ordermap.put("number",orderSetting.getNumber());
            ordermap.put("reservations",orderSetting.getReservations());
            data.add(ordermap);
        }
        return data;
    }

    @Override
    public void changeNumberByDate(OrderSetting orderSetting) {
        orderSettingDao.changeNumberByDate(orderSetting);
    }
}
