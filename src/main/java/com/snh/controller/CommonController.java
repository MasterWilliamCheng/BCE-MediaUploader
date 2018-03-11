package com.snh.controller;

import com.snh.entity.DataUser;
import com.snh.service.UserService;
import com.snh.weixin.entity.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CommonController {
    @Resource
    private UserService userService;

    @RequestMapping(value = "menu")
    public String menu(HttpServletRequest request) {
        return "menu";
    }

    @RequestMapping(value = "index")
    public String index(HttpServletRequest request) {
        Object object = request.getSession().getAttribute("dataUser_needUpdate");
        boolean needUpdate =  true;
        if(object == null){
            needUpdate = false;
        }

        if(needUpdate){
            String openId = (String)request.getSession().getAttribute("openId");
            DataUser dataUser = userService.findUserByOpenid(openId);
            request.getSession().setAttribute("dataUser", dataUser);
            request.getSession().setAttribute("dataUser_needUpdate", false);
            request.getSession().setMaxInactiveInterval(1800);
        }
        return "index";
    }

    @RequestMapping(value = "logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:login";
    }
}
