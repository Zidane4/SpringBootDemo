package com.zidane.spring.util.exception;

/**
 * 用户异常
 *
 * @author Zidane
 * @since 2018-08-12
 */
public class UserExistException extends Exception
{
    public UserExistException(String errorMsg)
    {
        super(errorMsg);
    }
}
