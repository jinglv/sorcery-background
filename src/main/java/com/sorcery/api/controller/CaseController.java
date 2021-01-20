package com.sorcery.api.controller;

import cn.hutool.json.JSONUtil;
import com.sorcery.api.common.token.TokenDb;
import com.sorcery.api.constants.UserConstants;
import com.sorcery.api.dto.ResultDto;
import com.sorcery.api.dto.TokenDto;
import com.sorcery.api.dto.cases.CaseDto;
import com.sorcery.api.entity.Cases;
import com.sorcery.api.service.CaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author jingLv
 * @date 2021/01/19
 */
@Slf4j
@Api(tags = "测试用例管理")
@RestController
@RequestMapping("/cases")
public class CaseController {
    private final CaseService caseService;
    private final TokenDb tokenDb;

    public CaseController(CaseService caseService, TokenDb tokenDb) {
        this.caseService = caseService;
        this.tokenDb = tokenDb;
    }

    @ApiOperation(value = "批量新增测试用例", notes = "仅用于测试用户")
    @PostMapping("text")
    public ResultDto<Cases> save(HttpServletRequest request, @RequestBody CaseDto caseDto) {
        log.info("新增文本测试用例-请求入参：" + JSONUtil.parse(caseDto));
        if (Objects.isNull(caseDto)) {
            return ResultDto.fail("请求参数不能为空");
        }

        if (ObjectUtils.isEmpty(caseDto.getCaseData())) {
            return ResultDto.fail("测试用例数据不能为空");
        }
        if (ObjectUtils.isEmpty(caseDto.getCaseName())) {
            return ResultDto.fail("测试用例名称不能为空");
        }
        Cases cases = new Cases();
        cases.setCaseData(caseDto.getCaseData()).setCaseName(caseDto.getCaseName()).setRemark(caseDto.getRemark());
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        cases.setCreateUserId(tokenDto.getUserId());
        return caseService.save(cases);
    }
}
