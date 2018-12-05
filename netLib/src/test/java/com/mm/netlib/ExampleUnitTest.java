package com.mm.netlib;

import com.mm.netlib.download.DownLoadManager;
import com.mm.netlib.download.ProgressCallBack;

import org.junit.Test;

import okhttp3.ResponseBody;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void testDownLoadManager(String loadUrl, String destFileDir, String destFileName) {
        DownLoadManager.getInstance().load(loadUrl, new ProgressCallBack<ResponseBody>(destFileDir, destFileName) {
            @Override
            public void onStart() {
                //RxJava的onStart()
            }

            @Override
            public void onCompleted() {
                //RxJava的onCompleted()
            }

            @Override
            public void onSuccess(ResponseBody responseBody) {
                //下载成功的回调
            }

            @Override
            public void progress(final long progress, final long total) {
                //下载中的回调 progress：当前进度 ，total：文件总大小
            }

            @Override
            public void onError(Throwable e) {
                //下载错误回调
            }
        });
    }


}