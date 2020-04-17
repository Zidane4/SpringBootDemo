package com.zidane.spring.dao;

import com.zidane.spring.dao.mapper.UserMapper;
import com.zidane.spring.domain.User;

import groovy.util.logging.Slf4j;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Dao层数据操作类
 *
 * @author Zidane
 * @since 2018-08-12
 */
@Slf4j
@Repository
public class UserDao {
    private final Logger logger = LoggerFactory.getLogger(UserDao.class);

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据用户名称查询用户信息
     *
     * @param userName userName
     * @return User 用户信息
     */
    public User getUserByName(String userName) {
        logger.debug("The user name is: ", userName);
        return userMapper.getUserByName(userName);
    }

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     */
    public void create(User user) {
        logger.debug("Begin to create user, user is {}", user);
        userMapper.insert(user);
        logger.debug("Create user info successed.", user);
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     */
    public void update(User user) {
        logger.debug("Begin to update user info, user info is {}", user);
        userMapper.update(user);
        logger.debug("Update user info successed.", user);
    }

    /**
     * 查询所有用户信息
     *
     * @return List<User> 所有用户信息
     */
    public List<User> queryAllNormalUser() {
        logger.debug("Begin to query all user info.");
        List<User> users = userMapper.queryAllUser();
        logger.debug("Query all user successed, the users' info: {}", users);
        return users;
    }

    /**
     * 查询所有被锁定用户信息
     *
     * @return List<User> 所有被锁定用户信息
     */
    public List<User> queryAllLockedUser() {
        logger.debug("Begin to query all locked user.");
        List<User> users = userMapper.queryAllLockedUser();
        logger.debug("Query all locked user successed, the users' info: {}", users);
        return users;
    }
}
