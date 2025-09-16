#!/bin/bash

# IAM平台启动脚本
# 用于快速启动所有微服务

echo "=========================================="
echo "          IAM平台启动脚本"
echo "=========================================="

# 检查Java环境
if ! command -v java &> /dev/null; then
    echo "错误: 未找到Java环境，请先安装JDK 17+"
    exit 1
fi

# 检查Maven环境
if ! command -v mvn &> /dev/null; then
    echo "错误: 未找到Maven环境，请先安装Maven"
    exit 1
fi

# 设置环境变量
export JAVA_HOME=${JAVA_HOME:-$(dirname $(dirname $(readlink -f $(which java))))}
export MAVEN_HOME=${MAVEN_HOME:-$(dirname $(dirname $(readlink -f $(which mvn))))}

echo "Java版本: $(java -version 2>&1 | head -n 1)"
echo "Maven版本: $(mvn -version | head -n 1)"
echo ""

# 启动基础服务
echo "1. 启动基础服务..."
echo "请确保以下服务已启动："
echo "   - MySQL (端口: 3306)"
echo "   - Redis (端口: 6379)"
echo "   - Nacos (端口: 8848)"
echo "   - RocketMQ (端口: 9876)"
echo ""

read -p "基础服务是否已启动？(y/n): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "请先启动基础服务，然后重新运行此脚本"
    exit 1
fi

# 编译项目
echo "2. 编译项目..."
mvn clean compile -DskipTests

if [ $? -ne 0 ]; then
    echo "编译失败，请检查代码"
    exit 1
fi

echo "编译成功！"
echo ""

# 启动服务
echo "3. 启动微服务..."
echo "按以下顺序启动服务："
echo "   1. iam-gateway (网关服务)"
echo "   2. iam-auth-service (认证服务)"
echo "   3. iam-core-service (核心业务服务)"
echo "   4. iam-audit-service (审计服务)"
echo "   5. iam-system-service (系统服务)"
echo "   6. iam-frontend (前端服务)"
echo ""

# 启动网关
echo "启动网关服务..."
cd ../iam-gateway
nohup mvn spring-boot:run > logs/gateway.log 2>&1 &
GATEWAY_PID=$!
echo "网关服务已启动，PID: $GATEWAY_PID"
sleep 10

# 启动认证服务
echo "启动认证服务..."
cd ../services/iam-auth-service
nohup mvn spring-boot:run > logs/auth.log 2>&1 &
AUTH_PID=$!
echo "认证服务已启动，PID: $AUTH_PID"
sleep 5

# 启动核心业务服务
echo "启动核心业务服务..."
cd ../iam-core-service
nohup mvn spring-boot:run > logs/core.log 2>&1 &
CORE_PID=$!
echo "核心业务服务已启动，PID: $CORE_PID"
sleep 5

# 启动审计服务
echo "启动审计服务..."
cd ../iam-audit-service
nohup mvn spring-boot:run > logs/audit.log 2>&1 &
AUDIT_PID=$!
echo "审计服务已启动，PID: $AUDIT_PID"
sleep 5

# 启动系统服务
echo "启动系统服务..."
cd ../iam-system-service
nohup mvn spring-boot:run > logs/system.log 2>&1 &
SYSTEM_PID=$!
echo "系统服务已启动，PID: $SYSTEM_PID"
sleep 5

# 启动前端服务
echo "启动前端服务..."
cd ../../apps/iam-frontend
nohup mvn spring-boot:run > logs/frontend.log 2>&1 &
FRONTEND_PID=$!
echo "前端服务已启动，PID: $FRONTEND_PID"

echo ""
echo "=========================================="
echo "          IAM平台启动完成"
echo "=========================================="
echo "服务访问地址："
echo "  管理后台: http://localhost:8080"
echo "  API文档: http://localhost:8080/doc.html"
echo "  Nacos控制台: http://localhost:8848/nacos"
echo ""
echo "服务PID信息："
echo "  网关服务: $GATEWAY_PID"
echo "  认证服务: $AUTH_PID"
echo "  核心业务服务: $CORE_PID"
echo "  审计服务: $AUDIT_PID"
echo "  系统服务: $SYSTEM_PID"
echo "  前端服务: $FRONTEND_PID"
echo ""
echo "日志文件位置："
echo "  各服务日志: services/*/logs/*.log"
echo "  网关日志: iam-gateway/logs/gateway.log"
echo "  前端日志: apps/iam-frontend/logs/frontend.log"
echo ""
echo "停止服务命令："
echo "  kill $GATEWAY_PID $AUTH_PID $CORE_PID $AUDIT_PID $SYSTEM_PID $FRONTEND_PID"
echo "=========================================="