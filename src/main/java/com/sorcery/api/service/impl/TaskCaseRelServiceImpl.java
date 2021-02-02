package com.sorcery.api.service.impl;

import com.sorcery.api.dao.TaskCaseRelMapper;
import com.sorcery.api.dto.ResultDTO;
import com.sorcery.api.dto.cases.TaskCaseRelDetailDTO;
import com.sorcery.api.dto.cases.TaskCaseRelListDTO;
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
    public ResultDTO<PageTableResponse<TaskCaseRelDetailDTO>> listDetail(PageTableRequest<TaskCaseRelListDTO> pageTableRequest) {
        TaskCaseRelListDTO params = pageTableRequest.getParams();
        List<TaskCaseRelDetailDTO> taskCaseRelListDtoList = taskCaseRelMapper.listDetail(params, pageTableRequest.getPageNum(), pageTableRequest.getPageSize());
        PageTableResponse<TaskCaseRelDetailDTO> taskCaseRelDetailDtoPageTableResponse = new PageTableResponse<>();
        taskCaseRelDetailDtoPageTableResponse.setData(taskCaseRelListDtoList);
        return ResultDTO.success("成功", taskCaseRelDetailDtoPageTableResponse);
    }
}
