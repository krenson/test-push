package be.leforem.properties;

import be.leforem.formapass.test.AbstractUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class YamlPropertySourceFactoryUnitTest extends AbstractUnitTest {

    private static final String YAML_FILE_NAME = "testProperties.yml";
    private static final YamlPropertySourceFactory FACTORY = new YamlPropertySourceFactory();
    private static final String SOURCE_NAME = "mySource";
    private static final EncodedResource YAML_RESOURCE = new EncodedResource(new ClassPathResource(YAML_FILE_NAME));

    @Test
    void givenValidYaml_whenYamlPropertiesLoaded_thenThePropertiesCanBeRetrievedAndTheSourceNameIsTheSameAsTheProvidedSourceName() {
        // Act
        PropertySource<?> propertySource = FACTORY.createPropertySource(SOURCE_NAME, YAML_RESOURCE);

        // Check
        Object appVersion = propertySource.getProperty("application.version");
        assertThat(appVersion).isEqualTo("1.0.0");
        assertThat(propertySource.getName()).hasToString(SOURCE_NAME);
    }

    @Test
    void givenYamlNotProvided_whenYamlPropertiesLoaded_thenAnExceptionIsThrown() {
        // Act and check
        assertThatNullPointerException().isThrownBy(() -> FACTORY.createPropertySource(SOURCE_NAME, null));
    }

    @Test
    void givenValidYamlWithBlankSourceName_whenYamlPropertiesLoaded_thenTheSourceNameIsTheResourceName() {
        // Act
        PropertySource<?> propertySource = FACTORY.createPropertySource(" ", YAML_RESOURCE);

        // Check
        assertThat(propertySource.getName()).hasToString(YAML_FILE_NAME);
    }

}