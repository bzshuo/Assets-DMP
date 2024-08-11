package com.mengs.springboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 秘钥管理表
 * </p>
 *
 * @author zyz
 * @since 2023-05-07
 */
@Getter
@Setter
@ApiModel(value = "Secretkey对象", description = "秘钥管理表")
public class Secretkey implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("秘钥id")
    private String keyId;

    @ApiModelProperty("秘钥类型")
    private Integer keyType;

    @ApiModelProperty("公钥")
    private String keyPublic;

    @ApiModelProperty("私钥")
    private String keyPrivate;

    @ApiModelProperty("状态:0-有效 1-失效")
    private String state;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;


}
