#!/bin/bash

# Git Commit Helper Script
# 帮助生成符合规范的commit信息

echo "=== Git Commit Helper ==="
echo ""

# 获取当前分支
current_branch=$(git branch --show-current)
echo "当前分支: $current_branch"
echo ""

# 显示暂存区状态
echo "暂存区文件:"
git diff --cached --name-only
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
echo "请选择scope (可选):"
echo "1) core - 核心服务"
echo "2) auth - 认证服务"
echo "3) system - 系统服务"
echo "4) audit - 审计服务"
echo "5) config - 配置相关"
echo "6) docs - 文档"
echo "7) deps - 依赖管理"
echo "8) 跳过scope"
echo ""

read -p "请输入选项 (1-8): " scope_choice

case $scope_choice in
    1) scope="core" ;;
    2) scope="auth" ;;
    3) scope="system" ;;
    4) scope="audit" ;;
    5) scope="config" ;;
    6) scope="docs" ;;
    7) scope="deps" ;;
    8) scope="" ;;
    *) echo "无效选项"; exit 1 ;;
esac

echo ""

# 输入subject
read -p "请输入commit主题 (不超过50字符): " subject

if [ ${#subject} -gt 50 ]; then
    echo "错误: 主题超过50字符"
    exit 1
fi

echo ""

# 输入body (可选)
read -p "请输入详细描述 (可选，按Enter跳过): " body

echo ""

# 输入footer (可选)
read -p "请输入footer (如Closes #123，可选，按Enter跳过): " footer

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

echo "=== 生成的commit信息 ==="
echo "$commit_message"
echo ""

read -p "确认提交? (y/N): " confirm

if [ "$confirm" = "y" ] || [ "$confirm" = "Y" ]; then
    git commit -m "$commit_message"
    echo "提交成功!"
else
    echo "取消提交"
fi
