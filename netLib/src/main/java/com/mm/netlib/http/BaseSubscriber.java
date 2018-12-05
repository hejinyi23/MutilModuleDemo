package com.mm.netlib.http;

import android.content.Context;
import android.widget.Toast;

import com.mm.netlib.exception.NetErrors;
import com.mm.netlib.exception.ResponseThrowable;
import com.mm.netlib.utils.NetworkUtil;

import io.reactivex.observers.DisposableObserver;


/**
 * 该类仅供参考，实际业务Code, 根据需求来定义，
 */
public abstract class BaseSubscriber<T> extends DisposableObserver<T> {
    public abstract void onResult(T t);

    private Context context;
    private boolean isNeedCahe;

    public BaseSubscriber(Context context) {
        this.context = context;
    }

    @Override
    public void onError(Throwable e) {

        if (e instanceof ResponseThrowable) {
            onError((ResponseThrowable) e);
        } else {
            onError(new ResponseThrowable(e,NetErrors.UNKNOWN));
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        Toast.makeText(context, "http is start", Toast.LENGTH_SHORT).show();

        if (!NetworkUtil.isNetworkAvailable(context)) {
            Toast.makeText(context, "无网络，读取缓存数据", Toast.LENGTH_SHORT).show();
            onComplete();
        }

    }

    @Override
    public void onComplete() {

        Toast.makeText(context, "http is Complete", Toast.LENGTH_SHORT).show();

    }


    public abstract void onError(ResponseThrowable e);

    @Override
    public void onNext(Object o) {
        BaseResponse baseResponse = (BaseResponse) o;
        if (baseResponse.getCode() == 200) {
            onResult((T) baseResponse.getData());
        } else if (baseResponse.getCode() == 330) {

        } else if (baseResponse.getCode() == 503) {

        } else {

        }
    }
}
