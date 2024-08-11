package com.mengs.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 权限菜单表
 * </p>
 *
 * @author MS
 * @since 2022-03-19
 */
@Getter
@Setter
@TableName("role_menu")
@ApiModel(value = "RoleMenu对象", description = "权限菜单表")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("权限id")
    private Integer roleId;

    @ApiModelProperty("菜单id")
    private Integer menuId;


}
