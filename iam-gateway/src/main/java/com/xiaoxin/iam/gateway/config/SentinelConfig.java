package com.xiaoxin.iam.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import jakarta.annotation.PostConstruct;
import java.util.*;

/**
 * Sentinel网关限流熔断配置
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class SentinelConfig {

    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public SentinelConfig(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                         ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    /**
     * 注意：sentinelGatewayFilter由Spring Cloud Alibaba自动配置提供
     * 不需要手动创建，避免bean重复定义冲突
     */

    /**
     * Sentinel网关异常处理器
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        log.info("配置Sentinel网关异常处理器");
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    /**
     * 初始化Sentinel网关规则
     */
    // @PostConstruct  // 暂时禁用自动初始化
    public void initGatewayRules() {
        log.info("Sentinel网关规则初始化已暂时禁用");
        
        // try {
        //     // 初始化API定义
        //     initApiDefinitions();
        //     
        //     // 初始化限流规则
        //     initFlowRules();
        //     
        //     // 初始化自定义限流处理器
        //     initBlockRequestHandler();
        //     
        //     log.info("Sentinel网关规则初始化完成");
        // } catch (Exception e) {
        //     log.error("Sentinel网关规则初始化失败: {}", e.getMessage(), e);
        //     // 不抛出异常，允许服务启动
        // }
    }

    /**
     * 初始化API定义
     */
    private void initApiDefinitions() {
        Set<ApiDefinition> definitions = new HashSet<>();

        // 认证服务API定义
        ApiDefinition authApi = new ApiDefinition("auth-api")
            .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                add(new ApiPathPredicateItem().setPattern("/api/v1/auth/**")
                    .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
            }});
        definitions.add(authApi);

        // 核心服务API定义
        ApiDefinition coreApi = new ApiDefinition("core-api")
            .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                add(new ApiPathPredicateItem().setPattern("/api/v1/core/**")
                    .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
            }});
        definitions.add(coreApi);

        // 系统服务API定义
        ApiDefinition systemApi = new ApiDefinition("system-api")
            .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                add(new ApiPathPredicateItem().setPattern("/api/v1/system/**")
                    .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
            }});
        definitions.add(systemApi);

        // 审计服务API定义
        ApiDefinition auditApi = new ApiDefinition("audit-api")
            .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                add(new ApiPathPredicateItem().setPattern("/api/v1/audit/**")
                    .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
            }});
        definitions.add(auditApi);

        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
        log.info("已加载API定义，数量: {}", definitions.size());
    }

    /**
     * 初始化限流规则
     */
    private void initFlowRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();

        // 认证服务限流规则
        rules.add(createFlowRule("auth-api", 30, 60));
        rules.add(createFlowRule("iam-auth-service", 50, 100));

        // 核心服务限流规则
        rules.add(createFlowRule("core-api", 60, 120));
        rules.add(createFlowRule("iam-core-service", 80, 160));

        // 系统服务限流规则
        rules.add(createFlowRule("system-api", 40, 80));
        rules.add(createFlowRule("iam-system-service", 50, 100));

        // 审计服务限流规则
        rules.add(createFlowRule("audit-api", 20, 40));
        rules.add(createFlowRule("iam-audit-service", 30, 60));

        // 前端应用限流规则
        rules.add(createFlowRule("iam-frontend", 100, 200));

        try {
            GatewayRuleManager.loadRules(rules);
            log.info("已加载限流规则，数量: {}", rules.size());
        } catch (Exception e) {
            log.error("加载限流规则失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 创建限流规则
     */
    private GatewayFlowRule createFlowRule(String resource, int count, int burst) {
        GatewayFlowRule rule = new GatewayFlowRule(resource);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(count);
        rule.setBurst(burst);
        rule.setIntervalSec(1);
        return rule;
    }

    /**
     * 初始化自定义限流处理器
     */
    private void initBlockRequestHandler() {
        BlockRequestHandler blockRequestHandler = new BlockRequestHandler() {
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable ex) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 429);
                result.put("message", "请求过于频繁，请稍后再试");
                result.put("data", null);
                result.put("timestamp", System.currentTimeMillis());
                result.put("path", exchange.getRequest().getPath().value());

                return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(result));
            }
        };

        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
        log.info("已配置自定义限流处理器");
    }
}
