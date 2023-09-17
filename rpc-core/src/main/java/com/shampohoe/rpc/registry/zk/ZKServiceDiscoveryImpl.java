package com.shampohoe.rpc.registry.zk;

import com.shampohoe.rpc.enums.RpcError;
import com.shampohoe.rpc.exception.RpcException;
import com.shampohoe.rpc.loadbalance.LoadBalancer;
import com.shampohoe.rpc.loadbalance.loadbalancer.RandomLoadBalance;
import com.shampohoe.rpc.registry.ServiceDiscovery;
import com.shampohoe.rpc.registry.zk.util.CuratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;


import java.net.InetSocketAddress;
import java.util.List;

/**
 * 服务发现实现类
 * @ClassName: ZKServiceDiscoveryImpl
 * @Author: whc
 * @Date: 2021/06/14/0:57
 */
@Slf4j
public class ZKServiceDiscoveryImpl implements ServiceDiscovery {
	private final LoadBalancer loadBalancer;

	public ZKServiceDiscoveryImpl() {
		this(null);
	}

	public  ZKServiceDiscoveryImpl(LoadBalancer loadBalancer) {
		if(loadBalancer == null) {
			this.loadBalancer = new RandomLoadBalance();
		} else {
			this.loadBalancer = loadBalancer;
		}
	}

	@Override
	public InetSocketAddress serviceDiscovery(String serviceName) {
		CuratorFramework zkClient = CuratorUtils.getZkClient();
		// 获取服务地址列表
		List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, serviceName);
		if (serviceUrlList == null || serviceUrlList.size() == 0) {
			throw new RpcException(RpcError.SERVICE_NOT_FOUND, serviceName);
		}

		// 负载均衡
		String targetServiceUrl = loadBalancer.balance(serviceUrlList);
		log.info("通过负载均衡策略,获取到服务地址:[{}]", targetServiceUrl);
		String[] socketAddressArray = targetServiceUrl.split(":");
		String host = socketAddressArray[0];
		int port = Integer.parseInt(socketAddressArray[1]);
		return new InetSocketAddress(host, port);
	}
}
