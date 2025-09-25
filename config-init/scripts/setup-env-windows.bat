@echo off
chcp 65001 >nul
echo ==========================================
echo    小新云IAM平台 - Windows环境变量配置脚本
echo ==========================================
echo.

:: ==========================================
:: 配置变量定义区域 - 在此修改配置值
:: ==========================================
:: 
:: 使用说明：
:: 1. 如需修改环境变量，只需在下方修改对应的*_VALUE变量值
:: 2. 空值用 ="" 表示，非空值用 ="实际值" 表示
:: 3. 修改后保存并重新运行脚本即可应用新配置
:: 4. 生产环境请务必修改Redis密码
:: 
:: 配置示例：
:: set "REDIS_PASSWORD_VALUE=your_secure_password"
:: set "NACOS_SERVER_ADDR_VALUE=192.168.1.100:8848"
::
:: ==========================================

:: 基础服务配置
set "NACOS_SERVER_ADDR_VALUE=localhost:8848"
set "REDIS_HOST_VALUE=localhost"
set "REDIS_PORT_VALUE=6379"
set "REDIS_PASSWORD_VALUE=123456"
set "SENTINEL_DASHBOARD_VALUE=localhost:8181"
set "ROCKETMQ_NAME_SERVER_VALUE=localhost:9876"
set "SEATA_SERVER_VALUE=localhost:8091"



:: ==========================================
:: 脚本执行逻辑区域 - 无需修改
:: ==========================================

:: 设置颜色输出
for /f %%A in ('"prompt $H &echo on &for %%B in (1) do rem"') do set BS=%%A

:: 检查管理员权限
>nul 2>&1 "%SYSTEMROOT%\system32\cacls.exe" "%SYSTEMROOT%\system32\config\system"
if '%errorlevel%' NEQ '0' (
    echo [警告] 建议以管理员身份运行此脚本以设置系统环境变量
    echo [提示] 当前将设置用户级环境变量
    echo.
    set SCOPE=USER
) else (
    echo [信息] 检测到管理员权限，将设置系统级环境变量
    echo.
    set SCOPE=MACHINE
)

echo 正在配置IAM平台环境变量...
echo.

:: ===========================================
:: 基础服务配置
:: ===========================================
echo [1/3] 配置Nacos服务...
call :set_env_var NACOS_SERVER_ADDR "%NACOS_SERVER_ADDR_VALUE%"

echo [2/3] 配置Redis服务...
call :set_env_var REDIS_HOST "%REDIS_HOST_VALUE%"
call :set_env_var REDIS_PORT "%REDIS_PORT_VALUE%"
call :set_env_var REDIS_PASSWORD "%REDIS_PASSWORD_VALUE%"

echo [3/3] 配置微服务基础设施...
call :set_env_var SENTINEL_DASHBOARD "%SENTINEL_DASHBOARD_VALUE%"
call :set_env_var ROCKETMQ_NAME_SERVER "%ROCKETMQ_NAME_SERVER_VALUE%"
call :set_env_var SEATA_SERVER "%SEATA_SERVER_VALUE%"

goto :main_end


echo.
echo ==========================================
echo              配置完成！
echo ==========================================
echo.
echo 📋 已配置的环境变量：
echo.
echo 🔧 微服务基础设施：
echo   • Nacos注册/配置中心: %NACOS_SERVER_ADDR_VALUE%
echo   • Redis缓存: %REDIS_HOST_VALUE%:%REDIS_PORT_VALUE%
echo   • Sentinel流控: %SENTINEL_DASHBOARD_VALUE%
echo   • RocketMQ消息: %ROCKETMQ_NAME_SERVER_VALUE%
echo   • Seata分布式事务: %SEATA_SERVER_VALUE%
echo.
echo ⚠️  重要提示：
echo   1. 请重启命令行或IDE以使环境变量生效
echo   2. 如需修改配置，编辑脚本顶部的"配置变量定义区域"
echo   3. 生产环境请修改Redis密码
echo.

:: 询问是否立即重启explorer以刷新环境变量
echo 🔄 是否重启Windows资源管理器以立即刷新环境变量？
echo    (这将关闭所有文件管理器窗口)
echo.
set /p restart="输入 Y 重启，任意键跳过: "
if /i "%restart%"=="Y" (
    echo 正在重启Windows资源管理器...
    taskkill /f /im explorer.exe >nul 2>&1
    start explorer.exe
    echo ✓ 资源管理器已重启，环境变量已刷新
) else (
    echo ⏭️  已跳过重启，请手动重启应用程序以使环境变量生效
)

echo.
echo 🎉 环境配置脚本执行完成！
echo 📖 如需修改配置，可直接运行此脚本或手动设置环境变量
echo.
pause
goto :eof

:: ==========================================
:: 子例程定义区域
:: ==========================================

:: 定义设置环境变量的函数
:set_env_var
set VAR_NAME=%1
set VAR_VALUE=%2
if "%VAR_VALUE%"=="" (
    echo   ⚠ %VAR_NAME% = ^(跳过空值^)
) else (
    setx %VAR_NAME% "%VAR_VALUE%" /M >nul 2>&1 || setx %VAR_NAME% "%VAR_VALUE%" >nul
    echo   ✓ %VAR_NAME% = %VAR_VALUE%
)
goto :eof

:main_end
