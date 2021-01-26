package com.sorcery.api.service.impl;

import cn.hutool.json.JSONUtil;
import com.sorcery.api.common.exception.ServiceException;
import com.sorcery.api.common.jenkins.JenkinsClient;
import com.sorcery.api.common.utils.JenkinsUtils;
import com.sorcery.api.common.utils.StrUtils;
import com.sorcery.api.constants.Constants;
import com.sorcery.api.dao.CaseMapper;
import com.sorcery.api.dao.JenkinsMapper;
import com.sorcery.api.dao.TaskCaseRelMapper;
import com.sorcery.api.dao.TaskMapper;
import com.sorcery.api.dto.RequestInfoDto;
import com.sorcery.api.dto.ResultDto;
import com.sorcery.api.dto.TokenDto;
import com.sorcery.api.dto.jenkins.OperateJenkinsJobDto;
import com.sorcery.api.dto.page.PageTableRequest;
import com.sorcery.api.dto.page.PageTableResponse;
import com.sorcery.api.dto.task.AddTaskDto;
import com.sorcery.api.dto.task.QueryTaskListDto;
import com.sorcery.api.dto.task.TaskDto;
import com.sorcery.api.entity.Cases;
import com.sorcery.api.entity.Jenkins;
import com.sorcery.api.entity.Task;
import com.sorcery.api.entity.TaskCaseRel;
import com.sorcery.api.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author jingLv
 * @date 2021/01/21
 */
