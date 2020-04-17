package com.zidane.spring.web;

import com.zidane.spring.domain.User;
import com.zidane.spring.util.exception.UserExistException;
import com.zidane.spring.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用户锁定和解锁管理
 *
 * @author Zidane
 * @since 2018-08-12
 */
@Controller
public class LockController {

    @Autowired
    private UserService userService;

    /**
     * 查询普通用户列表
     *
     * @return ModelAndView 视图模型
     */
    @RequestMapping("/lock")
    public ModelAndView userLockList() {
        ModelAndView view = new ModelAndView();

        List<User> users = userService.getAllUsers();
        view.addObject("users", users);
        view.setViewName("lock");

        return view;
    }

    /**
     * 用户锁定及解锁设定
     *
     * @param userName 用户名
     * @param locked   锁定或解锁的标志位
     * @return ModelAndView 视图模型
     */
    @RequestMapping(value = "/userLock", method = RequestMethod.POST)
    public ModelAndView userLock(@RequestParam("userName") String userName,
            @RequestParam("locked") String locked) {
        ModelAndView view = new ModelAndView();

        try {
            userService.lockUser(userName, locked);
            view.setViewName("lock_success");
        } catch (UserExistException e) {
            view.setViewName("/error");
        }
        return view;
    }
}