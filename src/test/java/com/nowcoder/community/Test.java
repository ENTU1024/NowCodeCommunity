/*
package com.nowcoder.community;


import com.nowcoder.community.util.CommunityUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Test {
*//*

*/
/*
    @org.junit.jupiter.api.Test
    public void Test(){
     *//*
*/
/*
*//*

*/
/*   Preson zkr = new PresonImpl();
        ApplicationContext ac  = new AnnotationConfigApplicationContext(SpringConfig.class);

        zkr.eat((Food) ac.getBean("Apple")); ;*//*
*/
/*
*//*

*/
/*
    }*//*



    @RequestMapping(path = "/cookie/set", method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        //设置cookies的范围
        cookie.setPath("/community/");
        //设置生存时间
        cookie.setMaxAge(60 * 10);
        response.addCookie(cookie);
        return "set cookie";
    }
    @RequestMapping(path = "/cookie/get", method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(HttpServletRequest response) {
       String a = String.valueOf(response.getCookies());
        System.out.println(a);
       return "get cookie";
    }
    @RequestMapping(path = "/cookie/get", method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String  cookie) {
        System.out.println(cookie);
        return "get cookie";
    }
    @RequestMapping(path = "/cookie/get", method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpSession session){
        session.setAttribute("1", 1);
        session.setAttribute("2", "a");
        return "set session";
    }
}
*/
