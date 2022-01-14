package org.hkyaxhfg.tat.springfox;

import io.swagger.annotations.ApiOperation;
import org.hkyaxhfg.tat.lang.util.Container;
import org.hkyaxhfg.tat.lang.util.Unaware;
import org.springframework.core.env.Environment;
import org.springframework.util.RouteMatcher;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 默认的springfox定义器.
 *
 * @author: wjf
 * @date: 2022/1/12
 */
public class DefaultSpringfoxDefiner implements SpringfoxDefiner {

    private final SpringfoxProperties springfoxProperties;

    private final Environment environment;

    public DefaultSpringfoxDefiner(SpringfoxProperties springfoxProperties, Environment environment) {
        this.springfoxProperties = springfoxProperties;
        this.environment = environment;
    }

    @Override
    public Docket definition() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .globalRequestParameters(globalRequestParameters())
                .ignoredParameterTypes()
                .forCodeGeneration(true)
                .consumes(Collections.singleton("application/x-www-form-urlencoded"))
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(
                        "spring.application.name".equals(this.springfoxProperties.getDocTitle())
                                ? this.environment.getProperty(this.springfoxProperties.getDocTitle())
                                : this.springfoxProperties.getDocTitle()
                )
                .description(
                        "spring.application.name".equals(this.springfoxProperties.getDocDesc())
                                ? this.environment.getProperty(this.springfoxProperties.getDocDesc())
                                : this.springfoxProperties.getDocDesc()
                )
                .termsOfServiceUrl(serviceUrl().get())
                .contact(new Contact(
                        this.springfoxProperties.getMaintainerName(),
                        this.springfoxProperties.getMaintainerUrl(),
                        this.springfoxProperties.getMaintainerEmail()
                ))
                .version("3.0.0")
                .build();
    }

    @SuppressWarnings("all")
    public Supplier<String> serviceUrl() {
        return () -> {
            Container<String> container = new Container<>();
            Unaware.exceptionUnaware(() -> container.put(InetAddress.getLocalHost().getHostAddress()));
            StringBuilder serviceUrlBuilder = new StringBuilder(NET_SCHEMA);
            serviceUrlBuilder.append("://")
                    .append(container.get())
                    .append(":")
                    .append(this.environment.getProperty("server.port"))
                    .append(this.environment.getProperty("server.servlet.context-path"))
                    .append("/swagger-ui/index.html");
            return serviceUrlBuilder.toString();
        };
    }

    private List<RequestParameter> globalRequestParameters() {
        return this.springfoxProperties.getGlobalRequestParameters()
                .stream()
                .map(parameter -> new RequestParameterBuilder()
                        .name(parameter.getName())
                        .description(parameter.getDesc())
                        .query(
                                specificationBuilder -> specificationBuilder.model(
                                        modelSpecificationBuilder ->
                                                modelSpecificationBuilder.scalarModel(parameter.getDataType().getScalarType())
                                )
                        )
                        .required(parameter.isRequired())
                        .in(parameter.getParameterType())
                        .build()).collect(Collectors.toList());
    }

}
