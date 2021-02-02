package com.sorcery.api.common.utils;

import cn.hutool.json.JSONObject;
import com.sorcery.api.constants.Constants;
import com.sorcery.api.dto.RequestInfoDTO;
import com.sorcery.api.entity.Task;
import org.springframework.util.ObjectUtils;

/**
 * Jenkins工具类
 *
 * @author jingLv
 * @date 2021/01/25
 */
public class JenkinsUtils {

    /**
     * 获取执行测试的Job名称（resource/jenkins目录下已配置执行的Jenkins配置xml文件）
     *
     * @return 返回拼接完成的Jenkins名称
     */

    public static String getStartTestJobName(Integer createUserId) {
        return "test_" + createUserId;
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

    public static StringBuilder getUpdateTaskStatusUrl(RequestInfoDTO requestInfoDto, Task task) {
        StringBuilder updateStatusUrl = new StringBuilder();
        updateStatusUrl.append("curl -X PUT ");
        updateStatusUrl.append("\"").append(requestInfoDto.getBaseUrl()).append("/task/status \" ");
        updateStatusUrl.append("-H \"Content-Type: application/json \" ");
        updateStatusUrl.append("-H \"token: ").append(requestInfoDto.getToken()).append("\" ");
        updateStatusUrl.append("-d ");

        JSONObject json = new JSONObject();
        json.set("taskId", task.getId());
        json.set("status", Constants.STATUS_THREE);
        json.set("buildUrl", "${BUILD_URL}");

        updateStatusUrl.append("'").append(json.toString()).append("'");
        return updateStatusUrl;
    }
}
