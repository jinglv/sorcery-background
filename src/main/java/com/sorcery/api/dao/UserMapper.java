package com.sorcery.api.dao;

import com.sorcery.api.common.MySqlExtensionMapper;
import com.sorcery.api.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @author jinglv
 * @date 2021/01/18
 */
@Repository
public interface UserMapper extends MySqlExtensionMapper<User> {
}