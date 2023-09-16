package com.shampohoe.rpc.registry;

import com.shampohoe.rpc.enums.RpcError;
import com.shampohoe.rpc.exception.RpcException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * ClassName:ZkServiceRegistry
 * Package:com.shampohoe.rpc.registry
 * Description: ZooKeeper服务注册中心
 *
 * @Author kkli
 * @Create 2023/9/16 2:46
 * #Version 1.1
 */
@Slf4j

public class ZkServiceRegistry implements ServiceRegistry{


    // curator提供的zookeeper客户端
    private CuratorFramework client;

    // zookeeper根路径节点
    private static final String ROOT_PATH = "MyRPC";

    //连接zookeeper
    {
        // 重试策略
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory
                .builder()
                .connectString("192.168.159.130:2181")
                .sessionTimeoutMs(60 * 1000)
                .connectionTimeoutMs(15 * 1000)
                .retryPolicy(policy)
                .namespace(ROOT_PATH)
                .build();
        this.client.start();
        log.info("zookeeper连接成功");
    }

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        // 注册相应服务
        try {

            //例：servicePath = /com.whc.rpc.api.UserService
            String servicePath = "/" +serviceName;
            //如果节点不存在，则创建
            if(client.checkExists().forPath(servicePath) == null) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(servicePath,"0".getBytes());
            }

            //例：inetSocketAddress = /com.whc.rpc.api.UserService/127.0.0.1:9000
            String path = servicePath + inetSocketAddress;

            log.info(path);
            //创建临时节点：/MyRPC/com.whc.rpc.api.UserService/127.0.0.1:9000
            String rsNode = client.create().withMode(CreateMode.EPHEMERAL).forPath(path,"0".getBytes());
            log.error("服务注册成功："+rsNode);

			/*// serviceName创建成永久节点,服务提供者下线时,不删服务名,只删地址
			if(client.checkExists().forPath("/" + serviceName) == null) {
				client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/" + serviceName, "127.0.0.1".getBytes());
			}
			// 路径地址,一个/代表一个节点
			String path = "/" + serviceName + "/" + getServiceAddress(inetSocketAddress);
			// 临时节点,服务器下线就删除节点
			client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, "127.0.0.1".getBytes());*/
        } catch (Exception e) {
            log.error("服务注册失败,此服务已存在");
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }

    // 根据服务名返回地址
    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            List<String> strings = client.getChildren().forPath("/" + serviceName);
            // 这里默认用的第一个,后面加负载均衡
            String string = strings.get(0);
            return parseAddress(string);
        } catch (Exception e) {
            log.error("获取服务时有错误发生:", e);
        }
        return null;
    }


    // 地址 -> XXX.XXX.XXX.XXX:port 字符串
    private String getServiceAddress(InetSocketAddress serverAddress) {
        return serverAddress.getHostName() +
                ":" +
                serverAddress.getPort();
    }

    // 字符串解析为地址
    private InetSocketAddress parseAddress(String address) {
        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }
}
