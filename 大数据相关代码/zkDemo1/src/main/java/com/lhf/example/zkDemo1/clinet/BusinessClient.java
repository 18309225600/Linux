package com.lhf.example.zkDemo1.clinet;

import com.lhf.example.zkDemo1.ZKClient;
import com.lhf.example.zkDemo1.server.BusinessServer;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务客户端代码；
 *      主要功能如下：
 *          1.启动客户端
 *          2.获取zk链接
 *          3.读取服务端内容
 *          4.执行客户端代码
 */
public class BusinessClient {

    private static  volatile List<String> serversShow = new ArrayList();

    /**
     * 约定的zk配置
     */
    private static final String  ZK_PREFIX="/ZK_SERVER";

    public static void  main(String[] args) throws IOException, KeeperException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        //1.获取zk链接
        BusinessClient client = new BusinessClient();
        ZKClient zkConnection = ZKClient.getZkClient(ZK_PREFIX,client,"child",true);

        //2.服务器回调获取数据
        invoke(zkConnection);

        //3.执行业务代码
        client.businessHandle();
    }

    public static void invoke(ZKClient zkConnection) throws KeeperException, InterruptedException {
        List<String> Znodes = zkConnection.getChildren(ZK_PREFIX);
        List<String> servers = new ArrayList<>();
        for (String znode:Znodes){
            byte[] server = zkConnection.getZnode(ZK_PREFIX+"/"+znode);
            servers.add(new String(server));
        }

        serversShow = servers;
        System.out.println(serversShow);
    }

    private void businessHandle() throws InterruptedException {
        System.out.println("============>客户端业务代码。。。");
        System.out.println(serversShow);
        Thread.sleep(Long.MAX_VALUE);
    }
}
