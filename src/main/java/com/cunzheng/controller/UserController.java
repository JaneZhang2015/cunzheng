package com.cunzheng.controller;

import com.cunzheng.configuration.servie.UserService;
import com.cunzheng.contract.BlockUtil;
import com.cunzheng.contract.CunZhengContract;
import com.cunzheng.entity.UserRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/user")
@Slf4j
@Api(value = "用户管理", description = "电子存证用户管理模块功能")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CunZhengContract cunZhengContract;

    @PostMapping("/saveUser")
    @ApiOperation(value = "用户注册", notes = "用户注册")
    public String saveUser(
            @RequestParam String userName,
            @RequestParam String password,
            @RequestParam UserRole userRole
    ) throws Exception {

        boolean userNameExists = userService.hasUserNameCreated(userName);
        if (userNameExists) {
            return "Username already exists";
        }

        String accountJson = BlockUtil.newAccountSM2(password);

        cunZhengContract.saveUser(JSONObject.fromObject(accountJson).getString("address"), userName, userRole.getCode());
        userService.saveUser(userName, password, userRole, accountJson);

        return "success";
    }
}
