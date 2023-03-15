package be.leforem.properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.util.Properties;

import static java.util.Objects.requireNonNull;

public class YamlPropertySourceFactory implements PropertySourceFactory {

    private static final String HELLO = "Hello";

    public PropertySource<?> createPropertySource(String name, EncodedResource yamlResource) {
        Properties propertiesFromYaml = toProperties(requireNonNull(yamlResource));
        String sourceName = StringUtils.defaultIfBlank(name, yamlResource.getResource().getFilename());
        return new PropertiesPropertySource(sourceName, requireNonNull(propertiesFromYaml));
    }

    private Properties toProperties(EncodedResource yamlResource) {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(yamlResource.getResource());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

}