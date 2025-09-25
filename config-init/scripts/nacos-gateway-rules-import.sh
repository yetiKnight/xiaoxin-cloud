#!/bin/bash

# =============================================================================
# IAM网关Sentinel规则推送脚本
# 用途: 将所有Sentinel规则配置推送到Nacos配置中心
# 版本: 2.0
# 更新: 支持完整的6种Sentinel规则类型
# =============================================================================

set -e  # 遇到错误立即退出

# 配置参数
NACOS_SERVER="${NACOS_SERVER:-localhost:8848}"
NAMESPACE="${NACOS_NAMESPACE:-dev}"
GROUP="${SENTINEL_GROUP:-SENTINEL_GROUP}"
CONFIG_DIR="../sentinel"

# 颜色输出函数
print_info() {
    echo -e "\033[32m[INFO]\033[0m $1"
}

print_error() {
    echo -e "\033[31m[ERROR]\033[0m $1"
}

print_warn() {
    echo -e "\033[33m[WARN]\033[0m $1"
}

# 检查依赖
check_dependencies() {
    if ! command -v curl &> /dev/null; then
        print_error "curl命令未找到，请先安装curl"
        exit 1
    fi
    
    if [ ! -d "$CONFIG_DIR" ]; then
        print_error "配置目录不存在: $CONFIG_DIR"
        exit 1
    fi
}

# 推送单个配置文件
push_config() {
    local file_path=$1
    local data_id=$2
    local description=$3
    
    if [ ! -f "$file_path" ]; then
        print_warn "配置文件不存在，跳过: $file_path"
        return 1
    fi
    
    print_info "推送$description..."
    
    local response=$(curl -s -w "%{http_code}" -X POST "http://${NACOS_SERVER}/nacos/v1/cs/configs" \
        -d "dataId=$data_id" \
        -d "group=$GROUP" \
        -d "tenant=$NAMESPACE" \
        -d "content=$(cat "$file_path")" \
        -d "type=json")
    
    local http_code="${response: -3}"
    local body="${response%???}"
    
    if [ "$http_code" = "200" ] && [ "$body" = "true" ]; then
        print_info "✅ $description 推送成功"
        return 0
    else
        print_error "❌ $description 推送失败 (HTTP: $http_code, Response: $body)"
        return 1
    fi
}

# 主函数
main() {
    print_info "开始推送IAM网关Sentinel规则配置到Nacos..."
    print_info "Nacos服务器: $NACOS_SERVER"
    print_info "命名空间: $NAMESPACE"
    print_info "配置组: $GROUP"
    print_info "配置目录: $CONFIG_DIR"
    echo

    # 检查依赖
    check_dependencies
    
    local success_count=0
    local total_count=0
    
    # 定义规则配置映射 (文件名:dataId:描述)
    declare -a rules=(
        "iam-gateway-flow-rules.json:gateway-flow-rules:网关流量控制规则"
        "iam-gateway-degrade-rules.json:gateway-degrade-rules:网关熔断降级规则"
        "iam-gateway-api-group-rules.json:gateway-api-group-rules:网关API分组规则"
        "iam-gateway-system-rules.json:gateway-system-rules:网关系统保护规则"
        "iam-gateway-param-flow-rules.json:gateway-param-flow-rules:网关热点参数限流规则"
        "iam-gateway-authority-rules.json:gateway-authority-rules:网关授权规则"
    )
    
    # 推送所有规则
    for rule in "${rules[@]}"; do
        IFS=':' read -r file_name data_id description <<< "$rule"
        file_path="$CONFIG_DIR/$file_name"
        
        total_count=$((total_count + 1))
        if push_config "$file_path" "$data_id" "$description"; then
            success_count=$((success_count + 1))
        fi
        echo
    done
    
    # 输出结果统计
    echo "=============================================="
    print_info "推送完成! 成功: $success_count/$total_count"
    
    if [ $success_count -eq $total_count ]; then
        print_info "🎉 所有Sentinel规则推送成功!"
        echo
        print_info "请访问Nacos控制台验证配置:"
        print_info "http://$NACOS_SERVER/nacos"
        echo
        print_info "下一步操作:"
        print_info "1. 启动Sentinel Dashboard"
        print_info "2. 启动IAM网关服务"
        print_info "3. 验证规则是否生效"
        exit 0
    else
        print_error "⚠️  部分配置推送失败，请检查错误信息"
        exit 1
    fi
}

# 脚本入口
main "$@"

