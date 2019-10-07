package com.aosom.aosombase.nacos;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 解析
 */
public class MoreSiteConfigInitializer implements ApplicationContextInitializer  {



    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        DefaultListableBeanFactory defaultListableBeanFactory =(DefaultListableBeanFactory)applicationContext.getBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .rootBeanDefinition(MoreSiteBeanFactoryPostProcessor.class);

        defaultListableBeanFactory.registerBeanDefinition(MoreSiteBeanFactoryPostProcessor.BEAN_NAME, beanDefinitionBuilder.getBeanDefinition());


    }


}
