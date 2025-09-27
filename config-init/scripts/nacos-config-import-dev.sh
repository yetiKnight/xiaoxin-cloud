#!/bin/bash

# Nacos配置导入脚本
# 用于将配置文件导入到Nacos配置中心
# 
# 功能特性：
# - 自动识别配置文件格式（YAML/Properties/JSON/XML）
# - 正确处理配置内容的URL编码
# - 统一命名空间和分组管理
# - 详细的导入状态反馈

# Nacos服务器配置
NACOS_SERVER="http://localhost:8848"
NACOS_USERNAME="nacos"
NACOS_PASSWORD="nacos"
NACOS_NAMESPACE="dev"
NACOS_GROUP="IAM_GROUP"

# 配置文件目录
CONFIG_DIR="../config/dev"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}=== IAM平台Nacos配置导入脚本 ===${NC}"
echo "Nacos服务器: $NACOS_SERVER"
echo "命名空间: $NACOS_NAMESPACE"
echo "分组: $NACOS_GROUP"
echo ""

# 检查Nacos服务器是否可用
echo -e "${YELLOW}检查Nacos服务器连接...${NC}"
if ! curl -s "$NACOS_SERVER/nacos" > /dev/null; then
    echo -e "${RED}错误: 无法连接到Nacos服务器 $NACOS_SERVER${NC}"
    echo "请确保Nacos服务器已启动并可访问"
    exit 1
fi
echo -e "${GREEN}✓ Nacos服务器连接正常${NC}"
echo ""

# 获取访问Token（如果需要）
get_access_token() {
    local response=$(curl -s -X POST "$NACOS_SERVER/nacos/v1/auth/login" \
        -d "username=$NACOS_USERNAME&password=$NACOS_PASSWORD")
    
    if [[ $response == *"accessToken"* ]]; then
        echo $response | grep -o '"accessToken":"[^"]*"' | cut -d'"' -f4
    else
        echo ""
    fi
}

# 导入配置文件
import_config() {
    local config_file=$1
    local data_id=$2
    local content=$(cat "$CONFIG_DIR/$config_file")
    
    # 根据文件扩展名确定配置类型
    local config_type="text"
    if [[ $config_file == *.yml ]] || [[ $config_file == *.yaml ]]; then
        config_type="yaml"
    elif [[ $config_file == *.properties ]]; then
        config_type="properties"
    elif [[ $config_file == *.json ]]; then
        config_type="json"
    elif [[ $config_file == *.xml ]]; then
        config_type="xml"
    fi
    
    echo -e "${YELLOW}导入配置: $data_id (格式: $config_type)${NC}"
    
    # 使用curl的--data-urlencode来正确处理内容编码
    local response=$(curl -s -X POST "$NACOS_SERVER/nacos/v1/cs/configs" \
        -H "Content-Type: application/x-www-form-urlencoded" \
        --data-urlencode "dataId=$data_id" \
        --data-urlencode "group=$NACOS_GROUP" \
        --data-urlencode "content=$content" \
        --data-urlencode "type=$config_type" \
        $([ ! -z "$NACOS_NAMESPACE" ] && echo "--data-urlencode tenant=$NACOS_NAMESPACE"))
    
    if [[ $response == "true" ]]; then
        echo -e "${GREEN}✓ $data_id 导入成功 (格式: $config_type)${NC}"
        return 0
    else
        echo -e "${RED}✗ $data_id 导入失败: $response${NC}"
        return 1
    fi
}

# 配置文件列表
declare -A configs=(
    ["iam-auth-service-dev.yml"]="iam-auth-service-dev.yml"
    ["iam-core-service-dev.yml"]="iam-core-service-dev.yml"
    ["iam-system-service-dev.yml"]="iam-system-service-dev.yml"
    ["iam-audit-service-dev.yml"]="iam-audit-service-dev.yml"
    ["iam-gateway-dev.yml"]="iam-gateway-dev.yml"
    ["iam-common.yml"]="iam-common.yml"
)

# 导入所有配置文件
success_count=0
total_count=${#configs[@]}

for config_file in "${!configs[@]}"; do
    data_id="${configs[$config_file]}"
    
    if [ -f "$CONFIG_DIR/$config_file" ]; then
        if import_config "$config_file" "$data_id"; then
            ((success_count++))
        fi
    else
        echo -e "${RED}✗ 配置文件不存在: $CONFIG_DIR/$config_file${NC}"
    fi
    echo ""
done

# 导入结果统计
echo -e "${GREEN}=== 导入结果统计 ===${NC}"
echo "成功导入: $success_count/$total_count"

if [ $success_count -eq $total_count ]; then
    echo -e "${GREEN}🎉 所有配置文件导入成功！${NC}"
    exit 0
else
    echo -e "${YELLOW}⚠️  部分配置文件导入失败，请检查错误信息${NC}"
    exit 1
fi
