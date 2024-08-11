package com.mengs.springboot.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengs.springboot.common.Constants;
import com.mengs.springboot.common.Result;
import com.mengs.springboot.controller.dto.UserDTO;
import com.mengs.springboot.controller.dto.UserPasswordDTO;
import com.mengs.springboot.entity.Menu;
import com.mengs.springboot.entity.Role;
import com.mengs.springboot.entity.User;
import com.mengs.springboot.exception.ServiceException;
import com.mengs.springboot.mapper.MenuMapper;
import com.mengs.springboot.mapper.RoleMapper;
import com.mengs.springboot.mapper.RoleMenuMapper;
import com.mengs.springboot.mapper.UserMapper;
import com.mengs.springboot.utils.StringUtils;
import com.mengs.springboot.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    private static final Log LOG = Log.get();

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private MenuService menuService;

    public UserDTO login(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if (one != null) {
            BeanUtil.copyProperties(one, userDTO, true);
            userDTO.setId(one.getUserId());

            // 设置token
            String token = TokenUtils.genToken(one.getUserId().toString(), one.getPassword());
            userDTO.setToken(token);

            // 设置用户的菜单列表
            List<Menu> roleMenus = getRoleMenus(one.getRoleId());
            userDTO.setMenus(roleMenus);

            userDTO.setRoleId(one.getRoleId());
            // 获取该用户权限的中文名称
            Role role = roleMapper.selectById(one.getRoleId());
            userDTO.setRole(role.getName());

//            userDTO.setPassword("******");
            return userDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    public User register(UserDTO userDTO) {
        List<User> users = userMapper.selectAll();
        for (User user : users) {
            if (userDTO.getUsername().equals(user.getUsername())) {
                throw new ServiceException(Constants.CODE_600, "用户名已存在");
            }
        }
        User one = getUserInfo(userDTO);
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(userDTO, one, true);
            // 默认一个普通用户的角色
//            one.setRole(RoleEnum.ROLE_USER.toString());
            one.setRoleId(99);//普通用户
            one.setNickname("用户" + makeSixRandom());
            one.setAvatarUrl("https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png");
            one.setCreateTime(new Date());
            userMapper.insert(one);  // 把 copy完之后的用户对象存储到数据库
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return one;
    }

    public void updatePassword(UserPasswordDTO userPasswordDTO) {
        int update = userMapper.updatePassword(userPasswordDTO);
        System.out.println(update);
        if (update < 1) {
            throw new ServiceException(Constants.CODE_600, "密码错误");
        }
    }

    public Page<User> findPage(Page<User> page, String nickname, String email, String phone) {
//        return userMapper.findPage(page, nickname, email, phone);  // 使用 sql 查询
        // 使用框架
        QueryWrapper<User> uqw = new QueryWrapper<>();
        if (StringUtils.isNotBlank(nickname)) {
            uqw.like("nickname", nickname);
        }
        if (StringUtils.isNotBlank(email)) {
            uqw.like("email", email);
        }
        if (StringUtils.isNotBlank(phone)) {
            uqw.like("phone", phone);
        }

        User currentUser = TokenUtils.getCurrentUser();
        if (1 != currentUser.getRoleId()) {
            uqw.notIn("role_id", 1);

        }
        Page<User> pageInfo = page(page, uqw);
        Iterator<User> iterator = pageInfo.getRecords().iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            user.setRoleName(roleMapper.selectById(user.getRoleId()).getName());
        }
        return pageInfo;
    }

    private User getUserInfo(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        User one;
        try {
            one = userMapper.selectOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

    /**
     * 获取当前角色的菜单列表 （多级菜单）
     *
     * @param roleId
     * @return
     */
    private List<Menu> getRoleMenus(Integer roleId) {
//        Integer roleId = roleMapper.selectByGrade(grade);
        // 当前角色的所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);
        // 查询当前角色的菜单信息
        List<Menu> menus = menuMapper.selectBatchIds(menuIds);

        Collections.sort(menus, new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                Integer date1 = o1.getWeight();
                Integer date2 = o2.getWeight();
                return date1.compareTo(date2);
            }
        });
        return menus;
    }

    /**
     * 单一级菜单
     */
    private List<Menu> selectRoleMenus(String grade) {
        Integer roleId = roleMapper.selectByGrade(grade);
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);
        List<Menu> roleMenus = menuMapper.selectMenuList(menuIds);
        return roleMenus;
    }


    public String makeSixRandom() {
        int random = (int) ((Math.random() * 9 + 1) * 100000);
        return String.valueOf(random);
    }

    public Result delUsers(Integer userId) {
        try {
            update(Wrappers.<User>update().lambda().set(User::getIsDelete, "1").set(User::getUpdateTime, new Date()).eq(User::getUserId, userId));
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }
}
