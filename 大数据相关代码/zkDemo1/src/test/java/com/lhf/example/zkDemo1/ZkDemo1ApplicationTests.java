package com.lhf.example.zkDemo1;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Scanner;

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class ZkDemo1ApplicationTests {

	private ZKClient zkClient = null;

	private static final String ZK_PREFIX = "/Test01";

	@Before
	public void init() throws IOException, InterruptedException {
		zkClient = ZKClient.getZkClient(ZK_PREFIX);
	}


	/**
	 * 增删改查
	 */

	//新增
	@Test
	public void test1() throws UnsupportedEncodingException, KeeperException, InterruptedException {
		String node = "/create";
		byte[] data = "新增第一个Znode".getBytes("utf-8");

		String path = ZK_PREFIX + node;
		Stat stat = zkClient.znodeExist(path);
		if (stat!=null){
			System.out.println("==========>节点已经存在："+ZK_PREFIX+node);
		}else{
			String znode = zkClient.createZnode(ZK_PREFIX + node, data);
			System.out.println(znode);
		}

		Thread.sleep(Long.MAX_VALUE);
	}

	//查看注册监听
	@Test
	public void test2() throws KeeperException, InterruptedException {
		List<String> children = zkClient.getChildren(ZK_PREFIX);
		for (String ch:children){
			System.out.println(ch);
		}

		Thread.sleep(Long.MAX_VALUE);
	}




}

