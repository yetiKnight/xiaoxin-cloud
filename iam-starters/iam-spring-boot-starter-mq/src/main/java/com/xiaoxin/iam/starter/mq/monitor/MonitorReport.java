/*
 * Copyright 2024 xiaoxin. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xiaoxin.iam.starter.mq.monitor;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 监控报告
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@Builder
public class MonitorReport {

    /**
     * 总发送数量
     */
    private long totalSent;

    /**
     * 总消费数量
     */
    private long totalConsumed;

    /**
     * 总错误数量
     */
    private long totalError;

    /**
     * 主题发送统计
     */
    private Map<String, Long> topicSentStats;

    /**
     * 主题消费统计
     */
    private Map<String, Long> topicConsumedStats;

    /**
     * 主题错误统计
     */
    private Map<String, Long> topicErrorStats;

    /**
     * 计算成功率
     */
    public double getSuccessRate() {
        if (totalSent == 0) {
            return 0.0;
        }
        return (double) (totalSent - totalError) / totalSent * 100;
    }

    /**
     * 计算消费率
     */
    public double getConsumeRate() {
        if (totalSent == 0) {
            return 0.0;
        }
        return (double) totalConsumed / totalSent * 100;
    }
}
