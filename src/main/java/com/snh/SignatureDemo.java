package com.snh;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import com.baidubce.BceClientConfiguration;
import com.baidubce.auth.BceCredentials;
import com.baidubce.auth.BceV1Signer;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.auth.SignOptions;
import com.baidubce.http.Headers;
import com.baidubce.http.HttpMethodName;
import com.baidubce.internal.InternalRequest;
import com.baidubce.services.sts.StsClient;
import com.baidubce.services.sts.model.GetSessionTokenRequest;
import com.baidubce.services.sts.model.GetSessionTokenResponse;
import com.baidubce.util.DateUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SignatureDemo {
    private static String STS_ENDPOINT = Config.STS_ENDPOINT;
    private static String UTF8 = "utf-8";

    private String ak;
    private String sk;

    public SignatureDemo(String ak, String sk) {
        this.ak = ak;
        this.sk = sk;
    }

    private boolean isEmpty(String x) {
        return x == null || x.length() <= 0;
    }

    private String getStsToken(String sts) {
        System.out.println(sts);
        BceCredentials credentials = new DefaultBceCredentials(ak, sk);
        BceClientConfiguration config = new BceClientConfiguration()
                .withEndpoint(STS_ENDPOINT).withCredentials(credentials);
        StsClient client = new StsClient(config);

        GetSessionTokenRequest request = new GetSessionTokenRequest();
        request.setAcl(sts);
        request.setDurationSeconds(24 * 60 * 60);

        GetSessionTokenResponse response = client.getSessionToken(request);

        String pattern = "{" + "\"AccessKeyId\":\"%s\","
                + "\"SecretAccessKey\": \"%s\"," + "\"SessionToken\": \"%s\","
                + "\"Expiration\": \"%s\"" + "}";

        return String.format(pattern, response.getAccessKeyId(),
                response.getSecretAccessKey(), response.getSessionToken(),
                DateUtils.formatAlternateIso8601Date(response.getExpiration()));
    }

    private String getPolicySignature(String policy)
            throws UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {
        String policyBase64 = Base64.encodeBase64String(policy.getBytes(UTF8));
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(sk.getBytes(UTF8), "HmacSHA256"));
        String policySignature = new String(Hex.encodeHex(mac
                .doFinal(policyBase64.getBytes(UTF8))));

        String pattern = "{" + "\"policy\":\"%s\"," + "\"signature\": \"%s\","
                + "\"accessKey\": \"%s\"}";
        return String.format(pattern, policyBase64, policySignature, ak);
    }

    private String getNormalSignature(String httpMethod, String path,
            String queries, String headers) throws URISyntaxException,
            JsonParseException, JsonMappingException, IOException {
        URI uri = new URI("http://www.baidu.com" + path);
        InternalRequest request = new MyInternalRequest(
                HttpMethodName.valueOf(httpMethod), uri);

        if (!isEmpty(headers)) {
            ObjectMapper mapper = new ObjectMapper();
            Map<?, ?> map = mapper.readValue(headers, Map.class);
            Iterator<?> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String name = (String) iterator.next();
                String value = map.get(name).toString();
                request.addHeader(name, value);
            }
        }

        if (!isEmpty(queries)) {
            ObjectMapper mapper = new ObjectMapper();
            Map<?, ?> map = mapper.readValue(queries, Map.class);
            Iterator<?> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String name = (String) iterator.next();
                String value = map.get(name).toString();
                request.addParameter(name, value);
            }
        }

        SignOptions options = new SignOptions();
        options.setTimestamp(new Date());

        BceCredentials c = new DefaultBceCredentials(ak, sk);
        BceV1Signer bceV1Signer = new BceV1Signer();
        bceV1Signer.sign(request, c, options);
        String authorization = request.getHeaders().get(Headers.AUTHORIZATION);

        String xbceDate = DateUtils.formatAlternateIso8601Date(options
                .getTimestamp());

        String pattern = "{" + "\"statusCode\":200," + "\"signature\": \"%s\","
                + "\"xbceDate\": \"%s\"}";
        return String.format(pattern, authorization, xbceDate);
    }

    public String doIt(String httpMethod, String path, String queries,
            String headers, String policy, String sts, String callback) {
        String result = null;

        if (!isEmpty(sts)) {
            result = this.getStsToken(sts);
        } else if (!isEmpty(policy)) {
            try {
                result = this.getPolicySignature(policy);
            } catch (InvalidKeyException | UnsupportedEncodingException
                    | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else if (!isEmpty(httpMethod) && !isEmpty(path) && !isEmpty(headers)) {
            try {
                result = this.getNormalSignature(httpMethod, path, queries,
                        headers);
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        } else {
            result = "{\"statusCode\": 403}";
        }

        if (!isEmpty(callback)) {
            result = callback + "(" + result + ")";
        }

        return result;
    }
}
class MyInternalRequest extends InternalRequest {
    private boolean x = false;

    public MyInternalRequest(HttpMethodName httpMethod, URI uri) {
        super(httpMethod, uri);
    }

    public void addHeader(String name, String value) {
        if (name.equalsIgnoreCase(Headers.HOST)) {
            // Host只能被设置一次，绕过 BceV1Signer 里面的一个问题
            if (x) {
                return;
            }
            x = true;
        }

        super.addHeader(name, value);
    }
}