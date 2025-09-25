@echo off
chcp 65001 >nul
echo ==========================================
echo    小新云IAM平台 - 环境变量检查脚本
echo ==========================================
echo.

:: ==========================================
:: 期望配置值定义区域 - 与setup-env-windows.bat保持一致
:: ==========================================

:: 基础服务配置
set "EXPECTED_NACOS_SERVER_ADDR=localhost:8848"
set "EXPECTED_REDIS_HOST=localhost"
set "EXPECTED_REDIS_PORT=6379"
set "EXPECTED_REDIS_PASSWORD=123456"
set "EXPECTED_SENTINEL_DASHBOARD=localhost:8181"
set "EXPECTED_ROCKETMQ_NAME_SERVER=localhost:9876"
set "EXPECTED_SEATA_SERVER=localhost:8091"



:: ==========================================
:: 检查逻辑区域 - 无需修改
:: ==========================================

echo 🔧 微服务基础设施配置:
echo ----------------------------------------
call :check_env NACOS_SERVER_ADDR "%EXPECTED_NACOS_SERVER_ADDR%"
call :check_env REDIS_HOST "%EXPECTED_REDIS_HOST%"
call :check_env REDIS_PORT "%EXPECTED_REDIS_PORT%"
call :check_env REDIS_PASSWORD "%EXPECTED_REDIS_PASSWORD%"
call :check_env SENTINEL_DASHBOARD "%EXPECTED_SENTINEL_DASHBOARD%"
call :check_env ROCKETMQ_NAME_SERVER "%EXPECTED_ROCKETMQ_NAME_SERVER%"
call :check_env SEATA_SERVER "%EXPECTED_SEATA_SERVER%"

goto :main_end



echo.
echo ==========================================
echo              检查完成！
echo ==========================================
echo.
echo 📝 说明:
echo   ✓ = 已设置   ✗ = 未设置
echo   [匹配默认值] = 与期望配置一致
echo   [自定义值] = 用户自定义配置
echo.
echo 💡 配置指南:
echo   • 如需设置环境变量，运行: setup-env-windows.bat
echo   • 如需修改配置值，编辑 setup-env-windows.bat 顶部的配置区域
echo   • 生产环境请务必修改Redis密码
echo.
echo 🔧 配置示例:
echo   在 setup-env-windows.bat 中修改:
echo   set "REDIS_PASSWORD_VALUE=your_secure_password"
echo   set "NACOS_SERVER_ADDR_VALUE=192.168.1.100:8848"
echo.
pause
goto :eof

:: ==========================================
:: 子例程定义区域
:: ==========================================

:: 定义检查函数
:check_env
set VAR_NAME=%1
set DEFAULT_VALUE=%2
call set VAR_VALUE=%%%VAR_NAME%%%
if "%VAR_VALUE%"=="" (
    if "%DEFAULT_VALUE%"=="" (
        echo   ✗ %VAR_NAME% = ^(未设置^) [可选配置]
    ) else (
        echo   ✗ %VAR_NAME% = ^(未设置^) [默认: %DEFAULT_VALUE%]
    )
) else (
    if "%VAR_VALUE%"=="%DEFAULT_VALUE%" (
        echo   ✓ %VAR_NAME% = %VAR_VALUE% [匹配默认值]
    ) else (
        echo   ✓ %VAR_NAME% = %VAR_VALUE% [自定义值]
    )
)
goto :eof

:main_end
