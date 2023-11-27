package com.nowcoder.community.util;

public class RedisKeyUtil {
    private final static String SPLIT = ":";
    private final static String PREFIX_ENTITY_LIKE = "like:entity";
    private final static String PREFIX_USER_LIKE = "like:user";
    private final static String PREFIX_FOLLOWEE = "followee";
    private final static String PREFIX_FOLLOWER = "follower";
    private final static String PREFIX_KAPTCHA = "kaptcha";
    private final static String PREFIX_TICKET = "ticket";
    private final static String PREFIX_USER = "user";

    //某个实体的赞
    //like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, int entityId){
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    //某个用户的赞
    //like:user:userId -> int
    public static String getUserLikeKey(int userId){
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    //某个用户关注的实体,这里采用zset是用时间作为score,到时候好显示
    //followee:userId:entityType -> zset(entityId,now)
    public static String getFolloweeKey(int userId,int entityType){
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    //某个用户拥有的粉丝
    public static String getFollowerKey(int entityType, int entityId){
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    //登录验证码
    public static String getKaptchaKey(String owner){
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    //登录的凭证
    public static String getTicketKey(String ticket){
        return PREFIX_TICKET + SPLIT + ticket;
    }

    //用户
    public static String getUserKey(int userId){
       return PREFIX_USER + SPLIT + userId;
    }
}
