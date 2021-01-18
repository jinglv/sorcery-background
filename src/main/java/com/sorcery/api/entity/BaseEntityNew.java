package com.sorcery.api.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 所有entity需要继承的类
 *
 * @author jingLv
 * @date 2021/01/14
 */
@Data
public abstract class BaseEntityNew implements Serializable {

    private static final long serialVersionUID = -2760917746906030230L;
}
