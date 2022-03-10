package be.leforem.properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.util.Properties;

public class YamlPropertySourceFactory implements PropertySourceFactory {

    public PropertySource<?> createPropertySource(String name, EncodedResource resource) {
        Properties propertiesFromYaml = toProperties(resource);
        String sourceName = StringUtils.defaultIfBlank(name, resource.getResource().getFilename());
        return new PropertiesPropertySource(sourceName, propertiesFromYaml);
    }

    private Properties toProperties(EncodedResource yamlResource) {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(yamlResource.getResource());
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}