package com.gain.fill;

import com.utils.IdWorker;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.List;

public class FillId extends FillListener {
    @Override
    public void fillObject(SqlCommandType type, Object obj) throws IllegalAccessException {
        if (SqlCommandType.INSERT.equals(type)) {
            if (ObjectUtils.isEmpty(obj) || !isExist(obj,"id")) return;

            Field[] declaredFields = obj.getClass().getDeclaredFields();

            for (Field field : declaredFields) {
                field.setAccessible(true);
                if ("id".equalsIgnoreCase(field.getName()) && ObjectUtils.isEmpty(field.get(obj))) {
                    if (!"java.lang.Long".equals(field.getType().getName())) {
                        throw new RuntimeException("id type should is java.lang.Long");
                    }
                    setValue(obj, field, new IdWorker(0, 1).nextId());
                    return;
                }

            }
        }
    }

    @Override
    public String getUpdateSQL(SqlCommandType type, String sql, Object obj) {

        if (SqlCommandType.INSERT.equals(type)) {
            if (!isExist(obj, "id")) return sql;

            String[] contexts = sql.split("\\)");

            return contexts[0] + ",t_id )" + contexts[1] + ",?)";
        }

        return sql;
    }

    @Override
    public List<ParameterMapping> getUpdateParameter(List<ParameterMapping> parameterMappings, MappedStatement ms, Object obj) {
        if (SqlCommandType.INSERT.equals(ms.getSqlCommandType())) {
            if (!isExist(obj, "id")) return parameterMappings;
            parameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), "row.id", long.class).build());
        }
        return parameterMappings;
    }


}
