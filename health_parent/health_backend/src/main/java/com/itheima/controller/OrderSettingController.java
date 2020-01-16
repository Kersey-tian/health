package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
        try {
            //读取excel
            List<String[]> excelList = POIUtils.readExcel(excelFile);
            if(excelList != null && excelList.size() > 0){
                List<OrderSetting> orderSettingList = new ArrayList<>();
                for (String[] strings : excelList) {
                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]),Integer.parseInt(strings[1]));
                    orderSettingList.add(orderSetting);
                }
                orderSettingService.add(orderSettingList);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){//格式：2019-6
            String startday = date + "-1";
            String endday = date + "-31";

        try {
            List<Map> orderSettingByMonthList = orderSettingService.getOrderSettingByMonth(startday,endday);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,orderSettingByMonthList);
        } catch (Exception e) {
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    @RequestMapping("/changeNumberByDate")
    public Result changeNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.changeNumberByDate(orderSetting);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
    }
}
