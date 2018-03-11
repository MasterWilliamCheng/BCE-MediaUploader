package com.snh.controller;

import com.baidubce.BceClientConfiguration;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.BosObject;
import com.baidubce.services.sts.StsClient;
import com.baidubce.services.sts.model.GetSessionTokenRequest;
import com.google.gson.JsonObject;
import com.snh.Config;
import com.snh.SignatureDemo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AckController {

    @RequestMapping(value = "api/ack",produces = "text/javascript; charset=utf-8")
    public String ack(HttpServletRequest request, HttpServletResponse response){
        String httpMethod = request.getParameter("httpMethod");
        String path = request.getParameter("path");
        String queries = request.getParameter("queries");
        String headers = request.getParameter("headers");
        String policy = request.getParameter("policy");
        String sts = request.getParameter("sts");
        String callback = request.getParameter("callback");
        String responseBody = new SignatureDemo(Config.ACCESS_KEY_ID, Config.SECRET_ACCESS_KEY).doIt(httpMethod, path,
                queries, headers, policy, sts, callback);
//        response.addHeader("Content-Type", "text/javascript;charset=UTF-8");
        System.out.println(responseBody);
        return responseBody;
//        return new JsonObject();

    }


}
