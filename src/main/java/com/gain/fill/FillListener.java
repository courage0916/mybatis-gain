package com.gain.fill;

import com.gain.util.DatabaseService;
import com.gain.util.SpringContextUtil;
import jakarta.annotation.Resource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;


public abstract class FillListener {

    protected void setValue(Object obj, Field field, Object value) throws IllegalAccessException {
        field.set(obj, value);
    }

    protected Boolean isExist(Object obj, String fileName) {
        if (obj == null){
            return false;
        }

        for (Field field : obj.getClass().getDeclaredFields()) {

            field.setAccessible(true);

            if (fileName.equalsIgnoreCase(field.getName())) {

                return true;

            }
        }

        return false;
    }

    protected Boolean isExist(String tableName, String fileName){
        DatabaseService databaseService =  (DatabaseService) SpringContextUtil.getBean("databaseService");
        return databaseService.isExist(tableName,fileName);
    }

    public abstract void fillObject(SqlCommandType type, Object obj) throws IllegalAccessException;

    public abstract String getUpdateSQL(SqlCommandType type, String sql, Object obj);

    public abstract List<ParameterMapping> getUpdateParameter(List<ParameterMapping> parameterMappings, MappedStatement ms, Object obj);
}
