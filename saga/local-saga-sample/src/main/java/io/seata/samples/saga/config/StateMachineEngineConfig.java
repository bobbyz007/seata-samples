package io.seata.samples.saga.config;

import io.seata.saga.engine.config.DbStateMachineConfig;
import io.seata.saga.engine.impl.ProcessCtrlStateMachineEngine;
import io.seata.saga.rm.StateMachineEngineHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.concurrent.ThreadPoolExecutor;

/***
 * @author : Rain
 * @date : 2021/7/23 11:38 AM
 */
@Configuration
public class StateMachineEngineConfig {
    @Autowired
    private DataSource dataSource;

    @Value("${seata.application-id}")
    private String applicationId;

    @Value("${seata.tx-service-group}")
    private String txServiceGroup;

    @Bean
    public DbStateMachineConfig dbStateMachineConfig() {
        DbStateMachineConfig stateMachineConfig = new DbStateMachineConfig();
        stateMachineConfig.setDataSource(dataSource);
        stateMachineConfig.setResources(new String[]{"statelang/*.json"});
        stateMachineConfig.setEnableAsync(true);
        stateMachineConfig.setThreadPoolExecutor(threadExecutor());

        stateMachineConfig.setApplicationId(applicationId);
        stateMachineConfig.setTxServiceGroup(txServiceGroup);
        return stateMachineConfig;
    }

    @Bean
    public ProcessCtrlStateMachineEngine stateMachineEngine() {
        ProcessCtrlStateMachineEngine processCtrlStateMachineEngine = new ProcessCtrlStateMachineEngine();
        processCtrlStateMachineEngine.setStateMachineConfig(dbStateMachineConfig());
        return processCtrlStateMachineEngine;
    }

    @Bean
    public StateMachineEngineHolder stateMachineEngineHolder() {
        StateMachineEngineHolder engineHolder = new StateMachineEngineHolder();
        engineHolder.setStateMachineEngine(stateMachineEngine());
        return engineHolder;
    }

    @Bean
    public ThreadPoolExecutor threadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //?????????????????????
        executor.setCorePoolSize(1);
        //?????????????????????
        executor.setMaxPoolSize(20);
        //??????????????????
        executor.setQueueCapacity(99999);
        //??????????????????????????????????????????
        executor.setThreadNamePrefix("SAGA_ASYNC_EXE_");

        // ????????????????????????pool????????????max size?????????????????????????????????
        // CALLER_RUNS??????????????????????????????????????????????????????????????????????????????
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //???????????????
        executor.initialize();
        return executor.getThreadPoolExecutor();
    }
}