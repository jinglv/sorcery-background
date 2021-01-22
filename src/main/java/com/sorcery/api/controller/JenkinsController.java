package com.sorcery.api.controller;

import cn.hutool.json.JSONUtil;
import com.sorcery.api.common.token.TokenDb;
import com.sorcery.api.constants.UserConstants;
import com.sorcery.api.dto.ResultDto;
import com.sorcery.api.dto.TokenDto;
import com.sorcery.api.dto.jenkins.JenkinsDto;
import com.sorcery.api.dto.jenkins.QueryJenkinsListDto;
import com.sorcery.api.dto.jenkins.UpdateJenkinsDto;
import com.sorcery.api.dto.page.PageTableRequest;
import com.sorcery.api.dto.page.PageTableResponse;
import com.sorcery.api.entity.Jenkins;
import com.sorcery.api.service.JenkinsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author jingLv
 * @date 2021/01/19
 */
@Slf4j
@Api(tags = "Jenkins管理")
@RestController
@RequestMapping("/jenkins")
public class JenkinsController {

    private final JenkinsService jenkinsService;
    private final TokenDb tokenDb;

    public JenkinsController(JenkinsService jenkinsService, TokenDb tokenDb) {
        this.jenkinsService = jenkinsService;
        this.tokenDb = tokenDb;
    }

    /**
     * 新增Jenkins信息
     *
     * @param request    servlet request
     * @param jenkinsDto Jenkins新增信息
     * @return 返回接口请求结果
     */
    @ApiOperation(value = "添加Jenkins")
    @PostMapping
    public ResultDto<Jenkins> save(HttpServletRequest request, @RequestBody JenkinsDto jenkinsDto) {
        log.info("添加Jenkins信息，请求参数：{}", JSONUtil.parse(jenkinsDto));
        if (Objects.isNull(jenkinsDto)) {
            return ResultDto.success("Jenkins信息不能为空");
        }
        String name = jenkinsDto.getName();
        if (ObjectUtils.isEmpty(name)) {
            return ResultDto.success("Jenkins名称不能为空");
        }
        Jenkins jenkins = new Jenkins();
        // CopyUtil.copyPropertiesCglib(addHogwartsTestJenkinsDto, hogwartsTestJenkins);
        jenkins.setName(jenkinsDto.getName())
                .setTestCommand(jenkinsDto.getTestCommand())
                .setUrl(jenkinsDto.getUrl())
                .setUserName(jenkinsDto.getUserName())
                .setPassword(jenkinsDto.getPassword())
                .setRemark(jenkinsDto.getRemark())
                .setDefaultJenkinsFlag(jenkinsDto.getDefaultJenkinsFlag())
                .setCommandRunCaseType(jenkinsDto.getCommandRunCaseType())
                .setCommandRunCaseSuffix(jenkinsDto.getCommandRunCaseSuffix());
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        jenkins.setCreateUserId(tokenDto.getUserId());
        String commandRunCaseSuffix = jenkinsDto.getCommandRunCaseSuffix();
        //过滤带.的后缀，如.yml改为yml
        if (!ObjectUtils.isEmpty(commandRunCaseSuffix)) {
            jenkins.setCommandRunCaseSuffix(commandRunCaseSuffix.replace(".", ""));
        }
        return jenkinsService.save(tokenDto, jenkins);
    }

