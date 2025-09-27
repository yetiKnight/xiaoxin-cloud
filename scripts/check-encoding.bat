@echo off
echo ========================================
echo IDE编码配置检查工具
echo ========================================

echo.
echo 1. 系统编码页检查:
chcp

echo.
echo 2. Java系统属性检查:
java -XshowSettings:properties -version 2>&1 | findstr "file.encoding\|user.timezone\|user.language\|user.country"

echo.
echo 3. 环境变量检查:
echo JAVA_TOOL_OPTIONS=%JAVA_TOOL_OPTIONS%
echo LANG=%LANG%

echo.
echo 4. 中文字符测试:
echo 这是中文测试：你好世界！

echo.
echo 5. 创建Java编码测试文件...
echo public class EncodingTest { > EncodingTest.java
echo     public static void main(String[] args) { >> EncodingTest.java
echo         System.out.println("系统编码: " + System.getProperty("file.encoding")); >> EncodingTest.java
echo         System.out.println("用户时区: " + System.getProperty("user.timezone")); >> EncodingTest.java
echo         System.out.println("中文测试: 你好世界"); >> EncodingTest.java
echo     } >> EncodingTest.java
echo } >> EncodingTest.java

echo.
echo 6. 编译并运行测试...
javac -encoding UTF-8 EncodingTest.java
if exist EncodingTest.class (
    java -Dfile.encoding=UTF-8 EncodingTest
    del EncodingTest.java EncodingTest.class
) else (
    echo 编译失败，请检查Java环境
)

echo.
echo ========================================
echo 检查完成！
echo 如果中文显示正常，说明编码配置正确
echo 如果出现乱码，请参考 docs/IDE编码配置解决方案.md
echo ========================================
pause
