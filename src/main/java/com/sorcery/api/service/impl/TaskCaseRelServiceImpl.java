package com.sorcery.api.service.impl;

import com.sorcery.api.dao.TaskCaseRelMapper;
import com.sorcery.api.dto.ResultDto;
import com.sorcery.api.dto.cases.TaskCaseRelDetailDto;
import com.sorcery.api.dto.cases.TaskCaseRelListDto;
import com.sorcery.api.dto.page.PageTableRequest;
import com.sorcery.api.dto.page.PageTableResponse;
import com.sorcery.api.service.TaskCaseRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jingLv
 * @date 2021/01/22
 */
@Slf4j
@Service
public class TaskCaseRelServiceImpl implements TaskCaseRelService {

    private final TaskCaseRelMapper taskCaseRelMapper;

    public TaskCaseRelServiceImpl(TaskCaseRelMapper taskCaseRelMapper) {
        this.taskCaseRelMapper = taskCaseRelMapper;
    }

    /**
     * 查询任务关联的详细信息列表
     *
     * @param pageTableRequest 分页查询
     * @return 返回接口测试任务详细信息
     */
    @Override
    public ResultDto<PageTableResponse<TaskCaseRelDetailDto>> listDetail(PageTableRequest<TaskCaseRelListDto> pageTableRequest) {
        TaskCaseRelListDto params = pageTableRequest.getParams();
        List<TaskCaseRelDetailDto> taskCaseRelListDtoList = taskCaseRelMapper.listDetail(params, pageTableRequest.getPageNum(), pageTableRequest.getPageSize());
        PageTableResponse<TaskCaseRelDetailDto> taskCaseRelDetailDtoPageTableResponse = new PageTableResponse<>();
        taskCaseRelDetailDtoPageTableResponse.setData(taskCaseRelListDtoList);
        return ResultDto.success("成功", taskCaseRelDetailDtoPageTableResponse);
    }
}
