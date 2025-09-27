#!/bin/bash

# 设置UTF-8编码
export LANG=zh_CN.UTF-8
export LC_ALL=zh_CN.UTF-8

echo "启动IAM平台服务..."

# 设置JVM参数
export JAVA_OPTS="-Dfile.encoding=UTF-8 -Duser.timezone=Asia/Shanghai -Djava.awt.headless=true"

echo "启动网关服务..."
cd iam-gateway
nohup mvn spring-boot:run -Dspring-boot.run.jvmArguments="$JAVA_OPTS" > ../logs/gateway.out 2>&1 &
cd ..

sleep 10

echo "启动认证服务..."
cd services/iam-auth-service
nohup mvn spring-boot:run -Dspring-boot.run.jvmArguments="$JAVA_OPTS" > ../../logs/auth.out 2>&1 &
cd ../..

sleep 5

echo "启动核心服务..."
cd services/iam-core-service
nohup mvn spring-boot:run -Dspring-boot.run.jvmArguments="$JAVA_OPTS" > ../../logs/core.out 2>&1 &
cd ../..

echo "所有服务启动完成！"
