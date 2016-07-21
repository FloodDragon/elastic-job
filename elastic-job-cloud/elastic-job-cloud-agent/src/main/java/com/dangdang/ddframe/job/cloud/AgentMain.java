/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.dangdang.ddframe.job.cloud;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.cloud.executor.TaskExecutor;
import com.dangdang.ddframe.job.util.json.GsonFactory;

import lombok.RequiredArgsConstructor;
import org.apache.mesos.MesosExecutorDriver;
import org.apache.mesos.Protos.Status;

/**
 * 云作业启动执行器.
 *
 * @author caohao
 */
@RequiredArgsConstructor
public final class AgentMain {
    
    // CHECKSTYLE:OFF
    public static void main(final String[] args) {
    // CHECKSTYLE:ON
        MesosExecutorDriver driver = new MesosExecutorDriver(new TaskExecutor(GsonFactory.getGson().fromJson(args[0], ShardingContext.class)));
        System.exit(Status.DRIVER_STOPPED == driver.run() ? 0 : -1);
    }
}
