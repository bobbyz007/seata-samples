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
package org.example.account.controller;

import java.math.BigDecimal;

import org.apache.seata.core.context.RootContext;
import org.example.account.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * http://localhost:8083/?userId=1001&orderMoney=100
 */
@RestController
@RequestMapping("/api/account")
public class AccountController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
    @Autowired
    AccountService accountService;

    @GetMapping(value = "/debit")
    public void debit(@RequestParam(name = "userId") String userId, @RequestParam(name = "orderMoney") BigDecimal orderMoney) {
        LOGGER.info("debit money for XID: " + RootContext.getXID());
        accountService.debit(userId, orderMoney);
    }
}
