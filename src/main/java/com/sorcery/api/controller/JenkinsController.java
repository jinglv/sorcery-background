package com.sorcery.api.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jingLv
 * @date 2021/01/19
 */
@Slf4j
@Api(tags = "Jenkins管理")
@RestController
@RequestMapping("/jenkins")
public class JenkinsController {
}