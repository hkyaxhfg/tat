package org.hkyaxhfg.tat.amqp;

import java.util.List;

/**
 * ExchangeDef: 定义遵循Amqp协议的交换机.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
public class ExchangeDef {
    /**
     * id.
     */
    private Long id;
    /**
     * 交换机名称.
     */
    private String exchangeName;
    /**
     * 交换机类型.
     */
    private String exchangeType;
    /**
     * 是否持久.
     */
    private Boolean durable;
    /**
     * 交换机描述.
     */
    private String exchangeDescription;
    /**
     * 队列定义.
     */
    private List<QueueDef> queueDefs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public Boolean getDurable() {
        return durable;
    }

    public void setDurable(Boolean durable) {
        this.durable = durable;
    }

    public String getExchangeDescription() {
        return exchangeDescription;
    }

    public void setExchangeDescription(String exchangeDescription) {
        this.exchangeDescription = exchangeDescription;
    }

    public List<QueueDef> getQueueDefs() {
        return queueDefs;
    }

    public void setQueueDefs(List<QueueDef> queueDefs) {
        this.queueDefs = queueDefs;
    }
}
