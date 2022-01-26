package org.hkyaxhfg.tat.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * AmqpConsumerDef: 定义遵循Amqp协议的消费者实现.
 *
 * @author: wjf
 * @date: 2022/1/19
 */
@ConfigurationProperties(prefix = AmqpConsumerDef.PREFIX)
public class AmqpConsumerDef {

    public static final String PREFIX = "hkyaxhfg.tat.amqp-consumer-def";

    /**
     * 是否启动Amqp自动配置.
     */
    private boolean enabled = false;
    /**
     * amqp描述.
     */
    private String amqpDescription = "";
    /**
     * 消息队列监听器.
     */
    private List<MessageListener> messageListeners;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAmqpDescription() {
        return amqpDescription;
    }

    public void setAmqpDescription(String amqpDescription) {
        this.amqpDescription = amqpDescription;
    }

    public List<MessageListener> getMessageListeners() {
        return messageListeners;
    }

    public void setMessageListeners(List<MessageListener> messageListeners) {
        this.messageListeners = messageListeners;
    }

    /**
     * 消息队列监听器.
     */
    public static class MessageListener {

        /**
         * 交给spring容器管理的队列监听器的bean名称.
         */
        private String queueListenerBeanName;
        /**
         * 监听队列的方法名称, 此方法定义必须是以下格式:
         * 1. void methodName(String),
         * 2. returnType methodName(String).
         */
        private String listenerMethodName;
        /**
         * 队列消息转换器, 当触发监听时消息的解析方式, 默认为字符串方式.
         */
        private AmqpUtils.ConverterType messageConverterType = AmqpUtils.ConverterType.STRING;
        /**
         * 当前监听器方法监听的队列的队列名称, 多个队列名称使用[,]隔开.
         */
        private String queueNames;

        public String getQueueListenerBeanName() {
            return queueListenerBeanName;
        }

        public void setQueueListenerBeanName(String queueListenerBeanName) {
            this.queueListenerBeanName = queueListenerBeanName;
        }

        public String getListenerMethodName() {
            return listenerMethodName;
        }

        public void setListenerMethodName(String listenerMethodName) {
            this.listenerMethodName = listenerMethodName;
        }

        public AmqpUtils.ConverterType getMessageConverterType() {
            return messageConverterType;
        }

        public void setMessageConverterType(AmqpUtils.ConverterType messageConverterType) {
            this.messageConverterType = messageConverterType;
        }

        public String getQueueNames() {
            return queueNames;
        }

        public void setQueueNames(String queueNames) {
            this.queueNames = queueNames;
        }

    }

}
