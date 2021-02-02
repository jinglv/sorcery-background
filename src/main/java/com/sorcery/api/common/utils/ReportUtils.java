package com.sorcery.api.common.utils;

import com.sorcery.api.entity.Jenkins;
import org.springframework.util.ObjectUtils;

/**
 * 获取Allure测试报告工具类
 *
 * @author jingLv
 * @date 2021/01/25
 */
public class ReportUtils {

    /**
     * 获取allure报告地址
     *
     * @param buildUrl             Jenkins Url
     * @param jenkins              Jenkins信息
     * @param autoLoginJenkinsFlag 是否登录
     * @return 返回Allure报告url
     */
    public static String getAllureReportUrl(String buildUrl, Jenkins jenkins, boolean autoLoginJenkinsFlag) {
        if (ObjectUtils.isEmpty(buildUrl) || !buildUrl.contains("/job")) {
            return buildUrl;
        }
        String allureReportUrl = buildUrl;
        if (autoLoginJenkinsFlag) {
            allureReportUrl = getAllureReportUrlAndLogin(buildUrl, jenkins);
        }
        return allureReportUrl + "allure/";
    }

    /**
     * 获取可以自动登录Jenkins的allure报告
     *
     * @param buildUrl Jenkins Url
     * @param jenkins  Jenkins信息
     * @return 返回allure报告地址
     */
    private static String getAllureReportUrlAndLogin(String buildUrl, Jenkins jenkins) {
        String allureReportUrl;
        String allureReportBaseUrl = jenkins.getUrl() + "j_acegi_security_check?j_username=" + jenkins.getJenkinsUsername()
                + "&j_password=" + jenkins.getJenkinsPassword() + "&Submit=登录&remember_me=on" + "&from=";
        allureReportUrl = allureReportBaseUrl + buildUrl.substring(buildUrl.indexOf("/job"));
        return allureReportUrl;
    }
}
