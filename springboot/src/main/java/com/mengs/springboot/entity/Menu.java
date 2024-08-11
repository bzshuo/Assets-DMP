package com.mengs.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 菜单
 * </p>
 *
 * @author MS
 * @since 2022-03-19
 */
@Getter
@Setter
@ApiModel(value = "Menu对象", description = "菜单")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("菜单id")
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;

    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("菜单路径")
    private String path;

    @ApiModelProperty("说明")
    private String comment;

    @ApiModelProperty("菜单图标")
    private String icon;

    @TableField(exist = false)
    private List<Menu> children;

    @ApiModelProperty("页面路径")
    private String pagePath;

    @ApiModelProperty("权重（排序使用）")
    private Integer weight;

}
