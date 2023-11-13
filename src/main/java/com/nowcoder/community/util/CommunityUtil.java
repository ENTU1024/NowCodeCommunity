package com.nowcoder.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

public class CommunityUtil {

    //生成随机字符串
    //真的体现了封装精神,不要让业务代码直接调用UUID类的东西，减少代码复杂度的情况下，还可以对代码进行修改
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    //MD5加密
    //hello -》 abc123def456
    //hello + 3e4a8(salt) -> abc123def456asd
    public static String md5(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getJSONString(int code, String msg, Map<String, Object> map){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",msg);
        if (map != null){
            for (String key:map.keySet())
                jsonObject.put(key,map.get(key));
        }
        return jsonObject.toJSONString();
    }
    public static String getJSONString(int code){
        return getJSONString(code,null,null);
    }
    public static String getJSONString(int code,String msg){
        return getJSONString(code, msg,null);
    }

}
