package com.zidane.spring.task;

import com.zidane.spring.dao.UserDao;
import com.zidane.spring.domain.User;
import com.zidane.spring.util.cons.CommonConstant;
import com.zidane.spring.util.zk.Lock;
import com.zidane.spring.util.zk.ZkLockUtil;

import groovy.util.logging.Slf4j;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;

/**
 * quartz框架下的黑名单定时刷新任务
 * 每分钟定时执行一次刷新任务，更新黑名单信息至redis，对应存储在redis中的key值为“blackList”
 *
 * @author Zidane
 * @since 2019-02-12
 */
@Slf4j
public class BlackListRefreshTask {

    private static final Logger logger = LoggerFactory.getLogger(BlackListRefreshTask.class);

    /**
     * 当前线程的分布式锁标识; true表示: 上一次的分布式任务，是在本节点执行的
     */
    private static Lock locked = new Lock(false);

    @Autowired
    private UserDao userDao;

    @Autowired
    private ListOperations<String, Object> listOperations;

    /**
     * 定时任务执行方法，更新redis中的黑名单列表
     */
    public void process() {
        logger.debug("Refresh black list info begin.");

        if (ZkLockUtil.getOneZkLock(locked)) {
            // 删除key为BLACKLIST_REDIS_KEY的集合中的最左端的第一个元素，并返回元素的值
            listOperations.leftPop(CommonConstant.BLACKLIST_REDIS_KEY);
            // 查询数据库，获取所有被锁定的用户，即被加入黑名单的用户
            List<User> userList = userDao.queryAllLockedUser();
            List<String> blackListUserName = userList.stream().map(c -> c.getUserName()).collect(Collectors.toList());

            // 向集合中添加一个或多个元素，元素以集合的方式存在，从左到右
            // 这里加入的是一个包含多个用户名称的黑名单list
            listOperations.leftPushAll(CommonConstant.BLACKLIST_REDIS_KEY, blackListUserName);
        }

        logger.debug("Refresh black list info end.");
    }
}