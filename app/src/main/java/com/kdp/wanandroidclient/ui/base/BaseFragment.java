package com.kdp.wanandroidclient.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kdp.wanandroidclient.event.RxEvent;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;

/**
 * author: 康栋普
 * date: 2018/2/11
 */

public abstract class BaseFragment extends Fragment {
    private PublishSubject mSubject;
    private RxEvent mRxEvent;
    private DisposableObserver mDisposableObserver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
            getBundle(bundle);
        mRxEvent = RxEvent.getInstance();
        mDisposableObserver = new ReceiveEvent();
        //注册事件
        mSubject = mRxEvent.registerEvent(registerEvent());
        mSubject.subscribe(mDisposableObserver);
    }

    private class ReceiveEvent extends DisposableObserver {
        @Override
        public void onNext(Object o) {
            receiveEvent(o);
        }
        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onComplete() {
        }
    }

    protected abstract void receiveEvent(Object object);

    protected abstract String registerEvent();


    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销事件
        RxEvent.getInstance().unRegisterEvent(registerEvent(), mSubject, mDisposableObserver);
    }

    protected abstract void getBundle(Bundle bundle);


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        int layoutId = getLayoutId();
        if (layoutId != 0) {
            view = inflater.inflate(getLayoutId(), container, false);
            initViews(view);
        }
        return view;
    }

    protected abstract void initViews(View view);

    protected abstract int getLayoutId();


}
