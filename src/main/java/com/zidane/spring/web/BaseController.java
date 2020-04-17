package com.zidane.spring.web;

import com.zidane.spring.domain.User;
import com.zidane.spring.util.cons.CommonConstant;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller的基类
 * 保存用户上下文信息
 *
 * @author Zidane
 * @since 2018-08-12
 */
public class BaseController {
    /**
     * 获取保存在Session中的用户对象
     *
     * @param  request  http请求
     * @return User     用户信息
     */
    protected User getSessionUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(CommonConstant.USER_CONTEXT);
    }

    /**
     * 保存用户对象到Session中
     *
     * @param  request  http请求
     * @param  user     用户信息
     */
    protected void setSessionUser(HttpServletRequest request, User user) {
        request.getSession().setAttribute(CommonConstant.USER_CONTEXT, user);
    }
}
