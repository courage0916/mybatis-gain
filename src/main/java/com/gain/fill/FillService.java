package com.gain.fill;

import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.Invocation;
import org.mybatis.dynamic.sql.insert.render.DefaultInsertStatementProvider;
import org.mybatis.dynamic.sql.update.render.DefaultUpdateStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class FillService {

    private final ListenerSupport listenerSupport;

    public FillService() {
        listenerSupport = ListenerSupport.getInstance();
    }

    public void fill(Invocation invocation) throws IllegalAccessException {

        final Object[] args = invocation.getArgs();

        MappedStatement ms = (MappedStatement) args[0];

        BoundSql boundSql = ms.getBoundSql(args[1]);

        MappedStatement newMs = null;

        if (SqlCommandType.INSERT.equals(ms.getSqlCommandType())) {

            Object obj = ((DefaultInsertStatementProvider<?>) args[1]).getRow();

            listenerSupport.fillObject(ms.getSqlCommandType(), obj);
            BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), listenerSupport.getUpdateSQL(ms.getSqlCommandType(), boundSql.getSql(), obj), listenerSupport.getUpdateParameter(boundSql.getParameterMappings(), ms, obj), boundSql.getParameterObject());

            newMs = newMappedStatement(ms, newBoundSql);
            for (ParameterMapping mapping : boundSql.getParameterMappings()) {
                String prop = mapping.getProperty();
                if (boundSql.hasAdditionalParameter(prop)) {
                    newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
                }
            }

            args[0] = newMs;
        }



        if (SqlCommandType.UPDATE.equals(ms.getSqlCommandType())) {
           /* Object obj = ((DefaultUpdateStatementProvider) args[1]).getParameters();
            System.out.println(listenerSupport.getUpdateSQL(ms.getSqlCommandType(), boundSql.getSql(), null));
            BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), listenerSupport.getUpdateSQL(ms.getSqlCommandType(), boundSql.getSql(), null), listenerSupport.getUpdateParameter(boundSql.getParameterMappings(), ms, null), boundSql.getParameterObject());

            System.out.println(newBoundSql.getSql());
            System.out.println(newBoundSql.getParameterMappings().size());
            newMs = newMappedStatement(ms, newBoundSql);
            for (ParameterMapping mapping : newBoundSql.getParameterMappings()) {
                String prop = mapping.getProperty();
                if (boundSql.hasAdditionalParameter(prop)) {
                    newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
                }
            }

            DefaultUpdateStatementProvider.Builder builder1 = new DefaultUpdateStatementProvider.Builder();

            Map<String, Object> stringObjectMap = ((DefaultUpdateStatementProvider) args[1]).getParameters();
            stringObjectMap.put("p12",stringObjectMap.get("p11"));
            stringObjectMap.put("p11",new Date());
            builder1.withParameters(stringObjectMap);
            builder1.withUpdateStatement(
                    "update t_power set t_name = #{parameters.p1,jdbcType=VARCHAR}, t_code = #{parameters.p2,jdbcType=VARCHAR}, t_type = #{parameters.p3,jdbcType=VARCHAR}, t_parent_id = #{parameters.p4,jdbcType=BIGINT}, t_position = #{parameters.p5,jdbcType=INTEGER}, t_remark = #{parameters.p6,jdbcType=VARCHAR}, t_url = #{parameters.p7,jdbcType=VARCHAR}, t_icon = #{parameters.p8,jdbcType=VARCHAR}, t_sys_code = #{parameters.p9,jdbcType=VARCHAR}, t_company_code = #{parameters.p10,jdbcType=VARCHAR}, " +
                            "t_update_time = #{parameters.p11,jdbcType=TIMESTAMP} where t_id = #{parameters.p12,jdbcType=BIGINT}");

            DefaultUpdateStatementProvider builder = builder1.build();
            System.out.println(builder.getUpdateStatement());
            args[1] = builder;*/
        }



    }
    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource sqlSource) {

        MappedStatement.Builder builder = new
                MappedStatement.Builder(ms.getConfiguration(), ms.getId(), sqlSource , ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }
    private MappedStatement newMappedStatement(MappedStatement ms, BoundSql newBoundSql) {
        MappedStatement.Builder builder = new
                MappedStatement.Builder(ms.getConfiguration(), ms.getId(), new BoundSqlSource(newBoundSql), ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }


    class BoundSqlSource implements SqlSource {
        private final BoundSql boundSql;

        public BoundSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

}
