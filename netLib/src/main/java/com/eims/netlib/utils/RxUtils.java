package com.eims.netlib.utils;


import com.eims.netlib.http.BaseResponse;
import com.eims.netlib.exception.ExceptionHandle;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by goldze on 2017/6/19.
 * 有关Rx的工具类
 */
public class RxUtils {
//    /**
//     * 生命周期绑定
//     *
//     * @param lifecycle Activity
//     */
//    public static LifecycleTransformer bindToLifecycle(@NonNull Context lifecycle) {
//        if (lifecycle instanceof LifecycleProvider) {
//            return ((LifecycleProvider) lifecycle).bindToLifecycle();
//        } else {
//            throw new IllegalArgumentException("context not the LifecycleProvider type");
//        }
//    }
//
//    /**
//     * 生命周期绑定
//     *
//     * @param lifecycle Fragment
//     */
//    public static <T> LifecycleTransformer bindToLifecycle(@NonNull Fragment lifecycle) {
//        if (lifecycle instanceof LifecycleProvider) {
//            return ((LifecycleProvider) lifecycle).bindToLifecycle();
//        } else {
//            throw new IllegalArgumentException("fragment not the LifecycleProvider type");
//        }
//    }
//
//    /**
//     * 生命周期绑定
//     *
//     * @param lifecycle Fragment
//     */
//    public static <T> LifecycleTransformer bindToLifecycle(@NonNull LifecycleProvider lifecycle) {
//        return lifecycle.bindToLifecycle();
//    }

    /**
     * 线程调度器
     */
    public static <T> ObservableTransformer<BaseResponse<T>, BaseResponse<T>> schedulersTransformer() {
        return new ObservableTransformer<BaseResponse<T>, BaseResponse<T>>() {
            @Override
            public ObservableSource<BaseResponse<T>> apply(Observable<BaseResponse<T>> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 异常处理
     */
    public static <T> ObservableTransformer<BaseResponse<T>, BaseResponse<T>> exceptionTransformer() {

        return new ObservableTransformer<BaseResponse<T>, BaseResponse<T>>() {

            @Override
            public ObservableSource<BaseResponse<T>> apply(Observable<BaseResponse<T>> upstream) {
                return upstream.onErrorResumeNext(new HttpResponseFunc<BaseResponse<T>>());
            }
        };

    }

    private static class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {
        @Override
        public Observable<T> apply(Throwable t) {
            return Observable.error(ExceptionHandle.handleException(t));
        }
    }

    private static class HandleFuc<T> implements Function<BaseResponse<T>, T> {
        @Override
        public T apply(BaseResponse<T> response) {
            if (!response.isOk())
                throw new RuntimeException(!"".equals(response.getCode() + "" + response.getMessage()) ? response.getMessage() : "");
            return response.getData();
        }
    }

}
