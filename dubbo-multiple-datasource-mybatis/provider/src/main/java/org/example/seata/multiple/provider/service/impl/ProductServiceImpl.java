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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.example.seata.multiple.common.service.IProductService;
import org.apache.dubbo.config.annotation.DubboService;

import com.baomidou.dynamic.datasource.annotation.DS;

import org.example.seata.multiple.common.entity.Product;
import org.example.seata.multiple.provider.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DubboService(version = "1.0.0", interfaceClass = IProductService.class)
@DS(value = "master_3")
@Transactional
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;

    @Override
    public Product getById(int id) {
        return productMapper.getByIdForUpdate(id);
    }

    @Override
    public boolean updateById(Product product) {
        return productMapper.updateProductById(product) > 0 ? true : false;
    }
}
