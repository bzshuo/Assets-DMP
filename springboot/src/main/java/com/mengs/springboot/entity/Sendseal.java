package com.mengs.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 用章管理表
 * </p>
 *
 * @author zyz
 * @since 2023-05-07
 */
@Getter
@Setter
@ApiModel(value = "Sendseal对象", description = "用章管理表")
public class Sendseal implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("申请签章id")
    @TableId(value = "send_id", type = IdType.AUTO)
    private Integer sendId;

    private String sendTitle;

    @ApiModelProperty("申请用户id")
    private Integer userId;
    @TableField(exist = false)
    private String userName;

    @ApiModelProperty("用章id")
    private Integer sealId;
    @TableField(exist = false)
    private String sealName;
    @TableField(exist = false)
    private String urlPath;

    @ApiModelProperty("状态:0-初始 1-审批 2-加印(完成)")
    private String state;

    @ApiModelProperty("申请描述")
    private String remark;

    @ApiModelProperty("RSA系统秘钥id")
    private String rsaSystemId;

    @ApiModelProperty("RSA用户秘钥id")
    private String rsaUserId;

    @ApiModelProperty("加印前文件")
    private String originFileUrl;
    @TableField(exist = false)
    private String originFileUrlUUID;

    @ApiModelProperty("加印前文件哈希SHA1")
    private String originFileSha;

    @ApiModelProperty("加印后文件")
    private String newFileUrl;
    @TableField(exist = false)
    private String newFileUrlUUID;

    @ApiModelProperty("加印后文件哈希SHA1")
    private String newFileSha;

    @ApiModelProperty("审批人")
    private Integer approver;
    @TableField(exist = false)
    private String approverName;

    @ApiModelProperty("加印人")
    private Integer over;
    @TableField(exist = false)
    private String overName;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("审批时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date apprTime;

    @ApiModelProperty("用印时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date overTime;


}
