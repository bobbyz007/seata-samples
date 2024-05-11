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
package org.example.seata.dubbo.business;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// 本工程不用加载数据源，排除掉DataSourceAutoConfiguration
@SpringBootApplication(scanBasePackages = "org.example.seata.dubbo.business", exclude={DataSourceAutoConfiguration.class})
@EnableDubbo(scanBasePackages = "org.example.seata.dubbo.business")
public class BusinessExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusinessExampleApplication.class, args);
    }
}

