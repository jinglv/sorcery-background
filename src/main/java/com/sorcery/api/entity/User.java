package com.sorcery.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户实体类
 *
 * @author jinglv
 * @date 2020/01/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Table(name = "test_user")
public class User extends BaseEntityNew {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 自动生成用例job名称，不为空时表示已经创建job
     */
    @Column(name = "auto_create_case_job_name")
    private String autoCreateCaseJobName;

    /**
     * 执行测试job，不为空时表示已经创建job
     */
    @Column(name = "start_test_job_name")
    private String startTestJobName;

    /**
     * 默认Jenkins服务器
     */
    @Column(name = "default_jenkins_id")
    private Integer defaultJenkinsId;

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