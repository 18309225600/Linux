package com.lhf.example.zkDemo1;

import com.lhf.example.zkDemo1.clinet.BusinessClient;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.proto.WatcherEvent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.WatchEvent;
import java.util.List;
import java.util.function.Consumer;

/**
 * 总结常用API 和注册监听
 * 0. 监听在操作时，对应业务需求，决定是否需要加监听。加监听注意监听回调中的具体实现。
 * 1. 在获取一个节点的子节点时注册监听，子节点数量变化，会回调  zkClient.getchildren(,true)    对应功能 ：增删Znode
 * 2. 在获取一个节点的数据时注册监听，数据发生变化会回调，例如做负载均衡，节点连接数发生变化，相应的client变化 zkClient.getData(,true)  对应功能：改Znode
 */

public class ZKClient {

    private static final String  HOSTS = "192.168.85.135:2181,192.168.85.136:2181,192.168.85.137:2181";
    private static final int sessionTimeout = 2000;
    public ZooKeeper zkClient = null;
    public static ZKClient instance = null;

    public static String watchPath = "/";

    private ZKClient() throws IOException {
        initZkClient(watchPath,null);
    }

    private ZKClient(String watchPath,Object object) throws IOException {
        this.watchPath = watchPath;
        initZkClient(watchPath,object);
    }


    //创建基于一个节点的监听,创建监听的时候，判断是否有回调，（是否有必要设置监听，基于什么类型的监听，参数已经传入了，暂未写代码）
    //这样就实现了一个通用的zk框架，只不过还比较弱
    //回调应该抽取统一接口，将方法名字规定为invoke 要不然可能会有问题。
    private void initZkClient(String watchPath,Object object) throws IOException {
        zkClient = new ZooKeeper(HOSTS,sessionTimeout,(WatchedEvent event)->{
            System.out.println(event.getType()+"-----------"+event.getPath()+"-----------"+event.getState()+"------------"+event.getWrapper());
            try {
                System.out.println("=============将要回调"+object);
                if (object!=null){
                    System.out.println("=================>回调");
                    Method method = object.getClass().getDeclaredMethod("invoke", ZKClient.class);
                    method.invoke(object,instance==null?new ZKClient():instance);
                }

                zkClient.getChildren(watchPath,true);
            } catch (KeeperException | NoSuchMethodException | IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }


    public static ZKClient getZkClient(String watchPath, Object object, String child, boolean b) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
//        回调方法

        if (instance==null){
            instance = new ZKClient(watchPath,object);
        }
        return instance;
    }

    public static ZKClient getZkClient(String watchPath) throws IOException {
        if (instance==null){
            instance = new ZKClient(watchPath,null);
        }
        return instance;
    }

    //查询子节点
    public List<String> getChildren(String path) throws KeeperException, InterruptedException {
        List<String> children = zkClient.getChildren(path, true);
        return children;
    }


    //判断节点是否存在
    public Stat znodeExist(String path) throws KeeperException, InterruptedException {
        Stat stat= zkClient.exists(path, false);
        return stat;
    }

    //新增一个节点
    public String createZnode(String path,byte[] data) throws KeeperException, InterruptedException {
        String s = zkClient.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        return s;
    }

    //删除一个节点
    public void deleteZnode(String znode) throws KeeperException, InterruptedException {
        zkClient.delete(znode,-1);
    }

    //修改一个节点
    public Stat setZnodeData(String znode,byte[] data) throws KeeperException, InterruptedException {
        Stat stat = zkClient.setData(znode, data, -1);
        return stat;
    }

    //查询节点数据
    public byte[] getZnode(String path) throws KeeperException, InterruptedException {
        byte[] data = zkClient.getData(path, true, null);
        return data;
    }

}
