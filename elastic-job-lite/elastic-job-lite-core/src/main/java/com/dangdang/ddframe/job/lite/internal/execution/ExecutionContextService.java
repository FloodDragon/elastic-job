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

package com.dangdang.ddframe.job.lite.internal.execution;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.lite.api.config.JobConfiguration;
import com.dangdang.ddframe.job.lite.internal.config.ConfigurationService;
import com.dangdang.ddframe.job.lite.internal.offset.OffsetService;
import com.dangdang.ddframe.job.lite.internal.storage.JobNodeStorage;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 作业运行时上下文服务.
 * 
 * @author zhangliang
 */
public class ExecutionContextService {
    
    private final JobConfiguration jobConfiguration;
    
    private final JobNodeStorage jobNodeStorage;
    
    private final ConfigurationService configService;
    
    private final OffsetService offsetService;
    
    public ExecutionContextService(final CoordinatorRegistryCenter coordinatorRegistryCenter, final JobConfiguration jobConfiguration) {
        this.jobConfiguration = jobConfiguration;
        jobNodeStorage = new JobNodeStorage(coordinatorRegistryCenter, jobConfiguration);
        configService = new ConfigurationService(coordinatorRegistryCenter, jobConfiguration);
        offsetService = new OffsetService(coordinatorRegistryCenter, jobConfiguration);
    }
    
    /**
     * 获取当前作业服务器分片上下文.
     * 
     * @param shardingItems 分片项
     * @return 分片上下文
     */
    public ShardingContext getJobShardingContext(final List<Integer> shardingItems) {
        removeRunningIfMonitorExecution(shardingItems);
        if (shardingItems.isEmpty()) {
            return new ShardingContext(jobConfiguration.getJobName(), configService.getShardingTotalCount(), configService.getJobParameter(), configService.getFetchDataCount(), 
                    Collections.<ShardingContext.ShardingItem>emptyList());
        }
        Map<Integer, String> shardingItemParameterMap = configService.getShardingItemParameters();
        Map<Integer, String>  offSetMap = offsetService.getOffsets(shardingItems);
        List<ShardingContext.ShardingItem> shardingItemList = new ArrayList<>(shardingItems.size());
        for (int each : shardingItems) {
            shardingItemList.add(new ShardingContext.ShardingItem(each, shardingItemParameterMap.get(each), offSetMap.get(each)));
        }
        return new ShardingContext(jobConfiguration.getJobName(), configService.getShardingTotalCount(), configService.getJobParameter(), configService.getFetchDataCount(), shardingItemList);
    }
    
    private void removeRunningIfMonitorExecution(final List<Integer> shardingItems) {
        if (!configService.isMonitorExecution()) {
            return;
        }
        List<Integer> runningShardingItems = new ArrayList<>(shardingItems.size());
        for (int each : shardingItems) {
            if (isRunning(each)) {
                runningShardingItems.add(each);
            }
        }
        shardingItems.removeAll(runningShardingItems);
    }
    
    private boolean isRunning(final int shardingItem) {
        return jobNodeStorage.isJobNodeExisted(ExecutionNode.getRunningNode(shardingItem));
    }
}
