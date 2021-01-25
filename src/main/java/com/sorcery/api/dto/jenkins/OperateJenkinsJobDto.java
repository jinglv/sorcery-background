package com.sorcery.api.dto.jenkins;

import com.sorcery.api.dto.TokenDto;
import com.sorcery.api.entity.Jenkins;
import lombok.Data;

import java.util.Map;

/**
 * @author jingLv
 * @date 2021/01/25
 */
@Data
public class OperateJenkinsJobDto {
    /**
     * token信息
     */
    private TokenDto tokenDto;
    /**
     * Jenkins信息
     */
    private Jenkins jenkins;
    /**
     * 构建参数
     */
    private Map<String, String> params;
}
