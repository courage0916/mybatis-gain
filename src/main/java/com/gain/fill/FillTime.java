/*
package com.gain.fill;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

public class FillTime extends FillListener {

    public void fillTime(SqlCommandType type, Object obj) throws IllegalAccessException {

        if (ObjectUtils.isEmpty(obj)) return;

        Field[] declaredFields = obj.getClass().getDeclaredFields();

        if (SqlCommandType.INSERT.equals(type)) {
            for (Field field : declaredFields) {

                field.setAccessible(true);

                if (TimeFileType.UPDATE.getPoFile().equals(field.getName()) && isExist("t_power","t_update_time")) {

                    if (!"java.util.Date".equals(field.getType().getName())) {

                        throw new RuntimeException("updateTime type should is java.util.Date");

                    }

                    setValue(obj, field, new Date());

                    if (SqlCommandType.INSERT.equals(type)) return;
                }

                if (SqlCommandType.INSERT.equals(type) && TimeFileType.INSERT.getPoFile().equals(field.getName()) && ObjectUtils.isEmpty(field.get(obj))) {

                    if (!"java.util.Date".equals(field.getType().getName())) {

                        throw new RuntimeException("createTime type should is java.util.Date");

                    }

                    setValue(obj, field, new Date());
                }

            }
        }



    }

    @Override
    public void fillObject(SqlCommandType type, Object obj) throws IllegalAccessException {

        if (ObjectUtils.isEmpty(obj)) return;

        fillTime(type, obj);

    }

    @Override
    public String getUpdateSQL(SqlCommandType type, String sql, Object obj) {





        if (SqlCommandType.INSERT.equals(type)) {
            if (!isExist(obj, "createTime") && !isExist(obj, "updateTime")) return sql;
            String[] contexts = sql.split("\\)");
            return contexts[0] + String.format(",%S,%S) %S,?,?)", TimeFileType.INSERT.getDbFile(), TimeFileType.UPDATE.getDbFile(), contexts[1]);
        }

        if (SqlCommandType.UPDATE.equals(type)) {
            if(isExist("t_power","t_update_time")){
                String[] contexts = sql.split("set");
                String[] wheres = contexts[1].split("where");
                return contexts[0] + String.format("set %s , %s= ? where %s", wheres[0].toLowerCase(), TimeFileType.UPDATE.getDbFile().toLowerCase(),wheres[1].toLowerCase()) ;
            }
        }

        return sql;
    }

    @Override
    public List<ParameterMapping> getUpdateParameter(List<ParameterMapping> parameterMappings, MappedStatement ms, Object obj) {
        if (SqlCommandType.INSERT.equals(ms.getSqlCommandType())) {
            if (!isExist(obj, "createTime") && !isExist(obj, "updateTime")) return parameterMappings;
            parameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), "row." + TimeFileType.INSERT.getPoFile(), Date.class).build());
            parameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), "row." + TimeFileType.UPDATE.getPoFile(), Date.class).build());
        }

        if (SqlCommandType.UPDATE.equals(ms.getSqlCommandType())) {
            if (!isExist("t_power","t_update_time")) return parameterMappings;

            parameterMappings.remove(parameterMappings.size()-1);

            // size = 11   "updateTime":"2023-10-23 15:41:17"
            ParameterMapping.Builder parameterMappingB = new ParameterMapping.Builder(ms.getConfiguration(), "parameters.p11", Object.class);
            JdbcType jdbcType = JdbcType.TIMESTAMP;
            parameterMappingB.jdbcType(jdbcType);
            parameterMappings.add(parameterMappingB.build());

            ParameterMapping.Builder parameterMappingC = new ParameterMapping.Builder(ms.getConfiguration(), "parameters.p12", Object.class);
            JdbcType jdbcType2 = JdbcType.BIGINT;
            parameterMappingC.jdbcType(jdbcType2);
            parameterMappings.add(parameterMappingC.build());
        }
        return parameterMappings;
    }


    enum TimeFileType {

        INSERT("创建时间", "createTime", "t_create_time"),

        UPDATE("修改时间", "updateTime", "t_update_time");

        private final String remark;
        private final String poFile;
        private final String dbFile;

        TimeFileType(String remark, String poFile, String dbFile) {
            this.remark = remark;
            this.poFile = poFile;
            this.dbFile = dbFile;
        }

        public String getRemark() {
            return remark;
        }

        public String getPoFile() {
            return poFile;
        }

        public String getDbFile() {
            return dbFile;
        }
    }


}
*/
