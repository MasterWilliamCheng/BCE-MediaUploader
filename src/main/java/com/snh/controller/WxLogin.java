package com.snh.controller;

import com.mysql.jdbc.JDBC4Connection;
import com.snh.Config;
import com.snh.entity.DataUser;
import com.snh.service.UserService;
import com.snh.util.LogUtil;
import com.snh.util.SpringPropertiesUtil;
import com.snh.weixin.WxConfig;
import com.snh.weixin.api.CoreApi;
import com.snh.weixin.api.WxOpenApi;
import com.snh.weixin.api.WxSiteApi;
import com.snh.weixin.entity.UserAccessToken;
import com.snh.weixin.entity.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;


@Controller

public class WxLogin extends BaseController {

    @Resource
    private UserService userService;

    public static String url = SpringPropertiesUtil.getProperty("WX_URL");

    @RequestMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        String contextPath = request.getSession().getServletContext().getContextPath();
        if (LogUtil.isDebugEnabled()) {
            url = Config.URL + contextPath + "/login_callback";
        }
        model.addAttribute("callback_url", url);
        return "login";
    }

    @RequestMapping("/login_callback")
    public String qrLogin(@RequestParam(required = false) String code, @RequestParam String state, HttpServletRequest request) {
        String error = null;
        DataUser dataUser = null;
        Date date = new Date();
        boolean isExist = false;
        if (code != null) {
            //获取第一步的code后，请求以下链接获取access_token
            UserAccessToken token = CoreApi.create(WxSiteApi.class).getOAuthAccessToken(WxConfig.WEIXIN_LOGIN_APP_ID, WxConfig.WEIXIN_LOGIN_APP_SECRET, code, "authorization_code");
            if (token != null && token.getErrcode() == 0) {
                dataUser = userService.findUserByOpenid(token.getOpenid());
                //获取用户基本信息
//                UserInfo userInfo = CoreApi.create(WxOpenApi.class).getUserInfo(token.getAccess_token(), token.getOpenid(), "zh_CN");
                if (dataUser == null) {
                    dataUser = getDataUser(token.getOpenid());
                    if (dataUser != null) {
                        if(!dataUser.getGroupId().equals("")){
                            dataUser.setCtime(new Date(System.currentTimeMillis()));
//                        dataUser.setHeadimgurl(userInfo.getHeadimgurl());
                            userService.insertUser(dataUser);
                            request.getSession().setAttribute("dataUser_needUpdate", true);
                            request.getSession().setAttribute("openId", token.getOpenid());
                            request.getSession().setMaxInactiveInterval(1800);
                            isExist = true;
                        }else{
                            error = "authority_prevent";
                        }
                    } else {
                        String nickName = isWXfollower(token.getOpenid());
                        if(nickName == null){
                            error = "WX_prevent";
                        }else{
                            insertDataUser(nickName,token.getOpenid());
                            error = "authority_prevent";
                        }
                    }
                } else {
                    dataUser.setUtime(date);
                    userService.updateByOpenid(dataUser);
                    isExist = true;
                }
                if (isExist) {
                    request.getSession().setAttribute("dataUser", dataUser);
                    request.getSession().setMaxInactiveInterval(1800);
                    return "redirect:index";
                }
            } else {
                //无法获取token
                error = "access_token_empty";
            }
        } else {
            //无法获取code
            error = "code_empty";
        }
        return "redirect:login?error=" + error;
    }


    @RequestMapping("wx/login_callback")
    public String qrLogin(@RequestParam(required = true) String openid, HttpServletRequest request) {
        String error = null;
        boolean isExist = false;
        Date date = new Date();
        DataUser dataUser = userService.findUserByOpenid(openid);
        if (dataUser == null) {
            dataUser = getDataUser(openid);
            if(dataUser != null){
                if(!dataUser.getGroupId().equals("")){
                    dataUser.setCtime(new Date(System.currentTimeMillis()));
//                        dataUser.setHeadimgurl(userInfo.getHeadimgurl());
                    userService.insertUser(dataUser);
                    request.getSession().setAttribute("dataUser_needUpdate", true);
                    request.getSession().setAttribute("openId", openid);
                    request.getSession().setMaxInactiveInterval(1800);
                    isExist = true;;
                }else{
                    error = "请等待管理员后台审核登陆权限";
                }
            }else{
                String nickName = isWXfollower(openid);
                if(nickName == null){
                    error = "请先关注官方微信公众号";
                }else{
                    insertDataUser(nickName,openid);
                    error = "请等待管理员后台审核登陆权限";
                }
            }
        }else{
            dataUser.setUtime(date);
            userService.updateByOpenid(dataUser);
            isExist = true;
        }
        if(isExist){
            request.getSession().setAttribute("dataUser", dataUser);
            request.getSession().setMaxInactiveInterval(1800);
            return "redirect:/index";
        }
        request.setAttribute("error",error);
        return "errorPage";

    }


    //查询登录用户信息
    @Transactional
    public DataUser getDataUser(String openid) {
        DataUser dataUser = null;
        try {
            Properties properties = new Properties();
            properties.setProperty("user", "USER");
            properties.setProperty("password", "PASSWORD");
            JDBC4Connection connection;
            try {
                connection = new JDBC4Connection("URL", 3306, properties, "DATABASE", "com.mysql.jdbc.Driver");

                Statement statement = connection.createStatement();
                String sql = "SELECT * from mst_staff where openid ='" + openid + "' limit 1";
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    dataUser = new DataUser();
                    String groupId = resultSet.getString("group_id")==null?"":resultSet.getString("group_id");
                    String depName = resultSet.getString("depname")==null?"":resultSet.getString("depname");
                    String staffName = resultSet.getString("staffname")==null?"":resultSet.getString("staffname");
                    dataUser.setOpenid(openid);
                    dataUser.setDepname(depName);
                    dataUser.setGroupId(groupId);
                    dataUser.setStaffname(staffName);
                    dataUser.setStatus((byte) 1);
                }
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dataUser;
    }

    //查询用户是否关注了微信公众号
    @Transactional
    public String isWXfollower(String openid) {
        String nickName = null;
        try {
            Properties properties = new Properties();
            properties.setProperty("user", "USER");
            properties.setProperty("password", "PASSWORD");
            JDBC4Connection connection;
            try {
                connection = new JDBC4Connection("URL", 3306, properties, "DATABASE", "com.mysql.jdbc.Driver");

                Statement statement = connection.createStatement();
                String sql = "SELECT * from wx_dat_users where openid ='" + openid + "' and status = 1 limit 1";
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    nickName = resultSet.getString("nickname")==null?"":resultSet.getString("nickname");
                }
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return nickName;
    }

    @Transactional
    //添加一条新用户待审核数据
    public int insertDataUser(String staffname,String openid) {
        int i = 0;
        try {
            Properties properties = new Properties();
            properties.setProperty("user", "USER");
            properties.setProperty("password", "PASSWORD");
            JDBC4Connection connection;
            try {
                connection = new JDBC4Connection("URL", 3306, properties, "DATABASE", "com.mysql.jdbc.Driver");

                Statement statement = connection.createStatement();
                String sql = "INSERT INTO mst_staff(depname,staffname,openid) VALUES('','"+staffname+"','"+openid+"') ";
                LogUtil.error("Print SQL:"+sql);
                i = statement.executeUpdate(sql);
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return i;
    }


}
