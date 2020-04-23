package com.zidane.spring.util.zk;

/**
 * zk锁
 *
 * @author Zidane
 * @since 2019-02-12
 */
public class Lock {
    private boolean locked;

    public Lock(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}