package com.eims.netlib.utils;

import android.content.Context;
import android.text.TextUtils;


import com.eims.netlib.BuildConfig;
import com.eims.netlib.adapter.GsonIntegerDefaultAdapter;
import com.eims.netlib.http.cookie.CookieJarImpl;
import com.eims.netlib.http.cookie.store.PersistentCookieStore;
import com.eims.netlib.http.interceptor.BaseInterceptor;
import com.eims.netlib.http.interceptor.CacheInterceptor;

import com.eims.netlib.http.interceptor.logging.Level;
import com.eims.netlib.http.interceptor.logging.LoggingInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by goldze on 2017/5/10.
 * RetrofitClient封装单例类, 实现网络请求
 */
public class RetrofitClient {
    //服务端根路径
    private static final String baseUrl = "https://www.zihu.com/";
    //超时时间
    private static final int DEFAULT_TIMEOUT = 20;
    //缓存时间
    private static final int CACHE_TIMEOUT = 10 * 1024 * 1024;
    //能挂起的最大连接数量
    private static int MAX_IDLE_CONNECTIONS;

    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;

    private Cache cache = null;
    private File httpCacheDirectory;

    private static Context mContext;


    private static class SingletonHolder {
        private SingletonHolder() {
        }
        private static RetrofitClient INSTANCE = new RetrofitClient();

    }


    public static void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public static RetrofitClient getInstance() {
        if (mContext == null) {
            return null;
        }
        return SingletonHolder.INSTANCE;
    }


    private RetrofitClient() {
        this(baseUrl, null);
    }

    private RetrofitClient(String url, Map<String, String> headers) {
        //网络请求属于io密集，不妨多分配点
        MAX_IDLE_CONNECTIONS = Runtime.getRuntime().availableProcessors() * 2;
        initOKHttpClient(headers);
        initRetrofit(url);
    }


    private void initOKHttpClient(Map<String, String> headers) {
        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(getAppContext().getCacheDir(), "net_cache");
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, CACHE_TIMEOUT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();

        okHttpClient = new OkHttpClient.Builder()
                //cookie处理
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(getAppContext())))
                .cache(cache)
                //添加请求头拦截器
                .addInterceptor(new BaseInterceptor(headers))
                //缓存拦截器，当没有网络连接的时候自动读取缓存中的数据，缓存存放时间默认为3天
                .addInterceptor(new CacheInterceptor(getAppContext()))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                // 这里你可以根据自己的机型设置同时连接的个数和每个保持时间为15s
                .connectionPool(new ConnectionPool(MAX_IDLE_CONNECTIONS, 15, TimeUnit.SECONDS))
                .addInterceptor(new LoggingInterceptor
                        .Builder()
                        .loggable(BuildConfig.DEBUG) //是否开启日志打印
                        .setLevel(Level.BASIC) //打印的等级
                        .log(Platform.INFO) // 打印类型
                        .request("Request") // request的Tag
                        .response("Response")// Response的Tag
                        .addHeader("log-header", "I am the log request header.") // 添加打印头, 注意 key 和 value 都不能是中文
                        .build()
                )
                .build();
    }


    private void initRetrofit(String url) {
        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(buildGson()))//服务器返回的json字符串转化为对象
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//使用Observable配置
                .baseUrl(url)
                .build();
    }

    private static Gson buildGson() {
        return new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(int.class, new GsonIntegerDefaultAdapter())
                .create();
    }


    private Context getAppContext() {
        return mContext;
    }

    /**
     * 初始化Request
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     *
     * @param api 定义的请求的服务器的API封装类
     */
    public <T> T create(final Class<T> api) {
        if (api == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(api);
    }

    /**
     * /**
     * execute your customer API
     * For example:
     * MyApiService service =
     * RetrofitClient.getInstance(MainActivity.this).create(MyApiService.class);
     * <p>
     * RetrofitClient.getInstance(MainActivity.this)
     * .execute(service.lgon("name", "password"), subscriber)
     * * @param subscriber
     */

    public static <T> T execute(Observable<T> observable, Observer<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return null;
    }


}
