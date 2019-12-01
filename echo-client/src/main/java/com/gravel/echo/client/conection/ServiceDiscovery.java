package com.gravel.echo.client.conection;

import com.alibaba.fastjson.JSONObject;
import com.gravel.echo.common.constants.EchoConstants;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ServiceDiscovery
 * @Description: 服务发现
 * @Author gravel
 * @Date 2019/11/28
 * @Version V1.0
 **/
@Slf4j
@Component
public class ServiceDiscovery {

    @Value("${registry.address}")
    private String registryAddress;
    @Autowired
    ConnectManage connectManage;

    // 服务地址列表
    private volatile List<String> addressList = new ArrayList<>();
    private ZkClient client;


    @PostConstruct
    public void init() {
        client = connectServer();
        watchNode(client);
    }

    //连接zookeeper
    private ZkClient connectServer() {
        return new ZkClient(registryAddress, 30000, 30000);
    }

    //监听子节点数据变化
    private void watchNode(final ZkClient client) {
        List<String> nodeList = client.subscribeChildChanges(EchoConstants.ZK_REGISTRY_PATH, (s, nodes) -> {
            log.info("监听到子节点数据变化{}", JSONObject.toJSONString(nodes));
            addressList.clear();
            getNodeData(nodes);
            updateConnectedServer();
        });
        getNodeData(nodeList);
        log.info("已发现服务列表...{}", JSONObject.toJSONString(addressList));
        updateConnectedServer();
    }

    //连接生产者端服务
    private void updateConnectedServer() {
        connectManage.updateConnectServer(addressList);
    }

    private void getNodeData(List<String> nodes) {
        log.info("/rpc子节点数据为:{}", JSONObject.toJSONString(nodes));
        for (String node : nodes) {
            String address = client.readData(EchoConstants.ZK_REGISTRY_PATH + "/" + node);
            addressList.add(address);
        }
    }
}