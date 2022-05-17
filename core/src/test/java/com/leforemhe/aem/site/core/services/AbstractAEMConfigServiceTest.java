package com.leforemhe.aem.site.core.services;

import com.leforemhe.aem.site.core.AbstractAEMTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractAEMConfigServiceTest<T> extends AbstractAEMTest {

    private static final transient Logger LOG = LoggerFactory.getLogger(AbstractAEMConfigServiceTest.class);

    protected T configService = null;

    private Map<String, Object> attributes;

    public void setup() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        attributes = setupConfigAttributes();
        Object[] attributesList = new Object[attributes.size() * 2];
        int index = 0;
        for (String key : attributes.keySet()) {
            attributesList[index] = key;
            attributesList[index + 1] = attributes.get(key);
            index += 2;
        }
        configService = context.registerInjectActivateService(getInstanceOfT(), attributesList);

    }

    protected abstract Map<String, Object> setupConfigAttributes();

    private T getInstanceOfT() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<T> type = getType();
        return type.getDeclaredConstructor().newInstance();

    }

    private Class<T> getType() {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> type = (Class<T>) superClass.getActualTypeArguments()[0];
        return type;
    }

    @Test
    public void checkConfiguration() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (String key : attributes.keySet()) {
            Object config = getConfig();
            Object result = config.getClass().getMethod(key).invoke(config);
            if(attributes.get(key).getClass().isArray()){
                assertArrayEquals((Object[]) attributes.get(key), (Object[]) result);
            }else {
                assertEquals(attributes.get(key), result);
            }
        }
    }

    private Object getConfig() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object configObject = configService.getClass().getMethod("getConfig").invoke(configService);
        return configObject;
    }
}
