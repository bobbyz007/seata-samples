/*
 *  Copyright 1999-2021 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.example.seata.saga.starter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.seata.saga.engine.AsyncCallback;
import org.apache.seata.saga.engine.StateMachineEngine;
import org.apache.seata.saga.proctrl.ProcessContext;
import org.apache.seata.saga.statelang.domain.ExecutionStatus;
import org.apache.seata.saga.statelang.domain.StateMachineInstance;
import org.apache.seata.spring.boot.autoconfigure.SeataAutoConfiguration;
import org.example.seata.saga.ApplicationKeeper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

/**
 * 对应的action日志打印使用warn，方便查看日志。
 *
 * 业务回滚： mockReduceBalanceFail参数为true，在BalanceActionImpl的reduce方法中读取，如果是true则抛出异常模拟业务失败。
 *          对应的compensate方法执行。
 * 业务成功： mockReduceBalanceFail参数不设置或为false。
 *
 */
@SpringBootApplication(scanBasePackages = "org.example.seata.saga", exclude = {SeataAutoConfiguration.class})
public class LocalSagaTransactionStarter {

    public static void main(String[] args) {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(LocalSagaTransactionStarter.class, args);

        StateMachineEngine stateMachineEngine = (StateMachineEngine)applicationContext.getBean("stateMachineEngine");

        transactionCommittedDemo(stateMachineEngine);

        transactionCompensatedDemo(stateMachineEngine);

        new ApplicationKeeper(applicationContext).keep();
    }

    private static void transactionCommittedDemo(StateMachineEngine stateMachineEngine) {

        Map<String, Object> startParams = new HashMap<>(3);
        String businessKey = String.valueOf(System.currentTimeMillis());
        startParams.put("businessKey", businessKey);
        startParams.put("count", 10);
        startParams.put("amount", new BigDecimal("100"));

        //sync test
        StateMachineInstance inst = stateMachineEngine.startWithBusinessKey("reduceInventoryAndBalance", null,
            businessKey, startParams);

        Assert.isTrue(ExecutionStatus.SU.equals(inst.getStatus()),
            "saga transaction execute failed. XID: " + inst.getId());
        System.out.println("saga transaction commit succeed. XID: " + inst.getId());

        inst = stateMachineEngine.getStateMachineConfig().getStateLogStore().getStateMachineInstanceByBusinessKey(
            businessKey, null);
        Assert.isTrue(ExecutionStatus.SU.equals(inst.getStatus()),
            "saga transaction execute failed. XID: " + inst.getId());

        //async test
        businessKey = String.valueOf(System.currentTimeMillis());
        inst = stateMachineEngine.startWithBusinessKeyAsync("reduceInventoryAndBalance", null, businessKey, startParams,
            CALL_BACK);

        waittingForFinish(inst);

        Assert.isTrue(ExecutionStatus.SU.equals(inst.getStatus()),
            "saga transaction execute failed. XID: " + inst.getId());
        System.out.println("saga transaction commit succeed. XID: " + inst.getId());
    }

    private static void transactionCompensatedDemo(StateMachineEngine stateMachineEngine) {
        Map<String, Object> startParams = new HashMap<>(4);
        String businessKey = String.valueOf(System.currentTimeMillis());
        startParams.put("businessKey", businessKey);
        startParams.put("count", 10);
        startParams.put("amount", new BigDecimal("100"));
        // 模拟业务异常，BalanceActionImpl会根据该参数值抛出异常
        startParams.put("mockReduceBalanceFail", "true");

        //sync test
        StateMachineInstance inst = stateMachineEngine.startWithBusinessKey("reduceInventoryAndBalance", null,
            businessKey, startParams);

        //async test
        businessKey = String.valueOf(System.currentTimeMillis());
        inst = stateMachineEngine.startWithBusinessKeyAsync("reduceInventoryAndBalance", null, businessKey, startParams,
            CALL_BACK);

        waittingForFinish(inst);

        Assert.isTrue(ExecutionStatus.UN.equals(inst.getStatus()),
            "saga transaction compensate failed. XID: " + inst.getId());
        System.out.println("saga transaction compensate succeed. XID: " + inst.getId());
    }

    private static volatile Object lock = new Object();
    private static AsyncCallback CALL_BACK = new AsyncCallback() {
        @Override
        public void onFinished(ProcessContext context, StateMachineInstance stateMachineInstance) {
            synchronized (lock) {
                lock.notifyAll();
            }
        }

        @Override
        public void onError(ProcessContext context, StateMachineInstance stateMachineInstance, Exception exp) {
            synchronized (lock) {
                lock.notifyAll();
            }
        }
    };

    private static void waittingForFinish(StateMachineInstance inst) {
        synchronized (lock) {
            if (ExecutionStatus.RU.equals(inst.getStatus())) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
