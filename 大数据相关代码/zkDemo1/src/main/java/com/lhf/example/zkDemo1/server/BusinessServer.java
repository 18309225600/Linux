package com.lhf.example.zkDemo1.server;

import com.lhf.example.zkDemo1.ZKClient;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;

/**
 *  业务服务端；
 *      主要功能如下：
 *
 *          1.启动服务
 *          2.获取到zk链接
 *          3.将自己注册到zk上
 *          4.执行相关业务代码
 */
public class BusinessServer {

    /**
     * 约定的zk配置
     */
    private static final String  ZK_PREFIX="/ZK_SERVER";



    public static void  main(String[] args) throws IOException, KeeperException, InterruptedException {
        //1.获取zk链接
        BusinessServer server = new BusinessServer();
        ZKClient zkConnection = ZKClient.getZkClient(ZK_PREFIX);

        //2.注册到服务器上
        registAtZK(zkConnection,server);

        //3.执行业务代码
        server.businessHandle();
    }

    private void businessHandle() throws InterruptedException {
        System.out.println("============>服务端业务代码。。。");
        Thread.sleep(Long.MAX_VALUE);
    }

    private static void registAtZK(ZKClient zkConnection, BusinessServer server) throws KeeperException, InterruptedException {
        zkConnection.createZnode(ZK_PREFIX+"/server",server.getClass().toString().getBytes());
    }
}
