package com.sorcery.api.dao;

import com.sorcery.api.common.MySqlExtensionMapper;
import com.sorcery.api.entity.Task;
import org.springframework.stereotype.Repository;

/**
 * @author jinglv
 * @date 2021/01/19
 */
@Repository
public interface TaskMapper extends MySqlExtensionMapper<Task> {
}