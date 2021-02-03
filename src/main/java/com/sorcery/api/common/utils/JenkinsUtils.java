package com.sorcery.api.common.utils;

import cn.hutool.json.JSONObject;
import com.sorcery.api.constants.Constants;
import com.sorcery.api.dto.RequestInfoDTO;
import com.sorcery.api.entity.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

/**
 * Jenkins工具类
 *
 * @author jingLv
 * @date 2021/01/25
 */
@Slf4j
public class JenkinsUtils {

    /**
     * 获取执行测试的Job名称
     * 拼接Job名称规则：RESTFulApiTest_1（创建的用户Id），如果用户start_test_job_name为null，则会根据该规则拼接Job名称，并保存
     *
     * @return 返回拼接完成的Jenkins名称
     */

    public static String getStartTestJobName(Integer createUserId) {
        return "RESTFulApiTest_" + createUserId;
    }

    /**
     * 获取执行测试的Job名称
     *
     * @return 返回获取的Jenkins的Job名称
     */
    public static String getJobSignByName(String jobName) {
        if (ObjectUtils.isEmpty(jobName) || !jobName.contains("_")) {
            return "";
        }
        return jobName.substring(0, jobName.lastIndexOf("_"));
    }

    /**
     * Jenkins回调(拼接执行任务修改状态接口)，更新执行测试任务状态
     *
     * @param requestInfoDto 请求信息
     * @param task           测试任务信息
     * @return 返回结果
     */
    public static StringBuilder getUpdateTaskStatusUrl(RequestInfoDTO requestInfoDto, Task task) {
        StringBuilder updateStatusUrl = new StringBuilder();
        updateStatusUrl.append("curl -X PUT ");
        updateStatusUrl.append("\"").append(requestInfoDto.getBaseUrl()).append("/task/status \" ");
        updateStatusUrl.append("-H \"Content-Type: application/json \" ");
        updateStatusUrl.append("-H \"token: ").append(requestInfoDto.getToken()).append("\" ");
        updateStatusUrl.append("-d ");

        JSONObject json = new JSONObject();
        json.set("taskId", task.getId());
        // 更新任务执行状态：执行完成
        json.set("status", Constants.STATUS_THREE);
        // 获取Jenkins中的构建地址
        json.set("buildUrl", "${BUILD_URL}");
        updateStatusUrl.append("'").append(json.toString()).append("'");
        log.info("返回更新状态的Url：{}", updateStatusUrl);
        return updateStatusUrl;
    }
}
