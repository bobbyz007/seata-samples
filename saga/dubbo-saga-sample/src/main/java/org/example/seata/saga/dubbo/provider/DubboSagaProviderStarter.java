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
package org.example.seata.saga.dubbo.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.seata.spring.boot.autoconfigure.SeataAutoConfiguration;
import org.example.seata.saga.dubbo.ApplicationKeeper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 运行时配置添加main args参数：--spring.profiles.active=provider
 */
@SpringBootApplication(scanBasePackages = "org.example.seata.saga.dubbo.provider", exclude = {SeataAutoConfiguration.class})
@EnableDubbo(scanBasePackages = "org.example.seata.saga.dubbo.provider")
public class DubboSagaProviderStarter {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(DubboSagaProviderStarter.class, args);

        new ApplicationKeeper(applicationContext).keep();
    }
}
