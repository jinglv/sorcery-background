package com.sorcery.api.common.utils;

import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

/**
 * 数据转换工具类
 *
 * @author jingLv
 * @date 2021/01/25
 */
public class StrUtils {
    /**
     * 将存储id的list转为字符串
     * <p>
     * 转换前=[2, 12, 22, 32]
     * 转换后= 2, 12, 22, 32
     *
     * @param caseIdList 传入测试用例id数组
     * @return 返回数组转换的String
     */
    public static String list2IdsStr(List<Integer> caseIdList) {
        if (Objects.isNull(caseIdList)) {
            return null;
        }
        return caseIdList.toString()
                .replace("[", "")
                .replace("]", "");

    }

    /**
     * URL与请求端口Port拼接
     *
     * @param requestUrl 基础请求的Url
     * @return 返回拼接的字符串
     */
    public static String getHostAndPort(String requestUrl) {
        if (ObjectUtils.isEmpty(requestUrl)) {
            return "";
        }
        String http = "";
        String tempUrl = "";
        if (requestUrl.contains("://")) {
            http = requestUrl.substring(0, requestUrl.indexOf("://") + 3);
            tempUrl = requestUrl.substring(requestUrl.indexOf("://") + 3);
        }
        if (tempUrl.contains("/")) {
            tempUrl = tempUrl.substring(0, tempUrl.indexOf("/"));
        }
        return http + tempUrl;
    }
}
