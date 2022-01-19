package org.hkyaxhfg.tat.springfox;

import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.service.ParameterType;

import java.util.Collections;
import java.util.List;

/**
 * springfox的自动配置属性.
 *
 * @author: wjf
 * @date: 2022/1/12
 */
@ConfigurationProperties(AutoConfigurationProperty.SPRINGFOX_MAIN_KEY)
public class SpringfoxProperties {
    /**
     * 是否启动文档, 默认为true.
     */
    private boolean enabled = AutoConfigurationProperty.SPRINGFOX.isDefaultValue();
    /**
     * 文档标题, 默认为 ${spring.application.name}.
     */
    private String docTitle = "spring.application.name";
    /**
     * 文档描述, 默认为 ${spring.application.name}.
     */
    private String docDesc = "spring.application.name";
    /**
     * 文档维护者名称, 默认为 hkyaxhfg.
     */
    private String maintainerName = "hkyaxhfg";
    /**
     * 文档维护者网址, 默认为 https://hkyaxhfg.org.
     */
    private String maintainerUrl = "https://hkyaxhfg.org";
    /**
     * 文档维护者邮箱, 默认为 hkyaxhfg@gmail.com.
     */
    private String maintainerEmail = "hkyaxhfg@gmail.com";
    /**
     * 全局参数, 在生成文档时的全局参数, 详细请看: {@link org.hkyaxhfg.tat.springfox.SpringfoxProperties.RequestParameter}.
     */
    private List<RequestParameter> globalRequestParameters = Collections.emptyList();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public String getDocDesc() {
        return docDesc;
    }

    public void setDocDesc(String docDesc) {
        this.docDesc = docDesc;
    }

    public String getMaintainerName() {
        return maintainerName;
    }

    public void setMaintainerName(String maintainerName) {
        this.maintainerName = maintainerName;
    }

    public String getMaintainerUrl() {
        return maintainerUrl;
    }

    public void setMaintainerUrl(String maintainerUrl) {
        this.maintainerUrl = maintainerUrl;
    }

    public String getMaintainerEmail() {
        return maintainerEmail;
    }

    public void setMaintainerEmail(String maintainerEmail) {
        this.maintainerEmail = maintainerEmail;
    }

    public List<RequestParameter> getGlobalRequestParameters() {
        return globalRequestParameters;
    }

    public void setGlobalRequestParameters(List<RequestParameter> globalRequestParameters) {
        this.globalRequestParameters = globalRequestParameters;
    }

    /**
     * 请求的全局参数.
     */
    public static class RequestParameter {
        /**
         * 参数名称.
         */
        private String name;
        /**
         * 参数描述.
         */
        private String desc;
        /**
         * 参数数据类型, 配置文件中以字符串形式存在, 可取值请看:
         * {@link org.hkyaxhfg.tat.springfox.DataType}.
         */
        private DataType dataType;
        /**
         * 参数是否必填.
         */
        private boolean required;
        /**
         * 参数的传参方式, 可取值请看:
         * {@link springfox.documentation.service.ParameterType}.
         */
        private ParameterType parameterType;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public DataType getDataType() {
            return dataType;
        }

        public void setDataType(DataType dataType) {
            this.dataType = dataType;
        }

        public boolean isRequired() {
            return required;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }

        public ParameterType getParameterType() {
            return parameterType;
        }

        public void setParameterType(ParameterType parameterType) {
            this.parameterType = parameterType;
        }
    }
}
