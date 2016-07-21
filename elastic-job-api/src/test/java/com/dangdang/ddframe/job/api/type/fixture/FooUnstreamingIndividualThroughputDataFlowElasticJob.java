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

package com.dangdang.ddframe.job.api.type.fixture;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.job.dataflow.DataFlowType;
import com.dangdang.ddframe.job.api.type.dataflow.AbstractIndividualThroughputDataFlowElasticJob;
import com.dangdang.ddframe.job.exception.JobException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class FooUnstreamingIndividualThroughputDataFlowElasticJob extends AbstractIndividualThroughputDataFlowElasticJob<Object> {
    
    private final JobCaller jobCaller;
    
    @Override
    public List<Object> fetchData(final ShardingContext shardingContext) {
        return jobCaller.fetchData();
    }
    
    @Override
    public boolean processData(final ShardingContext shardingContext, final Object data) {
        return jobCaller.processData(data);
    }
    
    @Override
    public void handleJobExecutionException(final JobException jobException) {
    }
    
    @Override
    protected DataFlowType getDataFlowType() {
        return DataFlowType.THROUGHPUT;
    }
}
