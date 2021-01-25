package com.sorcery.api.common.utils;

import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

/**
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
     * @param caseIdList
     * @return
     */
    public static String list2IdsStr(List<Integer> caseIdList) {
        if (Objects.isNull(caseIdList)) {
            return null;
        }
        return caseIdList.toString()
                .replace("[", "")
                .replace("]", "");

    }

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
