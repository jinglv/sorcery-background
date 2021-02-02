package com.sorcery.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

/**
 * @author jinglv
 * @date 2020/01/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Table(name = "test_task_case_rel")
public class TaskCaseRel extends BaseEntityNew {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 创建人id
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 任务id
     */
    @Column(name = "task_id")
    private Integer taskId;

    /**
     * 用例id
     */
    @Column(name = "case_id")
    private Integer caseId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;
}