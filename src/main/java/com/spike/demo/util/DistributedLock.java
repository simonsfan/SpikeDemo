package com.spike.demo.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;


/**
 * @ClassName DistributedLock
 * @Description 基于zookeeper实现的分布式锁
 * @Author simonsfan
 * @Date 2018/12/18
 * Version  1.0
 */
public class DistributedLock {
    public static Logger logger = LoggerFactory.getLogger(DistributedLock.class);
    //可重入排它锁
    private InterProcessMutex interProcessMutex;
    //竞争资源标志
    private String lockName;
    //根节点
    private String root = "/distributed/lock/";
    private static CuratorFramework curatorFramework;
    private static String ZK_URL = "10.200.20.85:2181";
    static{
        curatorFramework= CuratorFrameworkFactory.newClient(ZK_URL,new ExponentialBackoffRetry(1000,3));
        curatorFramework.start();
    }

    /**
     * 实例化
     * @param lockName
     */
    public DistributedLock(String lockName){
        try {
            this.lockName = lockName;
            interProcessMutex = new InterProcessMutex(curatorFramework, root + lockName);
        }catch (Exception e){
            logger.error("initial InterProcessMutex exception="+e);
        }
    }

    /**
     * 获取锁
     */
    public boolean acquireLock(){
        int count = 0;
        try {
            // 3秒内会尝试获取锁，加上重试的2次，可能占据9s的时间
            boolean flag = false;
            do{
                count ++ ;
                logger.info("try for "+count+" times");
                flag = interProcessMutex.acquire(9, TimeUnit.SECONDS);
            }while(!flag && count < 3);
            return flag;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 释放锁
     */
    public void releaseLock(){
        try {
            if(interProcessMutex != null && interProcessMutex.isAcquiredInThisProcess()){
                interProcessMutex.release();
                curatorFramework.delete().inBackground().forPath(root+lockName);
                logger.info("Thread:"+Thread.currentThread().getId()+" release distributed lock  success");
            }
        }catch (Exception e){
            logger.info("Thread:"+Thread.currentThread().getId()+" release distributed lock  exception="+e);
        }
    }
}

