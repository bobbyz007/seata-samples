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
package org.example.stock.controller;

import java.sql.SQLException;

import org.apache.seata.core.context.RootContext;
import org.example.stock.persistence.Stock;
import org.example.stock.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/stock")
@RestController
public class StockController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);
    @Autowired
    StockService stockService;

    @GetMapping(value = "/deduct")
    public void deduct(@RequestParam(name = "commodityCode") String commodityCode, @RequestParam(name = "count") Integer count) throws SQLException {
        LOGGER.info("deduct stock for XID: " + RootContext.getXID());
        stockService.deduct(commodityCode, count);
    }

    @GetMapping(value = "/get/{id}")
    public Stock getById(@PathVariable("id") Integer id) {
        return stockService.get(id);
    }

    @GetMapping(value = "/batch/update")
    public void batchUpdateCond() {
        try {
            stockService.batchUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/batch/delete")
    public void batchDeleteCond() {
        try {
            stockService.batchDelete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
