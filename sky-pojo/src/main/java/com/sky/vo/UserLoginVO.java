package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO implements Serializable {
    // 自己数据库表中的id
    private Long id;
    // 微信登录时获取的openid
    private String openid;
    // 登录令牌
    private String token;

}
