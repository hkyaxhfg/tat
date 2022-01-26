package org.hkyaxhfg.tat.validation;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationLogger;
import org.hkyaxhfg.tat.lang.util.LoggerGenerator;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * javax.validation的验证自动配置.
 *
 * @author: wjf
 * @date: 2022/1/19
 */
@Configuration
@ComponentScan(
        basePackages = {
                "org.hkyaxhfg.tat.validation"
        }
)
public class ValidationAutoConfigurationInitializer {

    @Bean
    public Validator validator() {
        AutoConfigurationLogger.autoconfigurationInfo("Validation");
        return Validation
                .byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()
                .getValidator();
    }

}
