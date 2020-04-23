package com.zidane.spring.util.zk;

import groovy.util.logging.Slf4j;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取zk分布式锁
 *
 * @author Zidane
 * @since 2019-02-12
 */
@Slf4j
public class ZkLockUtil {

    private static final Logger logger = LoggerFactory.getLogger(ZkLockUtil.class);

    private static final int LOCKWAITTIME = 5;

    /**
     * 本地IP
     */
    private static String hostIp;

    /**
     * zk服务端地址
     */
    private static String zkServerAddress;

    /**
     * 连接创建超时时间，单位毫秒
     */
    private static int connectionTimeout;

    /**
     * 会话超时时间，单位毫秒
     */
    private static int sessionTimeout;

    /**
     * 互斥锁的存储路径
     */
    private static String mutexPath;

    /**
     * 临时zk节点路径
     */
    private static String lockPath;

    /**
     * zk客户端
     */
    private static CuratorFramework zkClient;

    /**
     * 初始化zk客戶端
     */
    public void initZkClient() {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(getZkServerAddress())
                .sessionTimeoutMs(getSessionTimeout())
                .connectionTimeoutMs(getConnectionTimeout())
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
    }

    /**
     * 在分布式中，只有一个节点能够获取到锁，如果某一节点获取到锁之后，该节点将一直持有该锁。
     * 首先获取一个zk互斥锁，获取到互斥锁之后添加一个临时zk节点，表示获取到锁成功，如果其他节点检测到节点存在，则获取不到锁
     * 获取到锁的节点如果重启或者宕机，zk上的临时节点也会消失，其他节点就可以重新获取锁了
     *
     * @param locked 本地内存变量，表示之前是否已经获取到锁
     * @return boolean   是否获取了执行权限(true:执行; false:不执行)
     */
    public static boolean getOneZkLock(Lock locked) {
        // locked为false（表示没有在当前节点执行）
        if (!locked.isLocked()) {
            // 采取Curator分布式方案，首先创建processMutex对象
            InterProcessMutex processMutex = new InterProcessMutex(zkClient, mutexPath);
            try {
                // 抢分布式锁
                if (processMutex.acquire(LOCKWAITTIME, TimeUnit.SECONDS)) {
                    // 抢到了之后判断有没有临时节点，如果有则说明已经有其他节点之前抢到锁并且执行了，返回false
                    if (zkClient.checkExists().forPath(lockPath) != null) {
                        return false;
                    } else {
                        //如果不存在临时节点，说明本节点第一次抢到锁，则根据本节点ip创建临时节点
                        zkClient.create().withMode(CreateMode.EPHEMERAL).forPath(lockPath, hostIp.getBytes());
                        //设置locked为true，并返回成功
                        locked.setLocked(true);
                        return true;
                    }
                } else {
                    logger.info("Get distribution lock failed.");
                    return false;
                }
            } catch (Exception e) {
                logger.error("Get distribution lock failed, message is {}", e.getMessage());
                return false;
            } finally {
                try {
                    // 不管成功与否，均释放锁，这样后续可以继续抢锁
                    processMutex.release();
                } catch (Exception e) {
                    logger.error("Relase zk lock falied, lock path = " + mutexPath);
                }
            }
        } else {
            try {
                // 如果locked为true，说明此节点之前判定是抢到锁的节点
                // 判断临时节点是否存在
                if (zkClient.checkExists().forPath(lockPath) != null) {
                    String ip = new String(zkClient.getData().forPath(lockPath));
                    // 如果临时节点上的ip是本节点，属于正常情况，则返回成功，开始执行后续流程
                    if (hostIp.equals(ip)) {
                        return true;
                    } else {
                        // locked为true、ip不是本节点，说明上一次是在本节点执行的，但是可能因为本节点的zk突然连接断了，
                        // 其他节点抢到锁并已经开始执行，所以此时设置locked为false，并返回失败
                        locked.setLocked(false);
                        return false;
                    }
                } else {
                    // locked为true、临时节点不存在，说明上一次是在本节点执行的，但是可能zk突然连接断了，所以临时节点信息不存在了，
                    // 而且现在还没有其他节点抢到锁，那么此时需要将标志位设为false，重新开始抢锁流程
                    locked.setLocked(false);
                    return getOneZkLock(locked);
                }
            } catch (Exception e) {
                logger.error("Get distribution lock failed, message is {}", e.getMessage());
                return false;
            }
        }
    }

    public String getHostIp() {
        return hostIp;
    }

    public static void setHostIp(String hostIp) {
        ZkLockUtil.hostIp = hostIp;
    }

    public String getZkServerAddress() {
        return zkServerAddress;
    }

    public static void setZkServerAddress(String zkServerAddress) {
        ZkLockUtil.zkServerAddress = zkServerAddress;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public static void setSessionTimeout(int sessionTimeout) {
        ZkLockUtil.sessionTimeout = sessionTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public static void setConnectionTimeout(int connectionTimeout) {
        ZkLockUtil.connectionTimeout = connectionTimeout;
    }

    public String getMutexPath() {
        return mutexPath;
    }

    public static void setMutexPath(String mutexPath) {
        ZkLockUtil.mutexPath = mutexPath;
    }

    public String getLockPath() {
        return lockPath;
    }

    public static void setLockPath(String lockPath) {
        ZkLockUtil.lockPath = lockPath;
    }
}