package com.sorcery.api.dto.page;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应对象，分页查询返回
 *
 * @author jingLv
 * @date 2021/01/13
 */
@Data
public class PageTableResponse<T> implements Serializable {

    private static final long serialVersionUID = -7622061179624706105L;

    /**
     * 返回结果记录总数
     */
    private Integer recordsTotal;
    /**
     * 响应结果数据
     */
    private List<T> data;
}
