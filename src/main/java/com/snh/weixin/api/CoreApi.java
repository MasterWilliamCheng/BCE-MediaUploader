package com.snh.weixin.api;

import com.google.gson.GsonBuilder;
import com.snh.weixin.WxConfig;
import com.squareup.okhttp.OkHttpClient;
import org.apache.log4j.Logger;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.converter.SimpleXMLConverter;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * API服务创建层
 */
public class CoreApi {
    private static final Logger log = Logger.getLogger(CoreApi.class);
    public static OkHttpClient httpClient = new OkHttpClient();
    public static RestAdapter jsonAdapter;
    public static RestAdapter xmlAdapter;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder = builder.disableHtmlEscaping();
        SSLContext sslContext = sslContextForTrustedCertificates();
        httpClient.setSslSocketFactory(sslContext.getSocketFactory());
        jsonAdapter = new RestAdapter.Builder()
                .setEndpoint(WxConfig.WEIXIN_API_SERVER)
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .setClient(new OkClient(httpClient))
                .setConverter(new GsonConverter(builder.create()))
                .build();
        xmlAdapter = new RestAdapter.Builder()
                .setEndpoint(WxConfig.WEIXIN_API_SERVER)
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .setClient(new OkClient(httpClient))
                .setConverter(new SimpleXMLConverter())
                .build();
    }

    private CoreApi() {
    }


    public static <S> S create(Class<S> serviceClass) {
        String classPackage = serviceClass.getPackage().getName();
        switch (classPackage) {
            case "com.tencent.api.weixin.json":
                return jsonAdapter.create(serviceClass);
            case "com.tencent.api.weixin.xml":
                return xmlAdapter.create(serviceClass);
            default:
                return jsonAdapter.create(serviceClass);
        }
    }

    public static SSLContext sslContextForTrustedCertificates() {
        try {
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            return sslContext;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }


}


/**
 * 证书信任管理器（用于https请求）
 *
 * @author caspar.chen
 * @version 1.0
 */
class MyX509TrustManager implements X509TrustManager {

    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}

