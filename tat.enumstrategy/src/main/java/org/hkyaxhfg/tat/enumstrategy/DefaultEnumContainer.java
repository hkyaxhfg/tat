package org.hkyaxhfg.tat.enumstrategy;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hkyaxhfg.tat.lang.reflect.MethodReflector;
import org.hkyaxhfg.tat.lang.util.LoggerGenerator;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 默认的枚举容器.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
public class DefaultEnumContainer implements EnumContainer, InitializingBean {

    private static final Logger logger = LoggerGenerator.logger(DefaultEnumContainer.class);

    private final Map<String, List<EnumStrategy<?>>> enumMap = new HashMap<>();

    private final EnumProperties enumProperties;

    public DefaultEnumContainer(EnumProperties enumProperties) {
        this.enumProperties = enumProperties;
    }

    @Override
    public Map<String, List<EnumStrategy<?>>> findAll() {
        return enumMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (enumProperties.isEnabled()) {
            scans().forEach(this::load);
        }
    }

    /**
     * 扫描包.
     * @return List<Reflections>.
     */
    private List<Reflections> scans() {
        String packageNames = this.enumProperties.getScans();
        if (StringUtils.isBlank(packageNames)) {
            return Collections.emptyList();
        }

        return Arrays.stream(packageNames.split(","))
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .map(pack -> {
                    logger.info("扫描到Enum包: {}", pack);
                    return new Reflections(
                            new ConfigurationBuilder()
                                    .setUrls(ClasspathHelper.forPackage(pack))
                                    .filterInputsBy(new FilterBuilder().includePattern(String.format("%s.*", pack)))
                                    .setScanners(Scanners.SubTypes)

                    );
                }).collect(Collectors.toList());
    }

    /**
     * 加载每一个反射.
     * @param reflections reflections.
     */
    @SuppressWarnings("all")
    private void load(Reflections reflections) {
        try {
            Set<Class<? extends Enum>> enumClassSet = reflections.getSubTypesOf(Enum.class);

            if (CollectionUtils.isEmpty(enumClassSet)) {
                return;
            }

            enumClassSet
                    .stream()
                    .filter(enumClass -> {
                        List<Class<?>> interfaces = Arrays.asList(enumClass.getInterfaces());
                        return interfaces.contains(EnumStrategy.class);
                    })
                    .forEach(enumClass -> {
                        MethodReflector methodReflector = new MethodReflector(enumClass, "values");
                        EnumStrategy<?>[] enums = methodReflector.invoke(null);
                        enumMap.put(enumClass.getTypeName(), Arrays.asList(enums));
                    });
        } catch (ReflectionsException e) {
            return;
        }
    }

}
