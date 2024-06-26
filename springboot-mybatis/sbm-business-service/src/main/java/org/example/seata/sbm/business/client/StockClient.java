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
package org.example.seata.sbm.business.client;

import org.apache.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StockClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockClient.class);

    @Autowired
    private RestTemplate restTemplate;

    public void deduct(String commodityCode, int orderCount) {
        LOGGER.info("deduct stock for xid: " + RootContext.getXID());
        String url = "http://127.0.0.1:8081/api/stock/deduct?commodityCode=" + commodityCode + "&count=" + orderCount;
        try {
            restTemplate.getForEntity(url, Void.class);
        } catch (Exception e) {
            throw new RuntimeException(String.format("deduct url %s ,error:",url),e);
        }
    }
}
