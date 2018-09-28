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
import com.ifast.api.util.JWTUtil;
import com.ifast.common.utils.Result;
import com.ifast.domain.ApiUserDO;
import com.ifast.service.ProductService;
import com.ifast.service.UserService;
import io.jsonwebtoken.Jwt;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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
        String userId=JWTUtil.getUserId(token);
        Wrapper<ActiveUserDO> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", userId).eq("activeId", activeId);
        ActiveUserDO old = this.activeUserService.selectOne(wrapper);
        if (old != null) {
            return Result.build(1, "已经加入活动");
        }
        ActiveUserDO  activeUserDO = new ActiveUserDO();
        activeUserDO.setUserId(Long.parseLong(userId));
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
        if (list != null && list.size() > 0) {
            List<Long> params = new ArrayList<>();
            for (ActiveUserDO activeUserDO : list) {
                params.add(activeUserDO.getUserId());
            }
            List<ApiUserDO> users = this.userService.selectBatchIds(params);
            return Result.ok(users);
        }
        return Result.ok();
    }

    @PostMapping("createActive")
    @ApiOperation("创建活动")
    @Transactional
    public Result<?> createActive(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token
            , ActiveDO activeDO) {
        activeDO.setStop("0");
        activeDO.setCreateUserId(Long.parseLong(JWTUtil.getUserId(token)));
        if(activeDO.getId()!=null){
            this.apiActiveService.updateById(activeDO);
        }else{
            this.apiActiveService.insert(activeDO);
        }
        Long parentId = activeDO.getId();
        activeDO.setStop("0");
        List<Long> imgids = activeDO.getImgIds();
        List<ImgDO> imgs = Lists.newArrayList();
        if(imgids!=null&&imgids.size()>0){
            for (Long id : imgids) {
                ImgDO imgDO= new ImgDO();
                imgDO.setId(id);
                imgDO.setParentId(parentId);
                imgs.add(imgDO);
            }
            this.imgService.updateBatchById(imgs);
        }

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

    @PostMapping("/delFile")
    @ApiOperation("删除图片")
    public Result<?> delFile(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token
            , String imgId) {
        Wrapper<ImgDO> wrapper = new EntityWrapper<>();
        wrapper.eq("id", imgId);
        this.imgService.delete(wrapper);

        return Result.ok();
    }

    @PostMapping("/activeDetail")
    @ApiOperation("活动详情")
    public Result<?> aciveDetail(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token,Long activeId){
        ActiveDO activeDO = this.apiActiveService.selectById(activeId);
        if (activeDO != null) {
            List<ImgDO> imgs = new ArrayList<>();
            Wrapper<ImgDO> wrapper = new EntityWrapper<>();
            wrapper.eq("parentId", activeId);
            imgs = this.imgService.selectList(wrapper);
            activeDO.setImgs(imgs);
            Wrapper<ActiveUserDO> activeUserDOWrapper = new EntityWrapper<>();
            activeUserDOWrapper.eq("activeId", activeDO.getId());
            int total =this.activeUserService.selectCount(activeUserDOWrapper);
            activeDO.setUserTotal(total);
            return Result.ok(activeDO);
        }
        Result result = new Result();
        result.setCode(-1);
        result.setMsg("查不到数据");
        return result;
    }

    @PostMapping("/delDetail")
    @ApiOperation("删除活动")
    @Transactional
    public Result<?> delDetail(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token,Long activeId){
        ActiveDO activeDO = this.apiActiveService.selectById(activeId);
        if (activeDO != null) {
            this.apiActiveService.deleteById(activeId);
            Wrapper<ImgDO> wrapper = new EntityWrapper<>();
            wrapper.eq("parentId", activeId);
            List<ImgDO> imgs = this.imgService.selectList(wrapper);
            List<Long> ids = new ArrayList<>();

            for (ImgDO imgDO : imgs) {
                ids.add(imgDO.getId());
            }
            this.imgService.deleteBatchIds(ids);
            return Result.ok();
        }

        return Result.fail();
    }




}
