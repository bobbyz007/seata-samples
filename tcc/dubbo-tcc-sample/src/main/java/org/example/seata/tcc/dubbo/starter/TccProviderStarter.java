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
package org.example.seata.tcc.dubbo.starter;

import org.example.seata.tcc.dubbo.ApplicationKeeper;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 因为provider和consumer在一个工程中，启动时记得修改application.yml的端口号 Server.port
 */
@SpringBootApplication(scanBasePackages = "io.seata.samples.tcc.dubbo")
@EnableDubbo(scanBasePackages = "io.seata.samples.tcc.dubbo.action")
public class TccProviderStarter{

    public static void main(String[] args) throws Exception {
        new TccProviderStarter().start0(args);
    }

    protected void start0(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TccProviderStarter.class, args);

        new ApplicationKeeper(applicationContext).keep();
    }
}
