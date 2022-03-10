package be.leforem.properties;

import be.leforem.formapass.test.AbstractSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class YamlPropertySourceFactoryIntegrationTest extends AbstractSpringBootTest {

    @Autowired
    private TestProperties properties;

    @Test
    void givenApplicationContextLoaded_thenPropertiesFromYamlLoaded() {
        // Act
        String appVersion = properties.getAppVersion();

        // Check
        assertThat(appVersion).isEqualTo("1.0.0");

    }

}