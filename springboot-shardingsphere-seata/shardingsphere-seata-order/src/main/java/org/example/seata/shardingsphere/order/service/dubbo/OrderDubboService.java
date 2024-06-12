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
package org.example.seata.shardingsphere.order.service.dubbo;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.seata.shardingsphere.common.entity.OrderEntity;
import org.example.seata.shardingsphere.common.service.IOrderService;
import org.example.seata.shardingsphere.order.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService(version = "1.0.0", interfaceClass = IOrderService.class)
public class OrderDubboService implements IOrderService {
    @Autowired
    private OrderServiceImpl orderService;

    @Override
    public void insertOrder(OrderEntity orderEntity) {
        orderService.insertOrder(orderEntity);
    }
}
