#!/bin/bash

# Git Commit Helper Script - 增强版
# 基于Cursor规则10-git-workflow.mdc自动生成符合规范的commit信息

set -e  # 遇到错误立即退出

echo "=== Git Commit Helper - 增强版 ==="
echo ""

# 检查是否有暂存的文件
if [ -z "$(git diff --cached --name-only)" ]; then
    echo "❌ 错误: 没有暂存的文件，请先使用 'git add' 添加文件"
    exit 1
fi

# 获取当前分支
current_branch=$(git branch --show-current)
echo "🌿 当前分支: $current_branch"
echo ""

# 显示暂存区状态
echo "📁 暂存区文件:"
git diff --cached --name-only | sed 's/^/  ✓ /'
echo ""

# 智能推荐scope
echo "🔍 智能分析变更范围..."
staged_files=$(git diff --cached --name-only)
suggested_scope=""

if echo "$staged_files" | grep -q "services/iam-core-service/"; then
    suggested_scope="core"
elif echo "$staged_files" | grep -q "services/iam-auth-service/"; then
    suggested_scope="auth"
elif echo "$staged_files" | grep -q "services/iam-system-service/"; then
    suggested_scope="system"
elif echo "$staged_files" | grep -q "services/iam-audit-service/"; then
    suggested_scope="audit"
elif echo "$staged_files" | grep -q "iam-gateway/"; then
    suggested_scope="gateway"
elif echo "$staged_files" | grep -q "iam-common/"; then
    suggested_scope="common"
elif echo "$staged_files" | grep -q "config-init/"; then
    suggested_scope="config"
elif echo "$staged_files" | grep -q "docs/"; then
    suggested_scope="docs"
elif echo "$staged_files" | grep -q "iam-dependencies/\|pom.xml"; then
    suggested_scope="deps"
fi

if [ -n "$suggested_scope" ]; then
    echo "💡 建议的scope: $suggested_scope"
fi
echo ""

# 选择commit类型
echo "请选择commit类型:"
echo "1) feat - 新功能"
echo "2) fix - 修复bug"
echo "3) docs - 文档更新"
echo "4) style - 代码格式调整"
echo "5) refactor - 重构代码"
echo "6) perf - 性能优化"
echo "7) test - 测试相关"
echo "8) chore - 构建过程或辅助工具的变动"
echo "9) ci - 持续集成相关"
echo "10) build - 构建系统或外部依赖的变动"
echo ""

read -p "请输入选项 (1-10): " type_choice

case $type_choice in
    1) commit_type="feat" ;;
    2) commit_type="fix" ;;
    3) commit_type="docs" ;;
    4) commit_type="style" ;;
    5) commit_type="refactor" ;;
    6) commit_type="perf" ;;
    7) commit_type="test" ;;
    8) commit_type="chore" ;;
    9) commit_type="ci" ;;
    10) commit_type="build" ;;
    *) echo "无效选项"; exit 1 ;;
esac

echo ""

# 选择scope
echo "📂 请选择scope (影响范围):"
echo "1) core - 核心服务 (iam-core-service)"
echo "2) auth - 认证服务 (iam-auth-service)"  
echo "3) system - 系统服务 (iam-system-service)"
echo "4) audit - 审计服务 (iam-audit-service)"
echo "5) gateway - 网关服务 (iam-gateway)"
echo "6) common - 公共模块 (iam-common)"
echo "7) config - 配置相关 (config-init)"
echo "8) docs - 文档 (docs)"
echo "9) deps - 依赖管理 (iam-dependencies)"
echo "10) 跳过scope"

if [ -n "$suggested_scope" ]; then
    echo ""
    echo "💡 推荐选择: $suggested_scope (基于变更文件智能推荐)"
fi
echo ""

read -p "请输入选项 (1-10): " scope_choice

case $scope_choice in
    1) scope="core" ;;
    2) scope="auth" ;;
    3) scope="system" ;;
    4) scope="audit" ;;
    5) scope="gateway" ;;
    6) scope="common" ;;
    7) scope="config" ;;
    8) scope="docs" ;;
    9) scope="deps" ;;
    10) scope="" ;;
    *) echo "❌ 无效选项"; exit 1 ;;
esac

echo ""

# 输入subject
echo "📝 请输入commit主题:"
echo "提示: 使用现在时态，首字母小写，不超过50字符，不加句号"
read -p "主题: " subject

if [ -z "$subject" ]; then
    echo "❌ 错误: 主题不能为空"
    exit 1
fi

if [ ${#subject} -gt 50 ]; then
    echo "❌ 错误: 主题超过50字符 (当前${#subject}字符)"
    exit 1
fi

echo ""

# 输入body (可选)
echo "📄 请输入详细描述 (可选):"
echo "提示: 解释为什么做这个改动，与之前版本有什么不同"
read -p "详细描述 (按Enter跳过): " body

echo ""

# 输入footer (可选) 
echo "🔗 请输入footer (可选):"
echo "提示: 如 'Closes #123' 或 'BREAKING CHANGE: 详细说明'"
read -p "Footer (按Enter跳过): " footer

echo ""

# 构建commit信息
if [ -n "$scope" ]; then
    commit_message="$commit_type($scope): $subject"
else
    commit_message="$commit_type: $subject"
fi

if [ -n "$body" ]; then
    commit_message="$commit_message

$body"
fi

if [ -n "$footer" ]; then
    commit_message="$commit_message

$footer"
fi

echo "=== 📋 生成的commit信息 ==="
echo "$commit_message"
echo ""

# 使用commitlint验证
echo "🔍 正在验证commit信息格式..."
if command -v npx >/dev/null 2>&1; then
    echo "$commit_message" | npx commitlint
    if [ $? -eq 0 ]; then
        echo "✅ commit信息格式验证通过"
    else
        echo "❌ commit信息格式验证失败"
        exit 1
    fi
else
    echo "⚠️  未安装commitlint，跳过格式验证"
fi

echo ""
read -p "确认提交? (y/N): " confirm

if [ "$confirm" = "y" ] || [ "$confirm" = "Y" ]; then
    git commit -m "$commit_message"
    echo "✅ 提交成功!"
    
    # 提示推送
    echo ""
    echo "💡 别忘了推送到远程仓库:"
    echo "   git push origin $current_branch"
else
    echo "❌ 取消提交"
fi
