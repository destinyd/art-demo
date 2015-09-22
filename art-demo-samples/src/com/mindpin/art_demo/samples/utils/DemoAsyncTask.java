package com.mindpin.art_demo.samples.utils;

import android.content.Context;
import android.widget.Toast;
//import com.realityandapp.paopao_official_deliveryman.networks.HttpApi;
import roboguice.util.RoboAsyncTask;

/**
 * Created by fushang318 on 2014/8/13.
 */
public abstract class DemoAsyncTask<ResultT> extends RoboAsyncTask<ResultT> {

    protected DemoAsyncTask(Context context) {
        super(context);
    }

//    @Override
//    protected void onException(Exception e) throws RuntimeException {
//
//        if(e.getClass() == HttpApi.RequestDataErrorException.class){
//            Toast.makeText(getContext(), "服务器数据错误，请稍后再试", Toast.LENGTH_SHORT).show();
//        }
//
//        if(e.getClass() == HttpApi.AuthErrorException.class){
//            Toast.makeText(getContext(), "用户认证失败，请重新登陆", Toast.LENGTH_SHORT).show();
//        }
//
//        if(e.getClass() == HttpApi.NetworkErrorException.class){
//            Toast.makeText(getContext(), "网络连接失败，请稍后再试", Toast.LENGTH_SHORT).show();
//        }
//
//        e.printStackTrace();
//    }
}
