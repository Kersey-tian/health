package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class, protocol = "dubbo")
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 添加检查组
     *
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        this.setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }

    /**
     * 设置检查组和检查项的关系
     * 抽取出来作为添加和编辑调用
     *
     * @param checkGroupId
     * @param checkitemIds
     */
    @Override
    public void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        if (checkitemIds.length > 0 && checkitemIds != null) {
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroupid", checkGroupId);
                map.put("checkitemid", checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckGroup> page = checkGroupDao.findPage(queryPageBean.getQueryString());
        PageResult result = new PageResult(page.getTotal(),page.getResult());
        return result;
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.update(checkGroup);
        Integer id = checkGroup.getId();
        checkGroupDao.deleteForItemAndGroup(id);
        for (Integer checkitemId : checkitemIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("checkgroup_id",id);
            map.put("checkitem_id",checkitemId);
            checkGroupDao.setCheckGroupAndCheckItem(map);
        }
    }

    @Override
    public void delete(Integer id) {
        checkGroupDao.deleteForItemAndGroup(id);
        checkGroupDao.deleteForGroup(id);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
