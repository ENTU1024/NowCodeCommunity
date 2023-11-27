package com.nowcoder.community.controller;


import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
public class HomeController implements CommunityConstant {


    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("index");

        List<DiscussPost> list = discussPostService.findDiscussPosts(0,page.getOffset(),page.getLimit());
        List<Map<String,Object>> discussPots = new ArrayList<>();
        if (list != null){
            for (DiscussPost post : list){
                Map<String , Object> map = new HashMap<>();
                map.put("post",post);
                User user = userService.findUserById(post.getUserId());
                map.put("user",user);

                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount", likeCount);

                discussPots.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPots);
        return "/index";
    }

/*

 //   @Primary
    @RequestMapping("/Bye")
    @ResponseBody
    public String sayHellow(){
        return "hellow";
    }

    @RequestMapping("/o")
    @ResponseBody
    public String sayBye(){
        return "Bye";
    }


    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response){
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()){
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ": " + value);
        }
        System.out.println(request.getParameter("code"));

        response.setContentType("text/html;charset=utf-8");
        //写在小括号内可以自动添加finally来关闭writer
        try ( PrintWriter writer = response.getWriter()
        ) {
            writer.write("<h1>nowcode</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取request参数的两种方式之一:通过？
    @RequestMapping(path = "/students",method = RequestMethod.GET)
    @ResponseBody
    public String getstudents(
            @RequestParam(name = "current",required = false,defaultValue = "1") int current,
            @RequestParam(name = "limit" ,required = false,defaultValue = "10") int limit ){
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    @RequestMapping(path = "/students/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String getstudent(@PathVariable("id") int id){
        System.out.println(id);
        return "a student";
    }

    @RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return "successes";
    }

    //响应HTML数据
    @RequestMapping(path = "/teacher",method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("name","王江红");
        mav.addObject("age",20);
        mav.setViewName("/dome/view");
        return mav;
    }
    //Model是被Dispacth自动识别创建的
    @RequestMapping(path = "/school",method = RequestMethod.GET)
    public String  getSchool(Model model){
        model.addAttribute("name","gzmu");
        model.addAttribute("age",90);
        return "/dome/view";
    }

    @RequestMapping(path = "/emp",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getEmp(){
        Map<String,Object> emp = new HashMap<>();
        emp.put("name","w");
        emp.put("age","12");
        emp.put("Salary",10000);
        return emp;
    }
*/

}
