package com.sorcery.api.service;

import com.sorcery.api.dto.ResultDto;
import com.sorcery.api.dto.cases.TaskCaseRelDetailDto;
import com.sorcery.api.dto.cases.TaskCaseRelListDto;
import com.sorcery.api.dto.page.PageTableRequest;
import com.sorcery.api.dto.page.PageTableResponse;

/**
 * @author jingLv
 * @date 2021/01/19
 */
public interface TaskCaseRelService {
    /**
     * 查询任务关联的详细信息列表
     *
     * @param pageTableRequest 分页查询
     * @return 返回接口测试任务详细信息
     */
    ResultDto<PageTableResponse<TaskCaseRelDetailDto>> listDetail(PageTableRequest<TaskCaseRelListDto> pageTableRequest);

}
