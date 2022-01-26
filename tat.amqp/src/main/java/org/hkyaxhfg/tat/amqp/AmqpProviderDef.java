package org.hkyaxhfg.tat.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * AmqpProviderDef: 定义遵循Amqp协议的提供者实现.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
@ConfigurationProperties(prefix = AmqpProviderDef.PREFIX)
public class AmqpProviderDef {

    public static final String PREFIX = "hkyaxhfg.tat.amqp-provider-def";

    /**
     * 是否启动Amqp自动配置.
     */
    private boolean enabled = false;
    /**
     * amqp描述.
     */
    private String amqpDescription = "";
    /**
     * 交换机定义.
     */
    private List<ExchangeDef> exchangeDefs;

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

    public List<ExchangeDef> getExchangeDefs() {
        return exchangeDefs;
    }

    public void setExchangeDefs(List<ExchangeDef> exchangeDefs) {
        this.exchangeDefs = exchangeDefs;
    }

    /**
     * ExchangeDef: 定义遵循Amqp协议的交换机.
     */
    public static class ExchangeDef {
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

        /**
         * QueueDef: 定义遵循Amqp协议的队列.
         */
        public static class QueueDef {
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

    }

}
