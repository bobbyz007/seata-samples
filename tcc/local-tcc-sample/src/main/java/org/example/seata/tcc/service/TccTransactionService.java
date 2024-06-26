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
package org.example.seata.tcc.service;

import java.util.Map;

import org.apache.seata.core.context.RootContext;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.example.seata.tcc.action.TccActionOne;
import org.example.seata.tcc.action.TccActionTwo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Tcc transaction service.
 *
 * @author zhangsen
 */
@Service("tccTransactionService")
public class TccTransactionService {

    @Autowired
    private TccActionOne tccActionOne;

    @Autowired
    private TccActionTwo tccActionTwo;

    /**
     * 发起分布式事务
     *
     * @return string string
     */
    @GlobalTransactional
    public String doTransactionCommit() {
        //第一个TCC 事务参与者
        boolean result = tccActionOne.prepare(null, 1);
        if (!result) {
            throw new RuntimeException("TccActionOne failed.");
        }
        result = tccActionTwo.prepare(null, "two");
        if (!result) {
            throw new RuntimeException("TccActionTwo failed.");
        }
        return RootContext.getXID();
    }

    /**
     * Do transaction rollback string.
     *
     * @param map the map
     * @return the string
     */
    @GlobalTransactional
    public String doTransactionRollback(Map map) {
        //第一个TCC 事务参与者
        boolean result = tccActionOne.prepare(null, 1);
        if (!result) {
            throw new RuntimeException("TccActionOne failed.");
        }
        result = tccActionTwo.prepare(null, "two");
        if (!result) {
            throw new RuntimeException("TccActionTwo failed.");
        }
        map.put("xid", RootContext.getXID());

        // 模拟抛出异常，则需要回滚对应的prepare
        throw new RuntimeException("transacton rollback");
    }

    /**
     * Sets tcc action one.
     *
     * @param tccActionOne the tcc action one
     */
    public void setTccActionOne(TccActionOne tccActionOne) {
        this.tccActionOne = tccActionOne;
    }

    /**
     * Sets tcc action two.
     *
     * @param tccActionTwo the tcc action two
     */
    public void setTccActionTwo(TccActionTwo tccActionTwo) {
        this.tccActionTwo = tccActionTwo;
    }
}
