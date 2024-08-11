package com.mengs.springboot.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mengs.springboot.common.Result;
import com.mengs.springboot.entity.User;
import com.mengs.springboot.service.UsingsealService;
import com.mengs.springboot.utils.TokenUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.mengs.springboot.entity.Usingseal;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用章管理表 前端控制器
 * </p>
 *
 * @author zyz
 * @since 2023-05-07
 */
@RestController
@RequestMapping("/usingSeal")
public class UsingsealController {

    @Resource
    private UsingsealService usingsealService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Usingseal usingseal) {
        //当seal id为空，即为新增，插入创建时间
        if (ObjectUtils.isEmpty(usingseal.getSealId())) {
            usingseal.setCreateTime(new Date());
            User user = TokenUtils.getCurrentUser();
            usingseal.setStaff(user.getUsername());
        }
        usingsealService.saveOrUpdate(usingseal);
        return Result.success("操作成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        usingsealService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        usingsealService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(usingsealService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(usingsealService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam String sealName,
                                @RequestParam Integer pageSize) {
        QueryWrapper<Usingseal> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("seal_name", sealName);
        queryWrapper.orderByDesc("seal_id");
        return Result.success(usingsealService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/sealList")
    public Result sealList() {
        QueryWrapper<Usingseal> qw = new QueryWrapper<>();
        qw.select("seal_id", "seal_name");
        List<Usingseal> list = usingsealService.list(qw);
        return Result.success(list);
    }

}

