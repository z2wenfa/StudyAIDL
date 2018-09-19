package com.z2wenfa.aidldemo.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.z2wenfa.aidldemo.App;
import com.z2wenfa.aidldemo.IBinderManager;
import com.z2wenfa.aidldemo.BinderPoolService;

public class BinderPool {
    public static final int BOOKMANAGER = 1;
    public static final int CALCULATEMANAGER = 2;

    private static BinderPool binderPool;

    private BinderPool() {
        bindService();
    }


    public static BinderPool getInstance() {
        binderPool =new BinderPool();

        return binderPool;
    }

    public IBinder getBinderByTag(int tag){
        if(binderManager==null)return null;
        try {
            return binderManager.getBinderByTag(tag);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    private IBinderManager binderManager;

    private void bindService() {
        Intent intent=new Intent();
        intent.setClass(App.getInstance(),BinderPoolService.class);
        App.getInstance().bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection =new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binderManager=IBinderManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
