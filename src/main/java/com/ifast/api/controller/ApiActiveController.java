package com.ifast.api.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.ifast.api.pojo.domain.ActiveDO;
import com.ifast.api.pojo.domain.ActiveUserDO;
import com.ifast.api.pojo.domain.ImgDO;
import com.ifast.api.service.ActiveService;
import com.ifast.api.service.ActiveUserService;
import com.ifast.api.service.ImgService;
import com.ifast.common.utils.Result;
import com.ifast.domain.ApiUserDO;
import com.ifast.domain.AttentionDO;
import com.ifast.domain.ProductDO;
import com.ifast.domain.UnitDO;
import com.ifast.service.AttentionService;
import com.ifast.service.ProductService;
import com.ifast.service.UnitService;
import com.ifast.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yfj
 */
@RestController
@RequestMapping("/api/active/")
public class ApiActiveController {

    @Autowired
    private ActiveService activeService;

    @Autowired
    private ActiveUserService activeUserService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ImgService imgService;

    @Resource(name = "apiService")
    private UserService userService;

    @PostMapping("/activeListPage")
    @ApiOperation("查询活动信息")
    public Result<?> updateUser(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token
            , @ApiParam(name = "Active")ActiveDO activeDO) {
        ApiUserDO userDO = userService.getUserByToken(token);
        activeDO.setCreateUserId(userDO.getId());
        Page<ActiveDO> page = this.activeService.active(activeDO);
        return Result.ok(page);
    }

    @PostMapping("/joinActive")
    @ApiOperation("加入活动")
    public Result<?> joinActive(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token
            , @ApiParam(name = "activeId")Long activeId) {
        ApiUserDO userDO = userService.getUserByToken(token);
        ActiveUserDO  activeUserDO = new ActiveUserDO();
        activeUserDO.setUserId(userDO.getId());
        activeUserDO.setActiveId(activeId);
        this.activeUserService.insert(activeUserDO);
        return Result.ok();
    }

    @PostMapping("/activeUserList")
    @ApiOperation("查询参加活动人员列表")
    public Result<?> activeUserList(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token
            , @ApiParam(name = "activeId")Long activeId) {
        Wrapper<ActiveUserDO> wrapper = new EntityWrapper<>();
        wrapper.eq("activeId", activeId);
        List<ActiveUserDO> list = this.activeUserService.selectList(wrapper);
        List<Long> params = new ArrayList<>();
        for (ActiveUserDO activeUserDO : list) {
            params.add(activeUserDO.getUserId());
        }
        List<ApiUserDO> users = this.userService.selectBatchIds(params);
        return Result.ok(users);
    }

    @PostMapping("createActive")
    @ApiOperation("创建活动")
    @Transactional
    public Result<?> createActive(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token
            , @ApiParam(name = "activeDO")ActiveDO activeDO) {
        this.activeService.insert(activeDO);
        Long parentId = activeDO.getId();
        List<Long> imgids = activeDO.getImgIds();
        List<ImgDO> imgs = Lists.newArrayList();
        for (Long id : imgids) {
            ImgDO imgDO= new ImgDO();
            imgDO.setId(id);
            imgDO.setParentId(parentId);
            imgs.add(imgDO);
        }
        this.imgService.updateBatchById(imgs);
        return Result.ok();
    }

    @PostMapping("/uploadFile")
    @ApiOperation("上传图片")
    public Result<?> uploadFile(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token
            , @RequestParam ("poster") MultipartFile file) {
        String url=productService.upload(file);
        ImgDO imgDO = new ImgDO();
        imgDO.setUrl(url);
//        imgService.addImg(imgDO);
//        Long id = imgDO.getId();
        imgService.insert(imgDO);
        return Result.ok(imgDO);
    }




}
