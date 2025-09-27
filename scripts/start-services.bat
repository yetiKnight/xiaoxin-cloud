@echo off
chcp 65001
echo 启动IAM平台服务...

REM 设置环境变量
set JAVA_OPTS=-Dfile.encoding=UTF-8 -Duser.timezone=Asia/Shanghai -Djava.awt.headless=true

echo 启动网关服务...
start "IAM-Gateway" cmd /c "cd /d iam-gateway && mvn spring-boot:run -Dspring-boot.run.jvmArguments=\"%JAVA_OPTS%\""

timeout /t 10 /nobreak

echo 启动认证服务...
start "IAM-Auth" cmd /c "cd /d services/iam-auth-service && mvn spring-boot:run -Dspring-boot.run.jvmArguments=\"%JAVA_OPTS%\""

timeout /t 5 /nobreak

echo 启动核心服务...
start "IAM-Core" cmd /c "cd /d services/iam-core-service && mvn spring-boot:run -Dspring-boot.run.jvmArguments=\"%JAVA_OPTS%\""

echo 所有服务启动完成！
pause
