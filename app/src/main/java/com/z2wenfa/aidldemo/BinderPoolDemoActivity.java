package com.z2wenfa.aidldemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.z2wenfa.aidldemo.manager.BinderPool;

public class BinderPoolDemoActivity extends Activity {
    private BinderPool binderPool;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binderPool=BinderPool.getInstance();
        requestPermissions(new String[]{"com.z2wenfa.aidldemo.permission"},200);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binderManagerTest();
            }
        },1000);
    }

    private void binderManagerTest() {
        IBinder iBinder=binderPool.getBinderByTag(BinderPool.BOOKMANAGER);
        if(iBinder!=null){
            IBookManager bookManager=IBookManager.Stub.asInterface(iBinder);
            try {
                bookManager.addBook(new Book());
                Log.d("test","bookcount is "+bookManager.getAllBooks().size());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        IBinder iBinder1=binderPool.getBinderByTag(BinderPool.CALCULATEMANAGER);
        if(iBinder1!=null){
            ICalculateManager calculateManager=ICalculateManager.Stub.asInterface(iBinder1);
            try {
                Log.d("test","maxnum is "+calculateManager.getMaxNum(10,20));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
