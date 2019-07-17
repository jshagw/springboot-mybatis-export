package com.example.demo.controllers;

import com.example.beans.DialogParam;
import com.example.demo.mybatis.mapper.TestUserMapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import mytest.hello.Hello;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/HelloWorld")
public class HelloWorld {
    @Autowired
    private TestUserMapper testUserMapper;

    @GetMapping(value = "/{name}")
    @ApiOperation(value = "hello", notes = "say hello")
    @ApiImplicitParams({
            @ApiImplicitParam(name="name",value = "名称",required = true)
    })
    public String hello(@PathVariable("name") String name) {
        log.info(name);
        log.debug(name);
        log.info("id = 1, L1 = " + testUserMapper.selectByPrimaryKey(1).getL1());
        return new Hello().say(name + testUserMapper.selectByPrimaryKey(1).getL1());
    }

    @PostMapping(value = "/{name}")
    @ApiOperation(value = "dialog", notes = "test dialog")
    @ApiImplicitParams({
            @ApiImplicitParam(name="name",value = "名称",required = true),
            @ApiImplicitParam(name="body",value = "对话内容",required = true, dataType = "DialogParam")
    })
    public @ResponseBody DialogParam wHello(@PathVariable("name") String name, @RequestBody DialogParam body) {
        body.setSex("男");
        body.setName(name);
        return body;
    }
}
