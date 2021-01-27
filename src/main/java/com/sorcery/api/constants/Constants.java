package com.sorcery.api.constants;

/**
 * 通用相关常量
 *
 * @author jingLv
 * @date 2021/01/18
 */
public interface Constants {
    /**
     * 未删除
     */
    Integer DEL_FLAG_ONE = 1;

    /**
     * 已删除
     */
    Integer DEL_FLAG_ZERO = 0;

    /**
     * 0 无效
     */
    Integer STATUS_ZERO = 0;

    /**
     * 1 新建
     */
    Integer STATUS_ONE = 1;

    /**
     * 2 执行中
     */
    Integer STATUS_TWO = 2;

    /**
     * 3 执行完成
     */
    Integer STATUS_THREE = 3;

    /**
     * 1 任务类型 普通测试任务
     */
    Integer TASK_TYPE_ONE = 1;

    /**
     * 2 任务类型 一键执行测试任务
     */
    Integer TASK_TYPE_TWO = 2;
}
