package com.nowcoder.community.aspect;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AlphaAspect {

        //第一个*是任意返回值，第二个*是任意类，第二个*是任意方法，第四个*是任意参
        @Pointcut("")
        public void pointcut(){

        }
}