    /**
     * 修改Jenkins
     *
     * @param request          servlet request
     * @param updateJenkinsDto Jenkins修改信息
     * @return 返回接口请求结果
     */
    @ApiOperation(value = "修改Jenkins")
    @PutMapping
    public ResultDto<Jenkins> update(HttpServletRequest request, @RequestBody UpdateJenkinsDto updateJenkinsDto) {
        log.info("修改Jenkins信息，请求参数：{}", JSONUtil.parse(updateJenkinsDto));
        if (Objects.isNull(updateJenkinsDto)) {
            return ResultDto.success("Jenkins信息不能为空");
        }
        Integer jenkinsId = updateJenkinsDto.getId();
        String name = updateJenkinsDto.getName();
        if (Objects.isNull(jenkinsId)) {
            return ResultDto.fail("JenkinsId不能为空");
        }
        if (ObjectUtils.isEmpty(name)) {
            return ResultDto.fail("Jenkins名称不能为空");
        }
        Jenkins jenkins = new Jenkins();
        // CopyUtil.copyPropertiesCglib(updateHogwartsTestJenkinsDto, hogwartsTestJenkins);
        jenkins.setName(updateJenkinsDto.getName())
                .setTestCommand(updateJenkinsDto.getTestCommand())
                .setUrl(updateJenkinsDto.getUrl())
                .setUserName(updateJenkinsDto.getUserName())
                .setPassword(updateJenkinsDto.getPassword())
                .setRemark(updateJenkinsDto.getRemark())
                .setDefaultJenkinsFlag(updateJenkinsDto.getDefaultJenkinsFlag())
                .setCommandRunCaseType(updateJenkinsDto.getCommandRunCaseType())
                .setCommandRunCaseSuffix(updateJenkinsDto.getCommandRunCaseSuffix());
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        jenkins.setCreateUserId(tokenDto.getUserId());
        String commandRunCaseSuffix = updateJenkinsDto.getCommandRunCaseSuffix();
        //过滤待.的后缀，如.yml改为yml
        if (!ObjectUtils.isEmpty(commandRunCaseSuffix)) {
            jenkins.setCommandRunCaseSuffix(commandRunCaseSuffix.replace(".", ""));
        }
        return jenkinsService.update(tokenDto, jenkins);
    }

    /**
     * 根据jenkinsId查询Jenkins信息
     *
     * @param request   servlet request
     * @param jenkinsId JenkinsId
     * @return 返回接口请求结果
     */
    @ApiOperation(value = "根据jenkinsId查询Jenkins信息")
    @GetMapping("/{jenkinsId}")
    public ResultDto<Jenkins> getById(HttpServletRequest request, @PathVariable Integer jenkinsId) {
        log.info("根据jenkinsId查询Jenkins信息,JenkinsId为：{} ", jenkinsId);
        if (Objects.isNull(jenkinsId)) {
            return ResultDto.fail("JenkinsId不能为空");
        }
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        return jenkinsService.getById(jenkinsId, tokenDto.getUserId());
    }

    /**
     * 根据jenkinsId删除Jenkins信息
     *
     * @param request   servlet request
     * @param jenkinsId JenkinsId
     * @return 返回接口请求结果
     */
    @ApiOperation(value = "根据jenkinsId删除Jenkins信息")
    @DeleteMapping("/{jenkinsId}")
    public ResultDto<Jenkins> delete(HttpServletRequest request, @PathVariable Integer jenkinsId) {
        log.info("根据jenkinsId删除-入参= " + jenkinsId);
        if (Objects.isNull(jenkinsId)) {
            return ResultDto.fail("JenkinsId不能为空");
        }
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        return jenkinsService.delete(jenkinsId, tokenDto);
    }

    /**
     * 分页列表查询
     *
     * @param request          servlet request
     * @param pageTableRequest 分页请求
     * @return 返回接口请求结果
     */
    @ApiOperation(value = "分页列表查询")
    @GetMapping("list")
    public ResultDto<PageTableResponse<Jenkins>> list(HttpServletRequest request, PageTableRequest<QueryJenkinsListDto> pageTableRequest) {
        if (Objects.isNull(pageTableRequest)) {
            return ResultDto.fail("列表查询参数不能为空");
        }
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        log.info("分页列表查询，请求参数:{}, token信息：{}", JSONUtil.parse(pageTableRequest), JSONUtil.parse(tokenDto));
        QueryJenkinsListDto params = pageTableRequest.getParams();
        if (Objects.isNull(params)) {
            params = new QueryJenkinsListDto();
        }
        params.setCreateUserId(tokenDto.getUserId());
        pageTableRequest.setParams(params);
        return jenkinsService.list(pageTableRequest);
    }
}
