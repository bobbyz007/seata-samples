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
package io.seata.samples.saga.consumer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import io.seata.saga.engine.AsyncCallback;
import io.seata.saga.engine.StateMachineEngine;
import io.seata.saga.proctrl.ProcessContext;
import io.seata.saga.statelang.domain.ExecutionStatus;
import io.seata.saga.statelang.domain.StateMachineInstance;
import io.seata.samples.saga.ApplicationKeeper;
import io.seata.samples.saga.action.BalanceAction;
import io.seata.samples.saga.action.InventoryAction;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

/**
 * 运行时配置添加main args参数：--spring.profiles.active=consumer
 */

@SpringBootApplication(scanBasePackages = {"io.seata.samples.saga.config", "io.seata.samples.saga.consumer"})
@EnableDubbo(scanBasePackages = "io.seata.samples.saga.consumer")
public class DubboSagaTransactionStarter {
    @DubboReference(id = "inventoryAction", version = "1.0.0", timeout = 60000, check = false)
    private InventoryAction inventoryAction;

    @DubboReference(id = "balanceAction", version = "1.0.0", timeout = 60000, check = false)
    private BalanceAction balanceAction;

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(DubboSagaTransactionStarter.class, args);

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
        startParams.put("mockReduceBalanceFail", "true");

        //sync test
        StateMachineInstance inst = stateMachineEngine.startWithBusinessKey("reduceInventoryAndBalance", null,
            businessKey, startParams);

        //async test
        businessKey = String.valueOf(System.currentTimeMillis());
        inst = stateMachineEngine.startWithBusinessKeyAsync("reduceInventoryAndBalance", null, businessKey, startParams,
            CALL_BACK);

        waittingForFinish(inst);

        Assert.isTrue(ExecutionStatus.SU.equals(inst.getCompensationStatus()),
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