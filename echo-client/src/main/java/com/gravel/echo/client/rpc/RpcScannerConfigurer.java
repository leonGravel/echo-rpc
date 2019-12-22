package com.gravel.echo.client.rpc;

import com.gravel.echo.common.constants.EchoConstants;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @ClassName RpcScannerConfigurer
 * @Description: 扫描客户端需要代理的类
 * @Author gravel
 * @Date 2019/11/28
 * @Version V1.0
 **/
@Component
public class RpcScannerConfigurer implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        ClassPathRpcScanner scanner = new ClassPathRpcScanner(beanDefinitionRegistry);

        scanner.registerFilters();
        scanner.scan(StringUtils.tokenizeToStringArray(EchoConstants.BASE_PACKAGE, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }


}
