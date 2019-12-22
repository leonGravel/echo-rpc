package com.gravel.echo.common.zookeeper;

import com.gravel.echo.common.constants.EchoConstants;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName zkManager
 * @Description: zookeeper 管理类
 * @Author gravel
 * @Date 2019/12/5
 * @Version V1.0
 **/
@Slf4j
@Component
public class ZkManager {
    @Value("${registry.address}")
    private String registryAddress;


    /**
     * 注册zookeeper
     * @param data
     */
    public void register(String data) {
        if (data == null) {
            return;
        }
        ZkClient client = connectServer();
        AddRootNode(client);
        createNode(client, data);
    }

    /**
     * 连接zookeeper
     * @return
     */
    public ZkClient connectServer() {
        return new ZkClient(registryAddress, 20000, 20000);
    }

    /**
     * 创建根目录
     * @param client
     */
    private void AddRootNode(ZkClient client) {
        boolean exists = client.exists(EchoConstants.ZK_REGISTRY_PATH);
        if (!exists) {
            client.createPersistent(EchoConstants.ZK_REGISTRY_PATH);
            log.info("创建zookeeper主节点 {}", EchoConstants.ZK_REGISTRY_PATH);
        }
    }

    /**
     * 在根目录下，创建临时顺序子节点
     * @param client
     * @param data
     */
    private void createNode(ZkClient client, String data) {
        String path = client.create(EchoConstants.ZK_REGISTRY_PATH + "/provider", data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        log.info("创建zookeeper数据节点 ({} => {})", path, data);
    }
}
