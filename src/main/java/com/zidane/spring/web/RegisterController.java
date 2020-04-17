package com.zidane.spring.web;

import com.zidane.spring.domain.User;
import com.zidane.spring.service.UserService;
import com.zidane.spring.util.cons.CommonConstant;
import com.zidane.spring.util.exception.UserExistException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户注册管理
 *
 * @author Zidane
 * @since 2018-08-12
 */
@Controller
public class RegisterController extends BaseController {
    @Autowired
    private UserService userService;

    /**
     * 用户注册
     *
     * @return String  返回注册页面路径
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
        return "register";
    }

    /**
     * 用户注册服务
     *
     * @param request 请求信息
     * @param user    用户信息
     * @return ModelAndView  视图模型
     */
    @RequestMapping(value = "/register_user", method = RequestMethod.POST)
    public ModelAndView register_user(HttpServletRequest request, User user) {
        ModelAndView view = new ModelAndView();
        view.setViewName("register_success");
        try {
            userService.register(user);
        } catch (UserExistException e) {
            view.addObject(CommonConstant.ERROR_MSG_KEY, "用户名已经存在，请选择其它的名字。");
            view.setViewName("register");
        }
        setSessionUser(request, user);
        return view;
    }
}
