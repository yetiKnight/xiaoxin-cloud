package com.xiaoxin.iam.audit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoxin.iam.audit.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志Mapper接口
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {
}
