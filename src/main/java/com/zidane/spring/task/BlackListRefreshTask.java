package com.zidane.spring.task;

import com.zidane.spring.dao.UserDao;
import com.zidane.spring.domain.User;
import com.zidane.spring.util.cons.CommonConstant;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;

/**
 * quartz框架下的黑名单定时刷新任务
 * 每分钟定时执行一次刷新任务，更新黑名单信息至redis，对应存储在redis中的key值为“blackList”
 *
 * @author Zidane
 * @since 2018-08-12
 */
public class BlackListRefreshTask {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ListOperations<String, Object> listOperations;

    /**
     * 定时任务执行方法，更新redis中的黑名单列表
     */
    public void process() {
        // 删除key为BLACKLIST_REDIS_KEY的集合中的最左端的第一个元素，并返回元素的值
        listOperations.leftPop(CommonConstant.BLACKLIST_REDIS_KEY);
        // 查询数据库，获取所有被锁定的用户，即被加入黑名单的用户
        List<User> userList = userDao.queryAllLockedUser();
        List<String> blackListUserName = userList.stream().map(c -> c.getUserName()).collect(Collectors.toList());

        // 向集合中添加一个或多个元素，元素以集合的方式存在，从左到右
        // 这里加入的是一个包含多个用户名称的黑名单list
        listOperations.leftPushAll(CommonConstant.BLACKLIST_REDIS_KEY, blackListUserName);
    }
}