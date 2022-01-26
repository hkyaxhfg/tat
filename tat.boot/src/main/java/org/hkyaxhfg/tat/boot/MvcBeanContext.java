package org.hkyaxhfg.tat.boot;

import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationLogger;
import org.hkyaxhfg.tat.lang.util.LoggerGenerator;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * Mvc bean 注册器, 请注意定义bean的时候请考虑注册时机, 要在spring调用registry注册bean实例之前定义.
 *
 * @author: wjf
 * @date: 2022/1/14
 */
@Component
@SuppressWarnings("all")
public class MvcBeanContext implements BeanDefinitionRegistryPostProcessor {

    private static final Logger logger = LoggerGenerator.logger(MvcBeanContext.class);

    private static BeanDefinitionRegistry registry;

    private static ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        MvcBeanContext.registry = registry;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        MvcBeanContext.beanFactory = beanFactory;
    }

    public static void defRegisterBean(String beanName, Class<?> beanClass) {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
        rootBeanDefinition.setBeanClass(beanClass);

        MvcBeanContext.registry.registerBeanDefinition(beanName, rootBeanDefinition);
    }

    public static void registerBean(String beanName, Object beanInstance) {
        MvcBeanContext.beanFactory.registerSingleton(beanName, beanInstance);
    }

    public static void registerBean(Class<?> beanClass) {
        MvcBeanContext.beanFactory.createBean(beanClass);
    }

    public static boolean containsBean(String beanName) {
        return MvcBeanContext.beanFactory.containsBean(beanName);
    }

    public static <T> T getBean(Class<T> beanClass) {
        return MvcBeanContext.beanFactory.getBean(beanClass);
    }

    public static <T> T getBean(String beanName, Class<T> beanClass) {
        return MvcBeanContext.beanFactory.getBean(beanName, beanClass);
    }

}
