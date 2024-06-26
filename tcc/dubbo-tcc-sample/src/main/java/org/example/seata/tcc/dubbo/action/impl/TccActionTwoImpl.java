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
package org.example.seata.tcc.dubbo.action.impl;

import java.util.List;

import org.apache.seata.rm.tcc.api.BusinessActionContext;
import org.example.seata.tcc.dubbo.action.ResultHolder;
import org.example.seata.tcc.dubbo.action.TccActionTwo;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * The type Tcc action two.
 *
 * @author zhangsen
 */
@DubboService(version = "1.0.0", interfaceClass = TccActionTwo.class)
public class TccActionTwoImpl implements TccActionTwo {

    @Override
    public boolean prepare(BusinessActionContext actionContext, String b, List list) {
        String xid = actionContext.getXid();
        System.out.println("TccActionTwo prepare, xid:" + xid + ", b:" + b + ", c:" + list.get(1));
        return true;
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        System.out.println(
            "TccActionTwo commit, xid:" + xid + ", b:" + actionContext.getActionContext("b") + ", c:" + actionContext
                .getActionContext("c"));
        ResultHolder.setActionTwoResult(xid, "T");
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        System.out.println(
            "TccActionTwo rollback, xid:" + xid + ", b:" + actionContext.getActionContext("b") + ", c:" + actionContext
                .getActionContext("c"));
        ResultHolder.setActionTwoResult(xid, "R");
        return true;
    }

}
