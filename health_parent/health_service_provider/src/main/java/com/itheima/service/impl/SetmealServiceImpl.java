package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class,protocol = "dubbo")
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Value("${out_put_path}")
    private String outputpath;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        if(checkgroupIds != null && checkgroupIds.length > 0){
            this.setSetmealAndCheckGroup(setmeal,checkgroupIds);
        }
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        this.generateMobileStaticHtml();
    }

    public void setSetmealAndCheckGroup(Setmeal setmeal,Integer[] checkgroupIds){
        for (Integer checkgroupId : checkgroupIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("setmeal_id",setmeal.getId());
            map.put("checkgroup_id",checkgroupId);
            setmealDao.setSetmealAndCheckGroup(map);
        }
    }
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.findPage(queryString);
        long total = page.getTotal();
        List<Setmeal> result = page.getResult();

        return new PageResult(total,result);
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    //生成静态页面
    public void generateMobileStaticHtml(){
        //准备模板文件中所需的数据
        List<Setmeal> setmealList = this.findAll();
        //生成套餐列表静态页面
        generateMobileSetmealListHtml(setmealList);
        //生成套餐详情静态页面(多个)
        generateMobileSetmealDetailHtml(setmealList);
    }

    //生成套餐列表静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> setmealList){
        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("setmealList",setmealList);
        //调用工具类，将模板生成页面
        generateHtml("mobile_setmeal.ftl","m_setmeal.html",dataMap);
    }

    //生成套餐详情静态页面
    public void generateMobileSetmealDetailHtml(List<Setmeal> setmealList){
        for (Setmeal setmeal : setmealList) {
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("setmeal",this.findById(setmeal.getId()));
            //调用工具类，将模板生成页面
            generateHtml("mobile_setmeal_detail.ftl","setmeal_detail_"+setmeal.getId()+".html",dataMap);
        }
    }

    //将模板生成页面工具类
    public void generateHtml(String templateName,String htmlPageName,Map<String,Object> dataMap){
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;

        try {
            //加载模板文件
            Template template = configuration.getTemplate(templateName);
            //生成数据
            File file = new File(outputpath + "\\" + htmlPageName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            template.process(dataMap,out);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }finally {
            try {
                if (null != out){
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
