#!/bin/bash

# OAuth2 JWK端点测试脚本
# 用于验证网关配置是否正确

echo "🔍 OAuth2 JWK端点配置测试"
echo "========================================"

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 测试函数
test_endpoint() {
    local url=$1
    local description=$2
    local expected_status=${3:-200}
    
    echo -e "${YELLOW}测试: ${description}${NC}"
    echo "URL: $url"
    
    response=$(curl -s -w "HTTPSTATUS:%{http_code}" "$url" 2>/dev/null)
    http_code=$(echo $response | grep -o "HTTPSTATUS:[0-9]*" | cut -d: -f2)
    body=$(echo $response | sed -E 's/HTTPSTATUS:[0-9]*$//')
    
    if [ "$http_code" -eq "$expected_status" ]; then
        echo -e "${GREEN}✅ 成功: HTTP $http_code${NC}"
        if [ "$expected_status" -eq 200 ] && [ -n "$body" ]; then
            echo "响应内容："
            echo "$body" | jq . 2>/dev/null || echo "$body"
        fi
    else
        echo -e "${RED}❌ 失败: HTTP $http_code (期望: $expected_status)${NC}"
        if [ -n "$body" ]; then
            echo "错误内容："
            echo "$body"
        fi
    fi
    echo ""
}

echo "1. 测试通过网关访问JWK端点 (应该成功 - 在白名单中)"
test_endpoint "http://localhost:8080/oauth2/jwks" "网关JWK端点"

echo "2. 测试直接访问Auth Service的JWK端点"
test_endpoint "http://localhost:8081/oauth2/jwks" "Auth Service直接访问"

echo "3. 测试通过网关访问Token端点 (应该被拦截 - 需要认证)"
test_endpoint "http://localhost:8080/oauth2/token" "网关Token端点" 401

echo "4. 测试Core Service健康检查"
test_endpoint "http://localhost:8082/actuator/health" "Core Service健康检查"

echo "5. 测试Gateway健康检查"
test_endpoint "http://localhost:8080/actuator/health" "Gateway健康检查"

echo "========================================"
echo "🎯 测试完成！"
echo ""
echo "📋 配置摘要："
echo "- JWK端点已添加到网关白名单: /oauth2/jwks"
echo "- Core Service配置为通过网关访问JWK: http://localhost:8080/oauth2/jwks"
echo "- 网关路由配置了OAuth2端点转发到Auth Service"
echo ""
echo "🔧 下一步："
echo "1. 启动所有服务测试集成"
echo "2. 验证服务间调用是否正常"
echo "3. 检查JWT验证流程"
