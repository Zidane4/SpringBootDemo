package com.zidane.spring.util.cons;

/**
 * 系统常量
 *
 * @author Zidane
 * @since 2018-08-12
 */
public class CommonConstant
{
   /**
    * 用户对象放到Session中的键名称
    */
   public static final String USER_CONTEXT = "USER_CONTEXT";

   /**
    * 超级管理员的固定用户名
    */
   public static final String SUPER_USER_NAME = "admin";

   /**
    * 返回页面错误信息对应的key值
    */
   public static final String ERROR_MSG_KEY = "errorMsg";

   /**
    * 黑名单在redis存储中的key值
    */
   public static final String BLACKLIST_REDIS_KEY = "blackList";
}
