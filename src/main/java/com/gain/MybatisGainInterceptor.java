package com.gain;


import com.gain.fill.FillService;
import com.gain.log.LogService;
import com.gain.safe.AllowUpdateTables;
import com.gain.safe.SafeService;
import jakarta.annotation.Resource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * Mybatis Gain（增益） 拦截器
 * 主要实现一些需要额外的功能，如填充字段、新增 Mybatis Sql 日志
 */
@Component
@Intercepts({@Signature(
        type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class})})
public class MybatisGainInterceptor implements Interceptor {
    @Resource
    LogService logService;

    @Resource
    SafeService safeService;

    // 关于该插件的配置
    private final AllowUpdateTables allowUpdateTables;

    public MybatisGainInterceptor(AllowUpdateTables allowUpdateTables) {
        this.allowUpdateTables = allowUpdateTables;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        safeService.check(((MappedStatement) invocation.getArgs()[0]).getSqlCommandType(), (((MappedStatement) invocation.getArgs()[0]).getBoundSql(invocation.getArgs()[1])).getSql());

        new FillService().fill(invocation);

        logService.add("", "");

        return invocation.proceed();
    }
}