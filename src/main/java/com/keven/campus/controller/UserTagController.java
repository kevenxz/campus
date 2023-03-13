package com.keven.campus.controller;

import com.keven.campus.service.UserTagService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Keven
 * @version 1.0
 */
@RestController
@RequestMapping("/userTag")
@Api(value = "当前用户的标签")
public class UserTagController {

    @Autowired
    private UserTagService userTagService;
}