@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final JenkinsMapper jenkinsMapper;
    private final CaseMapper caseMapper;
    private final TaskCaseRelMapper taskCaseRelMapper;
    private final JenkinsClient jenkinsClient;

    public TaskServiceImpl(TaskMapper taskMapper, JenkinsMapper jenkinsMapper, CaseMapper caseMapper, TaskCaseRelMapper taskCaseRelMapper, JenkinsClient jenkinsClient) {
        this.taskMapper = taskMapper;
        this.jenkinsMapper = jenkinsMapper;
        this.caseMapper = caseMapper;
        this.taskCaseRelMapper = taskCaseRelMapper;
        this.jenkinsClient = jenkinsClient;
    }

    /**
     * 新增测试任务信息
     *
     * @param taskDto  task信息
     * @param taskType task类型
     * @return 返回接口task保存结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<Task> save(TaskDto taskDto, Integer taskType) {
        StringBuilder testCommand = new StringBuilder();
        AddTaskDto testTask = taskDto.getTestTask();
        List<Integer> caseIdList = taskDto.getCaseIdList();

        log.info("新增任务数据：{}", testTask);
        Jenkins queryJenkins = new Jenkins();
        queryJenkins.setId(testTask.getTestJenkinsId());
        queryJenkins.setCreateUserId(testTask.getCreateUserId());
        Jenkins result = jenkinsMapper.selectOne(queryJenkins);
        if (Objects.isNull(result)) {
            return ResultDto.fail("Jenkins信息为空");
        }
        List<Cases> casesList = caseMapper.selectByIds(StrUtils.list2IdsStr(caseIdList));

        // 组装测试命令
        makeTestCommand(testCommand, result, casesList);

        Task task = new Task();
        task.setName(testTask.getName());
        task.setTestJenkinsId(testTask.getTestJenkinsId());
        task.setCreateUserId(testTask.getCreateUserId());
        task.setRemark(testTask.getRemark());
        task.setTaskType(taskType);
        task.setTestCommand(testCommand.toString());
        task.setCaseCount(caseIdList.size());
        task.setStatus(Constants.STATUS_ONE);
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        taskMapper.insert(task);

        if (caseIdList.size() > 0) {
            List<TaskCaseRel> testTaskCaseList = new ArrayList<>();
            for (Integer testCaseId : caseIdList) {
                TaskCaseRel taskCaseRel = new TaskCaseRel();
                taskCaseRel.setTaskId(task.getId());
                taskCaseRel.setCaseId(testCaseId);
                taskCaseRel.setCreateUserId(task.getCreateUserId());
                taskCaseRel.setCreateTime(new Date());
                taskCaseRel.setUpdateTime(new Date());
                testTaskCaseList.add(taskCaseRel);
            }
            log.info("=====测试任务详情保存-落库入参====：" + JSONUtil.toJsonStr(testTaskCaseList));
            taskCaseRelMapper.insertList(testTaskCaseList);
        }
        return ResultDto.success("成功", task);
    }

    /**
     * 删除测试任务信息
     *
     * @param taskId       taskId主键
     * @param createUserId 创建人用户id
     * @return 返回接口task删除结果
     */
    @Override
    public ResultDto<Task> delete(Integer taskId, Integer createUserId) {
        Task queryTask = new Task();
        queryTask.setId(taskId);
        queryTask.setCreateUserId(createUserId);

        Task result = taskMapper.selectOne(queryTask);
        // 如果为空，则提示，也可以直接返回成功
        if (Objects.isNull(result)) {
            return ResultDto.fail("未查到测试任务信息");
        }
        taskMapper.deleteByPrimaryKey(taskId);
        return ResultDto.success("成功");
    }

    /**
     * 修改测试任务信息
     *
     * @param task task信息
     * @return 返回接口task更新结果
     */
    @Override
    public ResultDto<Task> update(Task task) {
        Task queryTask = new Task();
        queryTask.setId(task.getId());
        queryTask.setCreateUserId(task.getCreateUserId());

        Task result = taskMapper.selectOne(queryTask);
        //如果为空，则提示，也可以直接返回成功
        if (Objects.isNull(result)) {
            return ResultDto.fail("未查到测试任务信息");
        }
        result.setUpdateTime(new Date());
        result.setName(task.getName());
        result.setRemark(task.getRemark());

        taskMapper.updateByPrimaryKeySelective(result);
        return ResultDto.success("成功");
    }

    /**
     * 根据id查询
     *
     * @param taskId       askId主键
     * @param createUserId 创建人用户id
     * @return 返回接口task查询结果
     */
    @Override
    public ResultDto<Task> getById(Integer taskId, Integer createUserId) {
        Task queryTask = new Task();
        queryTask.setId(taskId);
        queryTask.setCreateUserId(createUserId);

        Task result = taskMapper.selectOne(queryTask);
        //如果为空，则提示，也可以直接返回成功
        if (Objects.isNull(result)) {
            ResultDto.fail("未查到测试任务信息");
        }
        return ResultDto.success("成功", result);
    }

    /**
     * 查询测试任务信息列表
     *
     * @param pageTableRequest 分页查询
     * @return 返回接口task分页查询结果
     */
    @Override
    public ResultDto<PageTableResponse<Task>> list(PageTableRequest<QueryTaskListDto> pageTableRequest) {
        QueryTaskListDto params = pageTableRequest.getParams();
        Integer pageNum = pageTableRequest.getPageNum();
        Integer pageSize = pageTableRequest.getPageSize();
        //总数
        Integer recordsTotal = taskMapper.count(params);
        //分页查询数据
        List<Task> hogwartsTestJenkinsList = taskMapper.list(params, (pageNum - 1) * pageSize, pageSize);
        PageTableResponse<Task> hogwartsTestJenkinsPageTableResponse = new PageTableResponse<>();
        hogwartsTestJenkinsPageTableResponse.setRecordsTotal(recordsTotal);
        hogwartsTestJenkinsPageTableResponse.setData(hogwartsTestJenkinsList);
        return ResultDto.success("成功", hogwartsTestJenkinsPageTableResponse);
    }

    /**
     * 开始执行测试任务信息
     *
     * @param tokenDto       token信息
     * @param requestInfoDto 接口请求信息
     * @param task           任务信息
     * @return 返回执行结果
     * @throws IOException io异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<String> startTask(TokenDto tokenDto, RequestInfoDto requestInfoDto, Task task) throws IOException {
        log.info("测试任务执行-Service请求参数：{}, 测试任务请求参数：{}", JSONUtil.toJsonStr(requestInfoDto), JSONUtil.toJsonStr(task));
        if (Objects.isNull(task)) {
            return ResultDto.fail("测试任务参数不能为空");
        }
        Integer defaultJenkinsId = tokenDto.getDefaultJenkinsId();
        if (Objects.isNull(defaultJenkinsId)) {
            return ResultDto.fail("未配置默认Jenkins");
        }
        Jenkins queryJenkins = new Jenkins();
        queryJenkins.setId(defaultJenkinsId);
        queryJenkins.setCreateUserId(tokenDto.getUserId());
        Jenkins resultJenkins = jenkinsMapper.selectOne(queryJenkins);
        if (Objects.isNull(resultJenkins)) {
            return ResultDto.fail("默认Jenkins不存在或已失效");
        }
        Task queryTask = new Task();
        queryTask.setId(task.getId());
        queryTask.setCreateUserId(task.getCreateUserId());
        Task resultTask = taskMapper.selectOne(queryTask);
        if (Objects.isNull(resultTask)) {
            String tips = "未查到测试任务";
            log.error(tips + resultTask.getId());
            return ResultDto.fail(tips);
        }
        String testCommandStr = task.getTestCommand();
        if (ObjectUtils.isEmpty(testCommandStr)) {
            testCommandStr = resultTask.getTestCommand();
        }
        if (ObjectUtils.isEmpty(testCommandStr)) {
            return ResultDto.fail("任务的测试命令不能为空");
        }
        //更新任务状态
        resultTask.setStatus(Constants.STATUS_TWO);
        taskMapper.updateByPrimaryKeySelective(resultTask);

        StringBuilder testCommand = new StringBuilder();
        //添加保存测试任务接口拼装的mvn test 命令
        testCommand.append(testCommandStr);
        testCommand.append(" \n");
        StringBuilder updateStatusUrl = JenkinsUtils.getUpdateTaskStatusUrl(requestInfoDto, resultTask);
        //构建参数组装
        Map<String, String> params = new HashMap<>();
        params.put("baseUrl", requestInfoDto.getBaseUrl());
        params.put("token", requestInfoDto.getToken());
        params.put("testCommand", testCommand.toString());
        params.put("updateStatusData", updateStatusUrl.toString());

        log.info("=====执行测试Job的构建参数组装====：" + JSONUtil.toJsonStr(params));
        log.info("=====执行测试Job的修改任务状态的数据组装====：" + updateStatusUrl);

        OperateJenkinsJobDto operateJenkinsJobDto = new OperateJenkinsJobDto();
        operateJenkinsJobDto.setParams(params);
        operateJenkinsJobDto.setTokenDto(tokenDto);
        operateJenkinsJobDto.setJenkins(resultJenkins);
        operateJenkinsJobDto.setParams(params);

        ResultDto<String> resultDto = jenkinsClient.operateJenkinsJob(operateJenkinsJobDto);
        //此处抛出异常，阻止事务提交
        if (0 == resultDto.getResultCode()) {
            throw new ServiceException("执行测试时异常-" + resultDto.getMessage());
        }
        return resultDto;
    }

    /**
     * 修改测试任务状态信息
     *
     * @param task task信息
     * @return 返回接口task修改状态结果
     */
    @Override
    public ResultDto<Task> updateStatus(Task task) {
        Task queryTask = new Task();
        queryTask.setId(task.getId());
        queryTask.setCreateUserId(task.getCreateUserId());
        Task result = taskMapper.selectOne(queryTask);
        //如果为空，则提示
        if (Objects.isNull(result)) {
            return ResultDto.fail("未查到测试任务信息");
        }
        //如果任务已经完成，则不重复修改
        if (Constants.STATUS_THREE.equals(result.getStatus())) {
            return ResultDto.fail("测试任务已完成，无需修改");
        }
        result.setUpdateTime(new Date());
        //仅状态为已完成时修改
        if (Constants.STATUS_THREE.equals(task.getStatus())) {
            result.setBuildUrl(task.getBuildUrl());
            result.setStatus(Constants.STATUS_THREE);
            taskMapper.updateByPrimaryKey(result);
        }
        return ResultDto.success("成功");
    }

    /**
     * 组装测试命令
     *
     * @param testCommand
     * @param jenkins
     * @param testCaseList
     */
    private void makeTestCommand(StringBuilder testCommand, Jenkins jenkins, List<Cases> testCaseList) {
        //打印测试目录
        testCommand.append("pwd");
        testCommand.append("\n");
        if (Objects.isNull(jenkins)) {
            throw new ServiceException("组装测试命令时，Jenkins信息为空");
        }
        if (Objects.isNull(testCaseList) || testCaseList.size() == 0) {
            throw new ServiceException("组装测试命令时，测试用例列表信息为空");
        }
        String runCommand = jenkins.getTestCommand();

        Integer commandRunCaseType = jenkins.getCommandRunCaseType();
        String systemTestCommand = jenkins.getTestCommand();

        if (ObjectUtils.isEmpty(systemTestCommand)) {
            throw new ServiceException("组装测试命令时，运行的测试命令信息为空");
        }
        //默认文本类型
        if (Objects.isNull(commandRunCaseType)) {
            commandRunCaseType = 1;
        }
        //文本类型
        if (commandRunCaseType == 1) {
            for (Cases cases : testCaseList) {
                //拼装命令前缀
                testCommand.append(systemTestCommand).append(" ");
                //拼装测试数据
                testCommand.append(cases.getCaseData())
                        .append("\n");
            }
        }
        //文件类型
        if (commandRunCaseType == 2) {
            String commandRunCaseSuffix = jenkins.getCommandRunCaseSuffix();
            if (ObjectUtils.isEmpty(commandRunCaseSuffix)) {
                throw new ServiceException("组装测试命令且case为文件时，测试用例后缀名不能为空");
            }
            for (Cases cases : testCaseList) {
                //拼装下载文件的curl命令
                makeCurlCommand(testCommand, cases, commandRunCaseSuffix);
                testCommand.append("\n");
                //拼装命令前缀
                testCommand.append(systemTestCommand).append(" ");
                //平台测试用例名称
                testCommand.append(cases.getCaseName())
                        //拼装.分隔符
                        .append(".")
                        //拼装case文件后缀
                        .append(commandRunCaseSuffix)
                        .append(" || true")
                        .append("\n");
            }
        }
        log.info("testCommand.toString()== " + testCommand.toString() + "  runCommand== " + runCommand);
        testCommand.append("\n");
    }

    /**
     * 拼装下载文件的curl命令
     *
     * @param testCommand
     * @param cases
     * @param commandRunCaseSuffix
     */
    private void makeCurlCommand(StringBuilder testCommand, Cases cases, String commandRunCaseSuffix) {
        //通过curl命令获取测试数据并保存为文件
        testCommand.append("curl ")
                .append("-o ");
        String caseName = cases.getCaseName();
        if (ObjectUtils.isEmpty(caseName)) {
            caseName = "测试用例无测试名称";
        }
        testCommand.append(caseName)
                .append(".")
                .append(commandRunCaseSuffix)
                .append(" ${baseUrl}/testCase/data/")
                .append(cases.getId())
                .append(" -H \"token: ${token}\" ");
        //本行命令执行失败，继续运行下面的命令行
        testCommand.append(" || true");
        testCommand.append("\n");
    }
}
