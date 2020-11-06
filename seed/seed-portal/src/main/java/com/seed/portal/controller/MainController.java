package com.seed.portal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Main pages controller.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 18:14
 */
@Slf4j
@Controller
@Api(tags = "Main Pages")
public class MainController {

    @ApiOperation("Home Page")
    @GetMapping(value = {"/index", "/home"})
    public String index() {
        return "index";
    }

    @ApiOperation("Login Page")
    @GetMapping(value = {"/login"})
    public String login() {
        return "login";
    }

    @ApiOperation("Login Fail Page")
    @GetMapping(value = {"/login_fail"})
    public String loginFail() {
        return "login-fail";
    }

}
