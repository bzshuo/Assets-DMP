package com.mengs.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mengs.springboot.common.Result;
import com.mengs.springboot.entity.Secretkey;
import com.mengs.springboot.service.SecretkeyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 秘钥管理表 前端控制器
 * </p>
 *
 * @author zyz
 * @since 2023-05-07
 */
@RestController
@RequestMapping("/secretkey")
public class SecretkeyController {

    @Resource
    private SecretkeyService secretkeyService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Secretkey secretkey) {
        secretkeyService.saveOrUpdate(secretkey);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        secretkeyService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        secretkeyService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(secretkeyService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(secretkeyService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
        QueryWrapper<Secretkey> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
        return Result.success(secretkeyService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

