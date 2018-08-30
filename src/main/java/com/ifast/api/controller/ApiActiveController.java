package com.ifast.api.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.ifast.api.pojo.domain.ActiveDO;
import com.ifast.api.pojo.domain.ActiveUserDO;
import com.ifast.api.pojo.domain.ImgDO;
import com.ifast.api.service.ApiActiveService;
import com.ifast.api.service.ActiveUserService;
import com.ifast.api.service.ImgService;
import com.ifast.common.utils.Result;
import com.ifast.domain.ApiUserDO;
import com.ifast.service.ProductService;
import com.ifast.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yfj
 */
@RestController
@RequestMapping("/api/active/")
public class ApiActiveController {

    @Autowired
    private ApiActiveService apiActiveService;

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
            , ActiveDO activeDO) {
        ApiUserDO userDO = userService.getUserByToken(token);
        activeDO.setCreateUserId(userDO.getId());
        Page<ActiveDO> page = this.apiActiveService.active(activeDO);
        return Result.ok(page);
    }

    @PostMapping("/joinActive")
    @ApiOperation("加入活动")
    public Result<?> joinActive(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token
            , Long activeId) {
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
            , Long activeId) {
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
            , ActiveDO activeDO) {
        this.apiActiveService.insert(activeDO);
        Long parentId = activeDO.getId();
        activeDO.setStop("0");
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
