package com.gain.fill;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.List;

import static com.gain.fill.FillStatus.StatusFile.STATUS;

@Service
public class FillStatus extends FillListener {
    @Override
    public void fillObject(SqlCommandType type, Object obj) throws IllegalAccessException {

        if (SqlCommandType.INSERT.equals(type) && isExist(obj, STATUS.getPoFile())) {
            Field[] declaredFields = obj.getClass().getDeclaredFields();

            for (Field field : declaredFields) {

                field.setAccessible(true);

                if (STATUS.getPoFile().equalsIgnoreCase(field.getName()) && ObjectUtils.isEmpty(field.get(obj))) {

                    if (!"java.lang.Boolean".equals(field.getType().getName())) {

                        throw new RuntimeException("status type should is java.lang.Boolean");

                    }

                    setValue(obj, field, true);

                    return;
                }

            }
        }

    }


    @Override
    public String getUpdateSQL(SqlCommandType type, String sql, Object obj) {

        if (SqlCommandType.INSERT.equals(type) && isExist(obj, STATUS.getPoFile())) {

            String[] contexts = sql.split("\\)");
            return contexts[0] + "," + STATUS.getDbFile() + ")" + contexts[1] + ",?)";
        }

        return sql;
    }

    @Override
    public List<ParameterMapping> getUpdateParameter(List<ParameterMapping> parameterMappings, MappedStatement ms, Object obj) {

        if (SqlCommandType.INSERT.equals(ms.getSqlCommandType()) && isExist(obj, STATUS.getPoFile())) {
            parameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), "row." + STATUS.getPoFile(), Boolean.class).build());
        }

        return parameterMappings;
    }

    enum StatusFile {

        STATUS("状态", "status", "t_status");

        private final String remark;
        private final String poFile;
        private final String dbFile;

        StatusFile(String remark, String poFile, String dbFile) {
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
