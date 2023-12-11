/*
package com.gain.fill;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.List;

public class FillUser extends FillListener {

    @Override
    public void fillObject(SqlCommandType type, Object obj) throws IllegalAccessException {

 */
/*       if (ObjectUtils.isEmpty(obj)) return;

        Field[] declaredFields = obj.getClass().getDeclaredFields();

        if (SqlCommandType.INSERT.equals(type) || SqlCommandType.UPDATE.equals(type)) {
            for (Field field : declaredFields) {

                field.setAccessible(true);

                if (UserFileType.UPDATE.getPoFile().equals(field.getName()) && ObjectUtils.isEmpty(field.get(obj))) {

                    if (!"java.lang.String".equals(field.getType().getName())) {

                        throw new RuntimeException("updateUser type should is java.lang.String");

                    }

                    setValue(obj, field, "admins");

                    if (SqlCommandType.INSERT.equals(type)) return;
                }

                if (SqlCommandType.INSERT.equals(type) && UserFileType.INSERT.getPoFile().equals(field.getName()) && ObjectUtils.isEmpty(field.get(obj))) {

                    if (!"java.lang.String".equals(field.getType().getName())) {

                        throw new RuntimeException("createUser type should is java.lang.String");

                    }

                    setValue(obj, field, "admins");
                }

            }
        }*//*


    }

    @Override
    public String getUpdateSQL(SqlCommandType type, String sql, Object obj) {
*/
/*        if (ObjectUtils.isEmpty(obj)) {
            return sql;
        }
        String[] contexts = sql.split("\\)");

        if (SqlCommandType.INSERT.equals(type)) {
            if (!isExist(obj, "createUser") && !isExist(obj, "updateUser")) return sql;
            return contexts[0] + String.format(",%S,%S) %S,?,?)", UserFileType.INSERT.getDbFile(), UserFileType.UPDATE.getDbFile(), contexts[1]);

        }

        if (SqlCommandType.UPDATE.equals(type)) {
            if (!isExist(obj, "updateUser")) return sql;
            return contexts[0] + String.format(",%S) %S,?)", UserFileType.UPDATE.getDbFile(), contexts[1]);
        }*//*


        return sql;
    }

    @Override
    public List<ParameterMapping> getUpdateParameter(List<ParameterMapping> parameterMappings, MappedStatement ms, Object obj) {

       */
/* if (SqlCommandType.INSERT.equals(ms.getSqlCommandType())) {
            if (!isExist(obj, "createUser") && !isExist(obj, "updateUser")) return parameterMappings;
            parameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), "row." + UserFileType.INSERT.poFile, String.class).build());
            parameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), "row." + UserFileType.UPDATE.poFile, String.class).build());
        }

        if (SqlCommandType.UPDATE.equals(ms.getSqlCommandType())) {
            if (!isExist(obj, "updateUser")) return parameterMappings;
            parameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), "row." + UserFileType.UPDATE.poFile, String.class).build());
        }*//*


        return parameterMappings;
    }

    enum UserFileType {

        INSERT("创建人", "createUser", "t_create_user"),

        UPDATE("修改人", "updateUser", "t_update_user");

        private final String remark;
        private final String poFile;
        private final String dbFile;

        UserFileType(String remark, String poFile, String dbFile) {
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
