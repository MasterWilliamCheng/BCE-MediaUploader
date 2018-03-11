package com.snh;

import com.snh.util.SpringPropertiesUtil;

public class Config {

    public static final String ACCESS_KEY_ID = SpringPropertiesUtil.getProperty("ACCESS_KEY_ID");
    public static final String SECRET_ACCESS_KEY = SpringPropertiesUtil.getProperty("SECRET_ACCESS_KEY");
    public static final String USERKEY = SpringPropertiesUtil.getProperty("USERKEY");
    public static final String USERID = SpringPropertiesUtil.getProperty("USERID");
    public static final String URL = SpringPropertiesUtil.getProperty("URL");
    public static final String STS_ENDPOINT = SpringPropertiesUtil.getProperty("STS_ENDPOINT");
    public static final String BOS_BUCKET = SpringPropertiesUtil.getProperty("BOS_BUCKET");
    public static final String BOS_ENDPOINT = SpringPropertiesUtil.getProperty("BOS_ENDPOINT");
    public static final String VOD_ENDPOINT = SpringPropertiesUtil.getProperty("VOD_ENDPOINT");
    public static final String TRANSCODING_PRESETGROUPNAME_ENCRYPT = SpringPropertiesUtil.getProperty("TRANSCODING_PRESETGROUPNAME_ENCRYPT");
    public static final String TRANSCODING_PRESETGROUPNAME_NOENCRYPT = SpringPropertiesUtil.getProperty("TRANSCODING_PRESETGROUPNAME_NOENCRYPT");
}
