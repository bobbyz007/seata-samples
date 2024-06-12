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
package org.example.seata.shardingsphere.business.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.seata.core.context.RootContext;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.example.seata.shardingsphere.business.service.IBusinessService;
import org.example.seata.shardingsphere.common.entity.OrderEntity;
import org.example.seata.shardingsphere.common.entity.OrderItemEntity;
import org.example.seata.shardingsphere.common.service.IOrderItemService;
import org.example.seata.shardingsphere.common.service.IOrderService;
import org.springframework.stereotype.Service;

@Service
public class BusinessServiceImpl implements IBusinessService {
    @DubboReference(version = "1.0.0", timeout = 60000, check = false)
    IOrderService orderService;
    @DubboReference(version = "1.0.0", timeout = 60000, check = false)
    IOrderItemService orderItemService;

    @Override
    @GlobalTransactional(name = "dubbo-purchase")
    public void purchase() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(123);
        orderEntity.setStatus("seata");
        orderEntity.setUserId(123);
        orderService.insertOrder(orderEntity);

        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setOrderItemId(124);
        orderItemEntity.setOrderId(124);
        orderItemEntity.setStatus("seata");
        orderItemEntity.setUserId(124);
        orderItemService.insertOrderItem(orderItemEntity);

        System.out.println("XID:" + RootContext.getXID());

        //throw new RuntimeException("回滚测试");
    }
}
