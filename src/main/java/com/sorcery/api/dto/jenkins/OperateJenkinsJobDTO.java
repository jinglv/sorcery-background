package com.sorcery.api.dto.jenkins;

import com.sorcery.api.dto.BaseDTO;
import com.sorcery.api.dto.TokenDTO;
import com.sorcery.api.entity.Jenkins;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author jingLv
 * @date 2021/01/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OperateJenkinsJobDTO extends BaseDTO {
    /**
     * token信息
     */
    private TokenDTO tokenDto;
    /**
     * Jenkins信息
     */
    private Jenkins jenkins;
    /**
     * Jenkins Job构建传入参数
     */
    private Map<String, String> params;
}
