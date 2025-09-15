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
echo "   3. iam-user-service (用户服务)"
echo "   4. iam-permission-service (权限服务)"
echo "   5. iam-organization-service (组织服务)"
echo "   6. iam-audit-service (审计服务)"
echo "   7. iam-notification-service (通知服务)"
echo "   8. iam-config-service (配置服务)"
echo "   9. iam-frontend (前端服务)"
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

# 启动用户服务
echo "启动用户服务..."
cd ../iam-user-service
nohup mvn spring-boot:run > logs/user.log 2>&1 &
USER_PID=$!
echo "用户服务已启动，PID: $USER_PID"
sleep 5

# 启动权限服务
echo "启动权限服务..."
cd ../iam-permission-service
nohup mvn spring-boot:run > logs/permission.log 2>&1 &
PERMISSION_PID=$!
echo "权限服务已启动，PID: $PERMISSION_PID"
sleep 5

# 启动组织服务
echo "启动组织服务..."
cd ../iam-organization-service
nohup mvn spring-boot:run > logs/organization.log 2>&1 &
ORG_PID=$!
echo "组织服务已启动，PID: $ORG_PID"
sleep 5

# 启动审计服务
echo "启动审计服务..."
cd ../iam-audit-service
nohup mvn spring-boot:run > logs/audit.log 2>&1 &
AUDIT_PID=$!
echo "审计服务已启动，PID: $AUDIT_PID"
sleep 5

# 启动通知服务
echo "启动通知服务..."
cd ../iam-notification-service
nohup mvn spring-boot:run > logs/notification.log 2>&1 &
NOTIFICATION_PID=$!
echo "通知服务已启动，PID: $NOTIFICATION_PID"
sleep 5

# 启动配置服务
echo "启动配置服务..."
cd ../iam-config-service
nohup mvn spring-boot:run > logs/config.log 2>&1 &
CONFIG_PID=$!
echo "配置服务已启动，PID: $CONFIG_PID"
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
echo "  用户服务: $USER_PID"
echo "  权限服务: $PERMISSION_PID"
echo "  组织服务: $ORG_PID"
echo "  审计服务: $AUDIT_PID"
echo "  通知服务: $NOTIFICATION_PID"
echo "  配置服务: $CONFIG_PID"
echo "  前端服务: $FRONTEND_PID"
echo ""
echo "日志文件位置："
echo "  各服务日志: services/*/logs/*.log"
echo "  网关日志: iam-gateway/logs/gateway.log"
echo "  前端日志: apps/iam-frontend/logs/frontend.log"
echo ""
echo "停止服务命令："
echo "  kill $GATEWAY_PID $AUTH_PID $USER_PID $PERMISSION_PID $ORG_PID $AUDIT_PID $NOTIFICATION_PID $CONFIG_PID $FRONTEND_PID"
echo "=========================================="