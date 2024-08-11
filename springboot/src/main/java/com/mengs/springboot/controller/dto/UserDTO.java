package com.mengs.springboot.controller.dto;

import com.mengs.springboot.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * 接受前端登录请求的参数
 */
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String role;
    private Integer roleId;
    private String college;
    private List<Menu> menus;
}
