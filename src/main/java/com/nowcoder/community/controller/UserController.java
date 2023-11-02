package com.nowcoder.community.controller;


import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private HostHolder holder;

    @Autowired
    private UserService userService;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String  uploadHeader(MultipartFile headerImage, Model model){
        if (headerImage == null){
            model.addAttribute("error", "您还没有选择图片");
            return "/site/setting";
        }

        //获取用户上传的文件名
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1 );
        if (StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件的格式不正确");
            return "/site/setting";
        }
        //生成随机文件名
        fileName = CommunityUtil.generateUUID()+ "." + suffix;
        //确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        //存储文件
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败: " + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发送异常！",e);
        }
        // http://localhost:8080/community/user/header/xxx.png
        User user = holder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){
        //服务器存放路径
        fileName = uploadPath + "/" + fileName;
        //文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        //响应图片
        response.setContentType("image/" + suffix);


        try (
                OutputStream os = response.getOutputStream();
                FileInputStream fis = new FileInputStream(fileName);
                ){
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1){
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
           logger.error("读取头象失败" + e.getMessage());
        }
    }

    @LoginRequired
    @RequestMapping(path = "/update-password", method = RequestMethod.POST)
    public String updatePassword(@CookieValue("ticket") String ticket, String forePassword, String password, Model model){
        if (StringUtils.isBlank(password)){
            model.addAttribute("passwordMsg","密码非法");
            return "/site/setting";
        }
        int result = userService.updatePassword(holder.getUser().getId(),forePassword, password);
        if (result == 1){
            model.addAttribute("msg","修改密码成功，您可以选择重新登录");
            model.addAttribute("target","/login");
            userService.logout(ticket);
            return "/site/operate-result";
        }else{
            model.addAttribute("forePasswordMsg","修改密码失败，请重新修改密码或者检测你的原密码是否正确");
            return "/site/setting";
        }

    }
}
