package org.hkyaxhfg.tat.amqp;

/**
 * ExchangeDef: 定义遵循Amqp协议的交换机.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
public class QueueDef {
    /**
     * id.
     */
    private Long id;
    /**
     * 队列名称.
     */
    private String queueName;
    /**
     * 是否持久.
     */
    private Boolean durable;
    /**
     * 队列名称.
     */
    private String queueDescription;
    /**
     * 路由key.
     */
    private String routingKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public Boolean getDurable() {
        return durable;
    }

    public void setDurable(Boolean durable) {
        this.durable = durable;
    }

    public String getQueueDescription() {
        return queueDescription;
    }

    public void setQueueDescription(String queueDescription) {
        this.queueDescription = queueDescription;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }
}
