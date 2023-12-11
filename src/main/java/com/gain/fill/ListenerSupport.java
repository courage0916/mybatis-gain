package com.gain.fill;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ListenerSupport {
    private static final ListenerSupport listenerSupport;

    static {
        try {
            listenerSupport = new ListenerSupport();
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final List<FillListener> listeners = new ArrayList<>();

    private ListenerSupport() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //扫描 FillListener 接口下的实现类
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(Object.class));
        Set<BeanDefinition> candidates = scanner.findCandidateComponents("com.gain.fill");

        for (BeanDefinition beanDefinition : candidates) {
            Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
            if (FillListener.class.isAssignableFrom(clazz)) {
                listeners.add((FillListener) clazz.getDeclaredConstructor().newInstance());
            }
        }
    }

    public static ListenerSupport getInstance() {
        return listenerSupport;
    }

    public void ListenerSupport(FillListener listener) {
        listeners.add(listener);
    }

    public void fillObject(SqlCommandType type, Object obj) throws IllegalAccessException {
        for (FillListener listener : listeners) {
            listener.fillObject(type, obj);
        }
    }

    public String getUpdateSQL(SqlCommandType type, String sql, Object obj) {
        for (FillListener listener : listeners) {
            sql = listener.getUpdateSQL(type, sql, obj);
        }
        return sql;
    }

    public List<ParameterMapping> getUpdateParameter(List<ParameterMapping> parameterMappings, MappedStatement ms, Object obj) {
        for (FillListener listener : listeners) {
            parameterMappings = listener.getUpdateParameter(parameterMappings, ms, obj);
        }
        return parameterMappings;
    }
}

