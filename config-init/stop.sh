#!/bin/bash

# IAM平台停止脚本
# 用于停止所有微服务

echo "=========================================="
echo "          IAM平台停止脚本"
echo "=========================================="

# 停止所有Java进程（IAM平台相关）
echo "正在停止IAM平台服务..."

# 查找并停止所有IAM相关的Java进程
IAM_PIDS=$(ps aux | grep java | grep -E "(iam-|xiaoxin)" | grep -v grep | awk '{print $2}')

if [ -z "$IAM_PIDS" ]; then
    echo "未找到运行中的IAM平台服务"
else
    echo "找到以下IAM平台服务进程："
    ps aux | grep java | grep -E "(iam-|xiaoxin)" | grep -v grep
    
    echo ""
    read -p "确认停止这些服务？(y/n): " -n 1 -r
    echo
    
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo "正在停止服务..."
        for pid in $IAM_PIDS; do
            echo "停止进程 $pid"
            kill $pid
        done
        
        # 等待进程完全停止
        sleep 3
        
        # 检查是否还有进程在运行
        REMAINING_PIDS=$(ps aux | grep java | grep -E "(iam-|xiaoxin)" | grep -v grep | awk '{print $2}')
        if [ ! -z "$REMAINING_PIDS" ]; then
            echo "强制停止剩余进程..."
            for pid in $REMAINING_PIDS; do
                echo "强制停止进程 $pid"
                kill -9 $pid
            done
        fi
        
        echo "所有IAM平台服务已停止"
    else
        echo "取消停止操作"
    fi
fi

echo ""
echo "=========================================="
echo "          IAM平台停止完成"
echo "=========================================="