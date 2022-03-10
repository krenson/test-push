package be.leforem.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "testProperties.yml", factory = YamlPropertySourceFactory.class)
public class TestProperties {

    private final String appVersion;

    public TestProperties( @Value("${application.version}" ) String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

}