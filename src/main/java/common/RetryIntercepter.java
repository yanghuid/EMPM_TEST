package common;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * 重试拦截器 okhttp自带重试机制，我们这边自定义重试次数
 */
public class RetryIntercepter implements Interceptor {

    public int maxRetry;//最大重试次数
    private int retryNum = 0;

    public RetryIntercepter(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        System.out.println("retryNum=" + retryNum);
        Response response = chain.proceed(request);
        while (!response.isSuccessful() && retryNum < maxRetry) {
            retryNum++;
            System.out.println("retryNum=" + retryNum);
            response = chain.proceed(request);
        }
        return response;
    }
}
