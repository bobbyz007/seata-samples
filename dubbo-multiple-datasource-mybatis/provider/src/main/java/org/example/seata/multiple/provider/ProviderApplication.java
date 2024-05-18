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
package org.example.seata.multiple.provider;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 注意：如果在application.yml中没有配置dubbo scan，则需要添加注解@EnableDubbo(scanBasePackages = "io.seata.samples.service")
 * scan:
 *     base-packages: io.seata.samples.service
 */
@SpringBootApplication/*(exclude = {DruidDataSourceAutoConfigure.class})*/
@EnableDubbo(scanBasePackages = "org.example.seata.multiple.provider")
public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ProviderApplication.class);
        app.run(args);
    }

}
