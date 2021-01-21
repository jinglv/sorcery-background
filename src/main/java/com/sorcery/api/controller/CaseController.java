package com.sorcery.api.controller;

import cn.hutool.json.JSONUtil;
import com.sorcery.api.common.token.TokenDb;
import com.sorcery.api.constants.UserConstants;
import com.sorcery.api.dto.ResultDto;
import com.sorcery.api.dto.TokenDto;
import com.sorcery.api.dto.cases.CaseDto;
import com.sorcery.api.dto.cases.QueryCaseListDto;
import com.sorcery.api.dto.cases.UpdateCaseDto;
import com.sorcery.api.dto.page.PageTableRequest;
import com.sorcery.api.dto.page.PageTableResponse;
import com.sorcery.api.entity.Cases;
import com.sorcery.api.service.CaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
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

    /**
     * 新增测试用例
     *
     * @param request servlet request
     * @param caseDto 请求参数
     * @return 返回接口请求结果
     */
    @ApiOperation(value = "新增测试用例", notes = "新增测试用例")
    @PostMapping("add")
    public ResultDto<Cases> save(HttpServletRequest request, @RequestBody CaseDto caseDto) {
        log.info("新增测试用例,请求参数：{}", JSONUtil.parse(caseDto));
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

    @ApiOperation(value = "通过文件新增测试用例", notes = "新增测试用例")
    @PostMapping("file")
    public ResultDto<Cases> saveFile(HttpServletRequest request, @RequestParam("caseFile") MultipartFile caseFile, CaseDto caseDto) throws IOException {
        log.info("通过文件新增测试用例-请求参数：{}", JSONUtil.parse(caseDto));
        if (Objects.isNull(caseDto)) {
            return ResultDto.fail("请求参数不能为空");
        }
        if (Objects.isNull(caseFile)) {
            return ResultDto.fail("测试用例文件不能为空");
        }
        if (ObjectUtils.isEmpty(caseDto.getCaseName())) {
            return ResultDto.fail("测试用例名称不能为空");
        }
        InputStream inputStream = caseFile.getInputStream();
        String caseData = IOUtils.toString(inputStream, "UTF-8");
        inputStream.close();
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        Cases cases = new Cases();
        cases.setCreateUserId(tokenDto.getUserId());
        //BeanUtils.copyProperties(addHogwartsTestCaseDto, hogwartsTestCase);
        // CopyUtil.copyPropertiesCglib(addHogwartsTestCaseDto, hogwartsTestCase);
        //文件类型时需要将文件中的数据进行赋值
        cases.setCaseData(caseDto.getCaseData()).setCaseName(caseDto.getCaseName()).setRemark(caseDto.getRemark());
        cases.setCaseData(caseData);
        return caseService.save(cases);
    }

    /**
     * 分页列表查询
     *
     * @param request          servlet request
     * @param pageTableRequest 分页请求参数
     * @return 返回接口请求结果
     */
    @ApiOperation(value = "分页列表查询")
    @GetMapping("list")
    public ResultDto<PageTableResponse<Cases>> list(HttpServletRequest request, PageTableRequest<QueryCaseListDto> pageTableRequest) {

        log.info("测试用例分页列表查询请求参数：{} ", JSONUtil.parse(pageTableRequest));
        if (Objects.isNull(pageTableRequest)) {
            return ResultDto.success("列表查询参数不能为空");
        }
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        QueryCaseListDto params = pageTableRequest.getParams();
        if (Objects.isNull(params)) {
            params = new QueryCaseListDto();
        }
        params.setCreateUserId(tokenDto.getUserId());
        pageTableRequest.setParams(params);
        return caseService.list(pageTableRequest);
    }

    /**
     * 修改测试用例
     *
     * @param request       servlet request
     * @param updateCaseDto 测试用例修改信息
     * @return 返回接口请求结果
     */
    @ApiOperation(value = "修改测试用例")
    @PutMapping
    public ResultDto<Cases> update(HttpServletRequest request, @RequestBody UpdateCaseDto updateCaseDto) {

        log.info("修改测试用例,请求参数：{}", JSONUtil.parse(updateCaseDto));
        if (Objects.isNull(updateCaseDto)) {
            return ResultDto.fail("测试用例信息不能为空");
        }
        Integer caseId = updateCaseDto.getId();
        String caseName = updateCaseDto.getCaseName();
        if (Objects.isNull(caseId)) {
            return ResultDto.fail("测试用例id不能为空");
        }
        if (ObjectUtils.isEmpty(caseName)) {
            return ResultDto.fail("测试用例名称不能为空");
        }
        Cases cases = new Cases();
        //BeanUtils.copyProperties(addHogwartsTestCaseDto, hogwartsTestCase);
        // CopyUtil.copyPropertiesCglib(updateHogwartsTestCaseDto, hogwartsTestCase);
        cases.setId(updateCaseDto.getId())
                .setCaseData(updateCaseDto.getCaseData())
                .setCaseName(updateCaseDto.getCaseName())
                .setRemark(updateCaseDto.getRemark());
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        cases.setCreateUserId(tokenDto.getUserId());
        return caseService.update(cases);
    }

    /**
     * 根据测试用例id查询
     *
     * @param request servlet request
     * @param caseId  测试用例id
     * @return 返回接口请求结果
     */
    @ApiOperation(value = "根据测试用例id查询")
    @GetMapping("{caseId}")
    public ResultDto<Cases> getById(HttpServletRequest request, @PathVariable Integer caseId) {

        log.info("根据测试用例id查询测试用例，测试用例id:{}", caseId);
        if (Objects.isNull(caseId)) {
            return ResultDto.fail("caseId不能为空");
        }
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        return caseService.getById(caseId, tokenDto.getUserId());
    }

    /**
     * 通过caseId删除测试用例
     *
     * @param caseId 测试用例id（主键）
     * @return 返回接口请求结果
     */
    @ApiOperation(value = "通过caseId删除测试用例")
    @DeleteMapping("{caseId}")
    public ResultDto<Cases> delete(HttpServletRequest request, @PathVariable Integer caseId) {
        log.info("根据测试用例id删除测试-请求参数:{}", caseId);
        if (Objects.isNull(caseId)) {
            return ResultDto.fail("caseId不能为空");
        }
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        return caseService.delete(caseId, tokenDto.getUserId());
    }

    /**
     * 根据测试用例id查询测试用例原始数据
     *
     * @param request servlet request
     * @param caseId  测试用例id
     * @return 返回接口请求结果
     */
    @ApiOperation(value = "根据测试用例id查询测试用例原始数据")
    @GetMapping("data/{caseId}")
    public ResultDto<String> getCaseDataById(HttpServletRequest request, @PathVariable Integer caseId) {
        log.info("根据用户id和caseId查询case原始数据-请求测试用例id:{}", caseId);
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        ResultDto<String> caseData = caseService.getCaseDataById(tokenDto.getUserId(), caseId);
        log.info("根据用户id和caseId查询case原始数据-查询到的数据：{}", caseData);
        return caseData;
    }
}