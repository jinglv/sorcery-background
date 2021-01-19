package com.sorcery.api.dao;

import com.sorcery.api.common.MySqlExtensionMapper;
import com.sorcery.api.entity.Jenkins;
import org.springframework.stereotype.Repository;

/**
 * @author jinglv
 * @date 2021/01/19
 */
@Repository
public interface JenkinsMapper extends MySqlExtensionMapper<Jenkins> {
}