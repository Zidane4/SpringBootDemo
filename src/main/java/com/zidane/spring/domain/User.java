package com.zidane.spring.domain;

import java.util.Date;

/**
 * 用户信息pojo
 *
 * @author Zidane
 * @since 2018-08-12
 */
public class User {
    private int userId;

    private String userName;

    /**
     * 用户类型：0-普通用户 1-超級管理员
     */
    private int userType;

    private String lastIp;

    private Date lastVisit;

    private String password;

    /**
     * 锁定标志位：0-未锁定 1-锁定，只有超級管理员才有锁定的权限
     */
    private int locked;

    private int credit;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public Date getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    @Override
    public String toString() {
        return "User{" + "userId=" + userId + ", userName='" + userName + '\'' + ", userType=" + userType + ", lastIp='"
                + lastIp + '\'' + ", lastVisit=" + lastVisit + ", password='" + password + '\'' + ", locked=" + locked
                + ", credit=" + credit + '}';
    }
}
