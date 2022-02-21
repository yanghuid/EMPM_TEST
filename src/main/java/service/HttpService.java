package service;

import common.RetryIntercepter;
import net.sf.json.JSONObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpService {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * @param urlType url类型 --省市区
     * @return 返回 map参数 key 对应代码，value对应中文地名
     * @throws IOException
     */
    public Map<String, Object> getRequest(String urlType, String upId) throws IOException {
        OkHttpClient mClient;
        mClient = new OkHttpClient.Builder()
                .addInterceptor(new RetryIntercepter(4))//重试
                .build();
        Request request = new Request.Builder()
                .url(urlAddress(urlType, upId))
                .build();

        Response response = mClient.newCall(request).execute();
        Map<String, Object> map = new HashMap<>();
        if (response.isSuccessful()) {
            JSONObject jsonary = JSONObject.fromObject(response.body().string());
            Iterator<String> iterator = jsonary.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = jsonary.get(key);
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * 拼接url地址
     *
     * @param urlType url类型 --省市区
     * @param upId    -- 上级代码id
     * @return 返回url地址
     */
    private String urlAddress(String urlType, String upId) {
        String retUrl;
        switch (urlType) {
            case "province":
                retUrl = "http://www.weather.com.cn/data/city3jdata/china.html";
                break;
            case "city":
                retUrl = "http://www.weather.com.cn/data/city3jdata/provshi/" + upId + ".html";
                break;
            case "county":
                retUrl = "http://www.weather.com.cn/data/city3jdata/station/" + upId + ".html";
                break;
            case "temperature":
                retUrl = "http://www.weather.com.cn/data/sk/" + upId + ".html";
                break;
            default:
                throw new IllegalStateException("获取不到url: " + urlType);
        }
        return retUrl;
    }
}
