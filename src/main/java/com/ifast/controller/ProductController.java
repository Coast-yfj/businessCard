package com.ifast.controller;


import java.util.Arrays;
import java.util.Map;

import com.ifast.api.pojo.domain.ImgDO;
import com.ifast.api.service.ImgService;
import com.ifast.common.annotation.Log;
import com.ifast.oss.domain.FileDO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ifast.common.base.AdminBaseController;
import com.ifast.domain.ProductDO;
import com.ifast.service.ProductService;
import com.ifast.common.utils.Result;

/**
 * <pre>
 *
 * </pre>
 * <small> 2018-08-19 19:10:02 | Aron</small>
 */
@Controller
@RequestMapping("/ifast/product")
public class ProductController extends AdminBaseController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ImgService imgService;

    @GetMapping()
    @RequiresPermissions("ifast:unit:unit")
    String Product(Long id, Map<String, Object> model) {
        model.put("id", id);
        return "ifast/product/product";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("ifast:unit:unit")
    public Result<Page<ProductDO>> list(ProductDO productDTO) {
        Wrapper<ProductDO> wrapper = new EntityWrapper<>(productDTO);
        Page<ProductDO> page = productService.selectPage(getPage(ProductDO.class), wrapper);
        return Result.ok(page);
    }

    @GetMapping("/add")
    @RequiresPermissions("ifast:unit:unit")
    String add() {
        return "ifast/product/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("ifast:unit:unit")
    String edit(@PathVariable("id") Integer id, Model model) {
        ProductDO product = productService.selectById(id);
        model.addAttribute("product", product);
        return "ifast/product/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("ifast:unit:unit")
    public Result<String> save(ProductDO product) {
        productService.insert(product);
        return Result.ok();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("ifast:unit:unit")
    public Result<String> update(ProductDO product) {
        productService.updateById(product);
        return Result.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("ifast:unit:unit")
    public Result<String> remove(Integer id) {
        productService.deleteById(id);
        return Result.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("ifast:unit:unit")
    public Result<String> remove(@RequestParam("ids[]") Integer[] ids) {
        productService.deleteBatchIds(Arrays.asList(ids));
        return Result.ok();
    }

    @GetMapping("/Image")
    public String  Image(Long id, Map<String, Object> model){
        model.put("id",id.toString());
    return "ifast/product/file";
    }

    @ResponseBody
    @GetMapping("/ImgList")
    public Result<Page<ImgDO>> list(Integer pageNumber, Integer pageSize, String id) {
        // 查询列表数据
        ImgDO imgDO=new ImgDO();
        imgDO.setParentId(Long.parseLong(id));
        Page<ImgDO> page = new Page<>(pageNumber, pageSize);
        Wrapper wrapper = new EntityWrapper(imgDO);
        page = imgService.selectPage(page, wrapper);
        return Result.ok(page);
    }

}
