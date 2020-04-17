package com.zidane.spring.service;

import com.zidane.spring.dao.UserDao;
import com.zidane.spring.domain.User;
import com.zidane.spring.util.cons.CommonConstant;
import com.zidane.spring.util.exception.UserExistException;

import groovy.util.logging.Slf4j;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;

/**
 * 用户管理服务，负责查询用户、注册用户、锁定用户、查看黑名单等操作
 *
 * @author Zidane
 * @since 2018-08-12
 */
@Slf4j
@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private ListOperations<String, Object> listOperations;

    /**
     * 注册一个新用户,如果用户名已经存在此抛出UserExistException的异常
     *
     * @param user 用户信息
     */
    public void register(User user) throws UserExistException {
        logger.debug("Begin to register user, the user info is: {}", user);

        User u = this.getUserByUserName(user.getUserName());
        if (u != null) {
            logger.error("The user is already exist.");
            throw new UserExistException("The user is already exist.");
        } else {
            user.setCredit(100);
            user.setUserType(0);
            user.setLastVisit(new Date());
            userDao.create(user);
        }
    }

    /**
     * 更新用户信息
     *
     * @param user
     */
    public void updateUserInfo(User user) {
        userDao.update(user);
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param userName  用户名
     * @return User     用户信息
     */
    public User getUserByUserName(String userName) {
        return userDao.getUserByName(userName);
    }

    /**
     * 将用户锁定，锁定的用户不能够登录
     *
     * @param userName 被锁定用户的用户名
     * @param locked   锁定或解锁的标识位：0-解锁；1-锁定
     */
    public void lockUser(String userName, String locked) throws UserExistException {
        logger.debug("Begin to lock user, the user name is: {}", userName);

        User user = userDao.getUserByName(userName);
        if (user == null) {
            logger.error("The user is not exist.");
            throw new UserExistException("The user is not exist.");
        }

        user.setLocked(Integer.valueOf(locked));
        userDao.update(user);

        logger.debug("The user is successfully locked.");
    }

    /**
     * 获取所有普通用户信息
     *
     * @return List<User> 所有普通用户信息
     */
    public List<User> getAllUsers() {
        return userDao.queryAllNormalUser();
    }

    /**
     * 获取系统当前的黑名单然后判断用户是否在名单中
     *
     * @return boolean 账号是否在黑名单中(true:在黑名单中; false:不在黑名单)
     */
    public boolean belongBlackList(String userName) {
        logger.debug("Begin to determine if the user belongs to blackList, the user name is: {}", userName);

        // 获取集合中指定范围的元素(key:集合key; start:起始位置; end:结束位置)
        // 这里获取的是redis中，key为BLACKLIST_REDIS_KEY的所有元素的集合
        List<Object> blackList = listOperations.range(CommonConstant.BLACKLIST_REDIS_KEY, 0, 0);
        // 本实现中，key(BLACKLIST_REDIS_KEY)所对应的元素只有一个，所以直接使用get(0)，从集合中获取第一个元素即可。
        if (blackList != null && blackList.size() > 0 && ((List<String>)blackList.get(0)).contains(userName)) {
            logger.info("The user belongs to blacklist.");
            return true;
        }
        return false;
    }
}
