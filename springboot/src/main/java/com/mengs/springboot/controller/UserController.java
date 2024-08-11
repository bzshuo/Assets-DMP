package com.mengs.springboot.controller;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mengs.springboot.common.Constants;
import com.mengs.springboot.common.Result;
import com.mengs.springboot.controller.dto.UserDTO;
import com.mengs.springboot.controller.dto.UserPasswordDTO;
import com.mengs.springboot.entity.User;
import com.mengs.springboot.exception.ServiceException;
import com.mengs.springboot.mapper.UserMapper;
import com.mengs.springboot.service.UserService;
import com.mengs.springboot.utils.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户基本表 前端控制器
 * </p>
 *
 * @author MS
 * @since 2022-03-19
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        UserDTO dto = userService.login(userDTO);
        return Result.success(dto);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return Result.success(userService.register(userDTO));
    }

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody User user) {
        try {
            if (user.getUserId() == null && user.getPassword() == null) {  // 新增用户默认密码
                List<User> users = userMapper.selectAll();
                for(User oldUser: users) {
                    if (user.getUsername().equals(oldUser.getUsername())) {
                        throw new ServiceException(Constants.CODE_600, "用户名已存在");
                    }
                }
                user.setAvatarUrl("https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png");
                user.setCreateTime(new Date());
                user.setNickname("用户" + userService.makeSixRandom());
                if (user.getRoleId() == 99) {
                    user.setPassword("123");
                } else if (user.getRoleId() == 100){
                    user.setPassword("1234");
                } else {
                    user.setPassword("12345");
                }
            } else {
                user.setUpdateTime(new Date());
            }
            userService.saveOrUpdate(user);
            return Result.success("操作成功！");
        } catch (Exception e) {
            return Result.error("操作失败，请联系超级管理员！");
        }
    }

    /**
     * 修改密码
     *
     * @param userPasswordDTO
     * @return
     */
    @PostMapping("/password")
    public Result password(@RequestBody UserPasswordDTO userPasswordDTO) {
        userService.updatePassword(userPasswordDTO);
        return Result.success();
    }

    /**
     * 管理员：密码重置 ， 默认重置密码123123
     * @param userId
     * @return
     */
    @GetMapping("/resetPwd")
    public Result resetPwd(@RequestParam Integer userId) {
        boolean update = userService.update(Wrappers.<User>update().lambda()
                                    .set(User::getPassword, "123123")
                                    .set(User::getUpdateTime, new Date())
                                    .eq(User::getUserId, userId));
        if (update) {
            return Result.success();
        }
        return Result.error();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return userService.delUsers(id);
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        for (Integer userId : ids) {
            Result result = userService.delUsers(userId);
            if (!"200".equals(result.getCode())) {
                return Result.error();
            }
        }
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }

    @GetMapping("/role/{role}")
    public Result findUsersByRole(@PathVariable String role) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", role);
        queryWrapper.ne("is_delete", "1");
        List<User> list = userService.list(queryWrapper);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }


    @GetMapping("/username/{username}")
    public Result findByUsername(@PathVariable String username) {
        try {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            queryWrapper.ne("is_delete", "1");
            User one = userService.getOne(queryWrapper);
            return Result.success(one);
        } catch (BusinessException be) {
            return Result.error("查询用户信息异常，请联系管理员！");
        }
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String nickname,
                           @RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") String phone) {
        try {
            Page<User> page = userService.findPage(new Page<User>(pageNum, pageSize), nickname, email, phone);
            return Result.success(page);
        } catch (BusinessException be) {
            return Result.error("系统开小差了！");
        }
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据

        List<User> list = userService.list();
        Iterator<User> iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.next().setPassword("******");
        }
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("userId", "用户编码");
        writer.addHeaderAlias("roleId", "权限ID");
        writer.addHeaderAlias("username", "用户名(唯一)").setColumnWidth(1, 12);
        writer.addHeaderAlias("password", "密码").setColumnWidth(2, 12);
        writer.addHeaderAlias("nickname", "昵称").setColumnWidth(3, 14);
        writer.addHeaderAlias("college", "归属").setColumnWidth(3, 14);
        writer.addHeaderAlias("email", "邮箱").setColumnWidth(4, 20);
        writer.addHeaderAlias("phone", "电话").setColumnWidth(5, 15);
        writer.addHeaderAlias("createTime", "创建时间").setColumnWidth(6, 15);
        writer.addHeaderAlias("avatarUrl", "头像").setColumnWidth(7, 20);
        writer.addHeaderAlias("updateTime", "更新时间").setColumnWidth(8, 15);
        writer.addHeaderAlias("isDelete", "删除状态1-删除 其他-未删除").setColumnWidth(9, 15);
        writer.addHeaderAlias("roleName", "暂不输出");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息*" + RandomUtil.randomInts(5), "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
     * excel 导入
     *
     * @param file
     * @throws Exception
     */
//    @PostMapping("/import")
//    public Result imp(MultipartFile file) throws Exception {
//        InputStream inputStream = file.getInputStream();
//        ExcelReader reader = ExcelUtil.getReader(inputStream);
//        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
////        List<User> list = reader.readAll(User.class);
//
//        // 方式2：忽略表头的中文，直接读取表的内容
//        List<List<Object>> list = reader.read(1);
//        List<User> users = CollUtil.newArrayList();
//        for (List<Object> row : list) {
//            User user = new User();
//            Result byUsername = findByUsername(row.get(1).toString());
//            if (byUsername.getData().toString().isEmpty()) {
//                user.setUsername(row.get(1).toString());
//            } else {
//                throw new BusinessException("用户名称唯一不可重复!");
//            }
//            user.setPassword(row.get(2).toString());
//            user.setNickname(row.get(3).toString());
//            user.setEmail(row.get(4).toString());
//            user.setPhone(row.get(5).toString());
//            if (!row.get(6).toString().isEmpty()) {
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date format = simpleDateFormat.parse(row.get(6).toString());
//                user.setCreateTime(format);
//            } else {
//                user.setCreateTime(new Date());
//            }
//            user.setAvatarUrl(row.get(8).toString());
//            users.add(user);
//        }
//
//        userService.saveBatch(users);
//        return Result.success(true);
//    }


//    public static void main(String[] args) {
//        //使指定区间内的数据隐藏
//        String str = "Hello world and java!";
//        String hideStr = StrUtil.hide(str, 5, 16);
//        System.out.println(hideStr);
//    }
}

