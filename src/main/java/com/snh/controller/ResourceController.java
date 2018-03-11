package com.snh.controller;

import com.baidubce.BceClientConfiguration;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.vod.VodClient;
import com.baidubce.services.vod.model.GenerateMediaDeliveryInfoResponse;
import com.baidubce.services.vod.model.GetMediaResourceResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import com.snh.Config;
import com.snh.entity.DataResource;
import com.snh.entity.DataUser;
import com.snh.entity.LogOperation;
import com.snh.entity.LogOperationParams;
import com.snh.service.LogService;
import com.snh.service.MediaService;
import com.snh.util.LogUtil;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class ResourceController {
    private static final String ACCESS_KEY_ID = Config.ACCESS_KEY_ID;
    private static final String SECRET_ACCESS_KEY = Config.SECRET_ACCESS_KEY;
    private static final String ENDPOINT = Config.BOS_ENDPOINT;
    private static final String BUCKETNAME = Config.BOS_BUCKET;
    private static final String VOD_ENDPOINT = Config.VOD_ENDPOINT;
    private static final String USERKEY = Config.USERKEY;
    private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
    private static final String USERID = Config.USERID;
    private static final String tp_encrypt = Config.TRANSCODING_PRESETGROUPNAME_ENCRYPT;
    private static final String tp_noencrypt = Config.TRANSCODING_PRESETGROUPNAME_NOENCRYPT;
    @Resource
    private MediaService mediaService;
    @Resource
    private LogService logService;

    //初始化BosClient
    public BosClient bosClientGetInstance() {
        //初始化BosClient配置
        BosClientConfiguration config = new BosClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY));
        config.setEndpoint(ENDPOINT);
        /*config.setProxyHost("127.0.0.1");
        config.setProxyPort(8080);*/

        BosClient bosClient = new BosClient(config);
        return bosClient;
    }

    //初始化VodClient
    public VodClient vodClientGetInstance() {
        //初始化VodClient配置
        BceClientConfiguration config = new BceClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY));
        config.setEndpoint(VOD_ENDPOINT);
        /*config.setProxyHost("127.0.0.1");
        config.setProxyPort(8080);*/

        VodClient vodClient = new VodClient(config);
        return vodClient;
    }

    //加载视频列表
    @RequestMapping(value = "console/video_list")
    public String getVideoList(HttpServletRequest request, @RequestParam(required = true, defaultValue = "1", value = "page") Integer page, @RequestParam(required = false, defaultValue = "10", value = "pageSize") Integer pageSize) {
        PageHelper.startPage(Integer.valueOf(page), Integer.valueOf(pageSize));
        String owner = request.getParameter("searchOwner") == null ? null : request.getParameter("searchOwner").trim();
        String name = request.getParameter("searchName") == null ? null : request.getParameter("searchName").trim();
        List<DataResource> videoList = mediaService.getVideoList(name,owner);
        VodClient vodClient = vodClientGetInstance();
        for (DataResource data : videoList) {
            if (data.getStatus() == 2) {
                GetMediaResourceResponse res = vodClient.getMediaResource(data.getObjectKey());
                String status = res.getStatus();
                Long durationInSeconds = res.getMeta().getDurationInSeconds();
                int videoStatus;
                switch (status) {
                    case "PROCESSING"://内部处理中
                        videoStatus = 3;
                        break;
                    case "RUNNING"://转码中
                        videoStatus = 2;
                        break;
                    case "FAILED"://转码失败
                        videoStatus = 4;
                        break;
                    case "PUBLISHED"://已发布
                        videoStatus = 5;
                        break;
                    case "DISABLED"://已停用
                        videoStatus = 6;
                        break;
                    case "BANNED"://已封禁
                        videoStatus = 7;
                        break;
                    default://异常
                        videoStatus = 8;
                        break;
                }
                data.setStatus(videoStatus);
                int seconds = Integer.parseInt(durationInSeconds.toString());
                int temp=0;
                StringBuffer sb=new StringBuffer();
                temp = seconds/3600;
                sb.append((temp<10)?"0"+temp+":":""+temp+":");

                temp=seconds%3600/60;
                sb.append((temp<10)?"0"+temp+":":""+temp+":");

                temp=seconds%3600%60;
                sb.append((temp<10)?"0"+temp:""+temp);
                data.setDuration(sb.toString());
                mediaService.updateMedia(data);
                /*String createTime = res.getCreateTime();
                String publishTime = res.getPublishTime();
                String title = res.getAttributes().getTitle();
                String description = res.getAttributes().getDescription();
                long duration = res.getMeta().getDurationInSeconds();
                long sizee = res.getMeta().getSizeInBytes();
                String transcodingPresetGroupName = res.getTranscodingPresetGroupName();
                List<PlayableUrl> playableUrlList = res.getPlayableUrlList();
                List<String> thumbnailList = res.getThumbnailList();*/
            }
        }
        PageInfo<DataResource> p = new PageInfo<DataResource>(videoList);
        request.setAttribute("page", p);
        request.setAttribute("videolist", videoList);
        request.setAttribute("searchName", name);
        request.setAttribute("searchOwner", owner);
        return "video_list";
    }

    //加载文档列表
    @RequestMapping(value = "console/doc_list")
    public String getDocList(HttpServletRequest request, @RequestParam(required = true, defaultValue = "1", value = "page") Integer page, @RequestParam(required = false, defaultValue = "10", value = "pageSize") Integer pageSize) {
        PageHelper.startPage(Integer.valueOf(page), Integer.valueOf(pageSize));
        List<DataResource> docList = mediaService.getDocList();
        PageInfo<DataResource> p = new PageInfo<DataResource>(docList);
        request.setAttribute("page", p);
        request.setAttribute("doclist", docList);
        return "doc_list";
    }

    //加载图片列表
    @RequestMapping(value = "console/pic_list")
    public String getPicList(HttpServletRequest request, @RequestParam(required = true, defaultValue = "1", value = "page") Integer page, @RequestParam(required = false, defaultValue = "10", value = "pageSize") Integer pageSize) {
        PageHelper.startPage(Integer.valueOf(page), Integer.valueOf(pageSize));
        String owner = request.getParameter("searchOwner") == null ? null : request.getParameter("searchOwner").trim();
        String name = request.getParameter("searchName") == null ? null : request.getParameter("searchName").trim();
        List<DataResource> picList = mediaService.getPicList(name,owner);
        PageInfo<DataResource> p = new PageInfo<DataResource>(picList);
        request.setAttribute("page", p);
        request.setAttribute("piclist", picList);
        request.setAttribute("searchOwner", owner);
        request.setAttribute("searchName", name);
        return "pic_list";
    }

    //上传新文件
    @RequestMapping(value = "console/uploader")
    public String uploader(HttpServletRequest request) {
        request.setAttribute("tp_encrypt",tp_encrypt);
        request.setAttribute("tp_noencrypt",tp_noencrypt);
        return "uploader";
    }

    //操作日志列表
    @RequestMapping(value = "console/log_list")
    public String getLogList(HttpServletRequest request, @RequestParam(required = true, defaultValue = "1", value = "page") Integer page, @RequestParam(required = false, defaultValue = "10", value = "pageSize") Integer pageSize) {
        PageHelper.startPage(Integer.valueOf(page), Integer.valueOf(pageSize));
        List<LogOperationParams> list = logService.getLogList();
        request.setAttribute("loglist", list);
        PageInfo<LogOperationParams> p = new PageInfo<LogOperationParams>(list);
        request.setAttribute("page", p);
        return "logOperation_list";
    }

    //保存上传文件信息
    @RequestMapping(value = "console/saveUploaderMes", produces = "text/javascript; charset=utf-8")
    @ResponseBody
    public JsonObject saveUploaderMes(HttpServletRequest request, HttpServletResponse response) {
        DataUser dataUser = (DataUser) request.getSession().getAttribute("dataUser");
        String filename = request.getParameter("filename");
        String bosId = request.getParameter("bosId");
        String size = request.getParameter("size");
        String type = request.getParameter("type");
        String transcodingPresetGroupName = request.getParameter("transcodingPresetGroupName");
        DataResource dataResource = new DataResource();
        dataResource.setObjectKey(bosId);
        dataResource.setResourceName(filename);
        dataResource.setSize(size);
        if (type.equals("1")) {
            dataResource.setStatus(2);
            if(transcodingPresetGroupName!=null && transcodingPresetGroupName.equals(tp_encrypt)){
                type = "0";
            }
        } else {
            dataResource.setStatus(1);
        }
        dataResource.setType(Integer.valueOf(type));
        dataResource.setOwner(dataUser.getStaffname());
        dataResource.setOwnerId(dataUser.getUserId());
        Date date = new Date();
        dataResource.setCtime(date);

        LogOperation logOperation = new LogOperation();
        logOperation.setResourceKey(bosId);
        logOperation.setUserId(dataUser.getUserId());
        logOperation.setCtime(date);
        logOperation.setType(1);
        logOperation.setIsValid(true);
        try {
            mediaService.insertResource(dataResource, logOperation);
        } catch (Exception e) {
            LogUtil.error("资源"+dataResource.getObjectKey()+"保存失败",e);
        }

//        int logNum = logService.insertLogOperation(bosId,dataUser.getUserId(),date,1);

        /*//从BOS上传媒资文件到VOD，以便之后视频加密播放
        VodClient vodClient = vodClientGetInstance();
        CreateMediaResourceResponse resp = vodClient.createMediaResource(BUCKETNAME, "uuid/"+bosId, filename, "由BOS上传", "test", 9);
        String mediaId = resp.getMediaId();

        GetMediaResourceResponse res = vodClient.getMediaResource(mediaId);
        String status = res.getStatus();
        String createTime = res.getCreateTime();
        String publishTime = res.getPublishTime();
        String title = res.getAttributes().getTitle();
        String description = res. getAttributes().getDescription();
        long duration = res.getMeta().getDurationInSeconds();
        long sizee = res.getMeta().getSizeInBytes();
        String transcodingPresetGroupName = res.getTranscodingPresetGroupName();
        List<PlayableUrl> playableUrlList = res.getPlayableUrlList();
        List<String> thumbnailList = res.getThumbnailList();*/

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", "success");
        return jsonObject;
    }

    //删除上传文件(VOD)
    @RequestMapping(value = "console/deleteVideo")
    public String deleteVideo(HttpServletRequest request) {
        String page = request.getParameter("page");
        DataUser dataUser = (DataUser) request.getSession().getAttribute("dataUser");
        String objectKey = request.getParameter("objectKey");
        String rId = request.getParameter("rId");
        Date date = new Date();
        LogOperation logOperation = new LogOperation();
        logOperation.setResourceKey(objectKey);
        logOperation.setUserId(dataUser.getUserId());
        logOperation.setCtime(date);
        logOperation.setType(2);
        logOperation.setIsValid(true);
        int i = getMediaStatus(objectKey);
        if(i != 0){
            //删除vod文件信息
            try {
                mediaService.deleteMedia(Long.valueOf(rId), logOperation);
            } catch (NumberFormatException e) {
                LogUtil.error("VOD文件"+objectKey+"删除失败",e);
            }
            //VOD删除
            VodClient vodClient = vodClientGetInstance();
            vodClient.deleteMediaResource(objectKey);
        }
        return "redirect:/console/video_list?page=" + page;
    }

    //删除上传文件(DOC/BOS)
    @RequestMapping(value = "console/deleteFile")
    public String deleteFile(HttpServletRequest request) {
        String page = request.getParameter("page");
        DataUser dataUser = (DataUser) request.getSession().getAttribute("dataUser");
        String objectKey = request.getParameter("objectKey");
        String rId = request.getParameter("rId");
        String type = request.getParameter("type") == null ? "" : request.getParameter("type");
        Date date = new Date();
        LogOperation logOperation = new LogOperation();
        logOperation.setResourceKey(objectKey);
        logOperation.setUserId(dataUser.getUserId());
        logOperation.setCtime(date);
        logOperation.setType(2);
        logOperation.setIsValid(true);
        int i = getMediaStatus(objectKey);
        if(i != 0){
            try {
                mediaService.deleteMedia(Long.valueOf(rId), logOperation);
            } catch (NumberFormatException e) {
                LogUtil.error("文件"+objectKey+"删除失败",e);
            }
            //BOS删除
            BosClient client = bosClientGetInstance();
            client.deleteObject(BUCKETNAME, "uuid/" + objectKey);
        }
        if (type.equals("2")) {
            return "redirect:/console/doc_list?page=" + page;
        } else if (type.equals("3")) {
            return "redirect:/console/pic_list?page=" + page;
        } else {
            return "redirect:index";
        }
    }

    //查看图片
    @RequestMapping(value = "get_picUrl")
    public String getPicUrl(HttpServletRequest request){
        String objectKey = request.getParameter("objectKey");
        int i = getMediaStatus(objectKey);
        if(i != 0){
            String picUrl = ENDPOINT + "/" + BUCKETNAME + "/uuid/"+objectKey;
            return "redirect:"+picUrl;
        }else {
            request.setAttribute("error","该文件可能已被删除,请刷新文件列表");
            return "errorPage";
        }

    }

    //加密视频播放
    @RequestMapping(value = "encryptVideo_player")
    public String encryptVideoPlayer(HttpServletRequest request) {
        String objectKey = request.getParameter("objectKey");
        String token = getToken(objectKey);
        return "redirect:player?"+"token="+token+"&objectKey="+objectKey;
    }

    @RequestMapping(value = "player")
    public String player(@RequestParam(value = "token") String token,@RequestParam(value = "objectKey") String objectKey,HttpServletRequest request) {
        int i = getMediaStatus(objectKey);
        if(i != 0){
            VodClient vodClient = vodClientGetInstance();
            GenerateMediaDeliveryInfoResponse resp = vodClient.generateMediaDeliveryInfo(objectKey, null);
            // 获取媒资对应的可播放源文件的地址
            String mediaSourceUrl = resp.getFile();
            // 获取媒资对应的封面图片的地址
            String mediaCoverPage = resp.getCover();
            request.setAttribute("sourceUrl", mediaSourceUrl);
            request.setAttribute("coverUrl", mediaCoverPage);
            request.setAttribute("token", token);
            request.setAttribute("ak", ACCESS_KEY_ID);
        }else{
            request.setAttribute("error","该文件可能已被删除,请刷新文件列表");
        }
        return "playEncryptVideo";

    }

    //非加密视频播放
    @RequestMapping(value = "video_player")
    public String videoPlayer(HttpServletRequest request) {
        String objectKey = request.getParameter("objectKey");
        int i = getMediaStatus(objectKey);
        if(i != 0){
            VodClient vodClient = vodClientGetInstance();
            GenerateMediaDeliveryInfoResponse resp = vodClient.generateMediaDeliveryInfo(objectKey, null);
            // 获取媒资对应的可播放源文件的地址
            String mediaSourceUrl = resp.getFile();
            // 获取媒资对应的封面图片的地址
            String mediaCoverPage = resp.getCover();
            request.setAttribute("sourceUrl", mediaSourceUrl);
            request.setAttribute("coverUrl", mediaCoverPage);
            request.setAttribute("ak", ACCESS_KEY_ID);
        }else{
            request.setAttribute("error","该文件可能已被删除,请刷新文件列表");
        }
        return "playVideo";
    }

    //查看资源是否已被删除
    public int getMediaStatus(String objectKey){
        int i = mediaService.findMediaStatusByObjectKy(objectKey);
        return i;
    }

    //web播放器获取验证token
    public String getToken(String objectKey) {
        long expirationTime = System.currentTimeMillis() / 1000 + 3600;
        String token = String.format("%s_%s_%s", getSignature(USERKEY, objectKey, expirationTime), USERID, expirationTime);
        return token;
    }
    /*@RequestMapping(value = "getToken")
    @ResponseBody
    public JsonObject getToken(HttpServletRequest request, HttpServletResponse response) {
        String file = request.getParameter("file");
        long expirationTime = System.currentTimeMillis() / 1000 + 3600;
        String token = String.format("%s_%s_%s", getSignature(USERKEY, file, expirationTime), USERID, expirationTime);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("token", token);
        return jsonObject;
    }*/

    public String getSignature(String userKey, String mediaId, long expirationTime) {
        try {
            String stringToSign = String.format("/%s/%s", mediaId, expirationTime);
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(userKey.getBytes(CHARSET_UTF8), "HmacSHA256"));
            return new String(Hex.encodeHex(mac.doFinal(stringToSign.getBytes(CHARSET_UTF8))));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public String FormetFileSize(long fileS) {//转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public String DateFormat(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = format.format(date);
        return dateStr;
    }
}
