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
package org.example.seata.multiple.provider.service.impl;

import org.example.seata.multiple.common.service.IAccountService;
import org.apache.dubbo.config.annotation.DubboService;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.example.seata.multiple.common.entity.Account;
import org.example.seata.multiple.provider.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService(version = "1.0.0", interfaceClass = IAccountService.class)
@DS(value = "master_1")
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Account getById(int id) {
        return accountMapper.selectByUserId(id);
    }

    @Override
    public boolean updateById(Account account) {
        return accountMapper.updateAccountById(account) > 0 ? true : false;
    }
}
