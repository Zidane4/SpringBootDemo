package com.zidane.spring.web;

import com.zidane.spring.domain.User;
import com.zidane.spring.service.UserService;
import com.zidane.spring.util.cons.CommonConstant;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 登录信息管理
 *
 * @author Zidane
 * @since 2018-08-12
 */
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {
    /**
     * 用户账户服务
     */
    @Autowired
    private UserService userService;

    /**
     * 用户登陆
     * 先后判断用户是否被锁定(在黑名单中)、是否存在、密码是否正确，并且如果登录用户为admin(超级用户)，则进入对应的账号管理页面
     *
     * @param  request      http请求
     * @return User         用户信息
     * @return ModelAndView 视图模型
     */
    @RequestMapping("/doLogin")
    public ModelAndView login(HttpServletRequest request, User user) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");

        if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())) {
            mav.addObject(CommonConstant.ERROR_MSG_KEY, "请输入用户名和密码");
        } else if (userService.belongBlackList(user.getUserName())) {
            // 这里belongBlackList是从redis缓存中读取黑名单信息，与用户输入的用户名做比对
            mav.addObject(CommonConstant.ERROR_MSG_KEY, "用户已经被锁定，不能登录。");
        } else {
            User dbUser = userService.getUserByUserName(user.getUserName());
            if (dbUser == null) {   // 用户是否存在
                mav.addObject(CommonConstant.ERROR_MSG_KEY, "用户不存在");
            } else if (!dbUser.getPassword().equals(user.getPassword())) {  // 密码是否正确
                mav.addObject(CommonConstant.ERROR_MSG_KEY, "用户密码不正确");
            } else {
                dbUser.setLastIp(request.getRemoteAddr());
                dbUser.setLastVisit(new Date());
                dbUser.setCredit(dbUser.getCredit() + 100);
                userService.updateUserInfo(dbUser);
                setSessionUser(request, dbUser);

                if (CommonConstant.SUPER_USER_NAME.equals(dbUser.getUserName())) {
                    return queryUserList();
                } else {
                    mav.setViewName("login_success");
                }
            }
        }

        return mav;
    }

    /**
     * 登录注销
     *
     * @param   session 上下文
     * @return  String  返回登录路径
     */
    @RequestMapping("/doLogout")
    public String logout(HttpSession session) {
        session.removeAttribute(CommonConstant.USER_CONTEXT);
        return "login";
    }

    /**
     * 查询所有用户信息列表
     *
     * @return ModelAndView 视图模型
     */
    private ModelAndView queryUserList() {
        ModelAndView view = new ModelAndView();

        List<User> users = userService.getAllUsers();
        view.addObject("users", users);
        view.setViewName("lock");  // 对应账号管理页面
        return view;
    }
}
