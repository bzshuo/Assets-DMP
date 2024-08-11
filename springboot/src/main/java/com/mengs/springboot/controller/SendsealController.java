package com.mengs.springboot.controller;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mengs.springboot.common.Result;
import com.mengs.springboot.config.ApplicationConfig;
import com.mengs.springboot.entity.Sendseal;
import com.mengs.springboot.entity.User;
import com.mengs.springboot.entity.Usingseal;
import com.mengs.springboot.service.SendsealService;
import com.mengs.springboot.service.UserService;
import com.mengs.springboot.service.UsingsealService;
import com.mengs.springboot.utils.PdfUtils;
import com.mengs.springboot.utils.SHAUtils;
import com.mengs.springboot.utils.StringUtils;
import com.mengs.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用章管理表 前端控制器
 * </p>
 *
 * @author zyz
 * @since 2023-05-07
 */
@RestController
@RequestMapping("/sendSeal")
public class SendsealController {

    @Resource
    private SendsealService sendsealService;
    @Autowired
    private UsingsealService usingsealService;
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationConfig applicationConfig;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Sendseal sendseal) {
        if (null == sendseal.getSendId()) {
            sendseal.setCreateTime(new Date());
            User user = TokenUtils.getCurrentUser();
            sendseal.setUserId(user.getUserId());
        }
        sendsealService.saveOrUpdate(sendseal);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
//        QueryWrapper<Sendseal> wqw = new QueryWrapper<>();
//        wqw.eq("send_id", id);
        Sendseal sendseal = new Sendseal();
        sendseal.setSendId(id);
        sendseal.setState("3");//作废
        sendsealService.saveOrUpdate(sendseal);
        return Result.success();
    }

    /**
     * 审批按钮 控制层
     *
     * @param id
     * @return
     */
    @DeleteMapping("/approve/{id}")
    public Result approve(@PathVariable Integer id) {
        Sendseal byId = sendsealService.getById(id);
        if (!"0".equals(byId.getState())) {
            return Result.error("请按流程操作");
        }
        Sendseal sendseal = new Sendseal();
        sendseal.setSendId(id);
        sendseal.setState("1");//审批
        User user = TokenUtils.getCurrentUser();
        sendseal.setApprover(user.getUserId());
        sendseal.setApprTime(new Date());
        sendsealService.saveOrUpdate(sendseal);
        return Result.success();
    }

    /**
     * 用印处理
     * 1、 更新成用印状态，
     * 2、 将用户上传的PDF文件 加印 处理 生成 New PDF文件，并将新文件存储至下载路径
     * 3、 将生成的New PDF文件 SHA1 40位 存储数据库
     *
     * @param id
     * @return
     */
    @DeleteMapping("/usingSeal/{id}")
    public Result usingSeal(@PathVariable Integer id) throws IOException {
        Sendseal byId = sendsealService.getById(id);
        if (!"1".equals(byId.getState())) {
            return Result.error("请按流程走向操作");
        }

        String originFileUrl = byId.getOriginFileUrl();
        String[] split = originFileUrl.split("\\/");
        Integer sealId = byId.getSealId();
        Usingseal usingseal = usingsealService.getById(sealId);
        String urlPath = usingseal.getUrlPath();
        String[] sp = urlPath.split("\\/");

        //对即将要生成的用印文件 做唯一标识码
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + "pdf";
        //new pdf 网络地址
        String newFileUrl = "http://" + applicationConfig.getServerIp() + ":9090/file/pdfNew/" + fileUUID;
        PdfUtils.pdfAddSeal(applicationConfig.getPdfOriginPath() + split[split.length - 1],
                applicationConfig.getPdfNewPath() + fileUUID,
                applicationConfig.getSignPicPath() + sp[sp.length - 1]);
        System.out.println("新PDF文件用印*保存成功");


        Sendseal sendseal = new Sendseal();
        sendseal.setSendId(id);
        sendseal.setState("2");//用印
        User user = TokenUtils.getCurrentUser();
        sendseal.setOver(user.getUserId());
        sendseal.setOverTime(new Date());
        sendseal.setNewFileUrl(newFileUrl);

        String newFileSHA = SHAUtils.pdfFileSHA(fileUUID, applicationConfig.getPdfNewPath());
        sendseal.setNewFileSha(newFileSHA);

        sendsealService.saveOrUpdate(sendseal);
        return Result.success("用印成功");
    }

    /**
     * 验证PDF文件是否被篡改
     * @param fileName
     * @param sendId
     * @return
     * @throws IOException
     */
    @GetMapping("/verifyCA")
    public Result deleteBatch(@RequestParam String fileName, @RequestParam Integer sendId) throws IOException {
        String verifySHA = SHAUtils.pdfFileSHA(fileName, applicationConfig.getPdfVerifyPath());
        System.out.println("-----上传文件SHA:" + verifySHA);
        Sendseal byId = sendsealService.getById(sendId);
        System.out.println("-----数据库用印文件SHA:" + byId.getNewFileSha());
        if (verifySHA.equals(byId.getNewFileSha())) {
            return Result.success("验证通过，文件未被篡改");
        }

        return Result.error("验证未通过，请使用正版文件");
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        sendsealService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(sendsealService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(sendsealService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Sendseal> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("send_id");
        User currentUser = TokenUtils.getCurrentUser();
        if (99 == currentUser.getRoleId()) {
            queryWrapper.eq("user_id", currentUser.getUserId());
        }
        Page<Sendseal> page = sendsealService.page(new Page<>(pageNum, pageSize), queryWrapper);
        page.getRecords().stream().forEach(v -> {
            String originFileUrl = v.getOriginFileUrl();
            if (StringUtils.isNotBlank(originFileUrl)) {
                String[] split = originFileUrl.split("\\/");
                v.setOriginFileUrlUUID(split[split.length - 1]);
            }

            Usingseal usingseal = usingsealService.getById(v.getSealId());
            v.setSealName(usingseal.getSealName());
            v.setUrlPath(usingseal.getUrlPath());

            User user = userService.getById(v.getUserId());
            v.setUserName(user.getUsername());
            if (null != v.getApprover()) {
                User approve = userService.getById(v.getApprover());
                v.setApproverName(approve.getUsername());
            }
            if (null != v.getOver()) {
                User over = userService.getById(v.getOver());
                v.setOverName(over.getUsername());
            }
        });
        return Result.success(page);
    }

    @GetMapping("/sendList")
    public Result sendList() {
        QueryWrapper<Sendseal> qw = new QueryWrapper<>();
        User currentUser = TokenUtils.getCurrentUser();
        if (99 == currentUser.getRoleId()) {
            qw.eq("user_id", currentUser.getUserId());
        }
        qw.eq("state", "2");
        qw.select("send_id", "send_title");
        List<Sendseal> list = sendsealService.list(qw);
        return Result.success(list);
    }

}

