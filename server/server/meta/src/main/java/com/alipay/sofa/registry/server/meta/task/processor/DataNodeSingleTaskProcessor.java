/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.registry.server.meta.task.processor;

import com.alipay.sofa.registry.log.Logger;
import com.alipay.sofa.registry.log.LoggerFactory;
import com.alipay.sofa.registry.server.meta.task.MetaServerTask;
import com.alipay.sofa.registry.task.Retryable;
import com.alipay.sofa.registry.task.batcher.TaskProcessor;

import java.util.List;

/**
 *
 * @author shangyu.wh
 * @version $Id: DataNodeSingleTaskProcessor.java, v 0.1 2017-12-11 19:35 shangyu.wh Exp $
 */
public class DataNodeSingleTaskProcessor implements TaskProcessor<MetaServerTask> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataNodeSingleTaskProcessor.class,
                                           "[Task]");

    @Override
    public ProcessingResult process(MetaServerTask task) {
        try {
            LOGGER.info("execute " + task);
            task.execute();
            LOGGER.info("end " + task);
            return ProcessingResult.Success;
        } catch (Throwable throwable) {
            LOGGER.error("Data node SingleTask Process error!", throwable);
            if (task instanceof Retryable) {
                Retryable retryAbleTask = (Retryable) task;
                if (retryAbleTask.checkRetryTimes()) {
                    return ProcessingResult.TransientError;
                }
            }
            return ProcessingResult.PermanentError;
        }
    }

    @Override
    public ProcessingResult process(List<MetaServerTask> tasks) {
        return null;
    }
}