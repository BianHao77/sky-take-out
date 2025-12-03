package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // 微信服务接口地址
    public static final String WX_LOGIN_ADDRESS="https://api.weixin.qq.com/sns/jscode2session";

    private final WeChatProperties weChatProperties;
    private final UserMapper userMapper;

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        // 1.调用微信接口服务，获取当前微信用户的openId
        String openid = getOpenid(userLoginDTO.getCode());

        // 2.判断openId是否为空，如果为空则登录失败
        if (openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 3.判断当前微信用户是否为新用户，如果是新用户，自动完成注册
        User user = userMapper.getByOpenid(openid);
        if (user == null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }



        return user;
    }

    /**
     * 获取微信用户openid
     * @param code
     * @return
     */
    private String getOpenid(String code){
        // 1.调用微信接口服务，获取当前微信用户的openId
        HashMap<String, String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        // 请求返回String类型的数据
        String json = HttpClientUtil.doGet(WX_LOGIN_ADDRESS, map);
        // 将String类型的数据转为JSONObject
        JSONObject jsonObject = JSONObject.parseObject(json);
        // 获取openid
        String openid = jsonObject.getString("openid");

        return openid;
    }

}
