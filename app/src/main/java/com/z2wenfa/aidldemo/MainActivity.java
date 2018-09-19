package com.z2wenfa.aidldemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService();
    }

    private void bindService() {
        Intent intent=new Intent();
        intent.setClass(this,BookManagerService.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
    }

    private void addBook() {
        try {
            bookManager.addBook(new Book());
            int bookCount =bookManager.getAllBooks().size();
            Log.d("test","bookCount is "+bookCount);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addBook();
                }
            },1000);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private IBookManager bookManager;

    private ServiceConnection serviceConnection =new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //运行在UI线程中
            bookManager=IBookManager.Stub.asInterface(service);
            addBook();
            try {
                bookManager.registerBookCountChangeListener(bookCountChangeListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //运行在UI线程中
//            bookManager.asBinder().unlinkToDeath(deathRecipient,0);
//            bookManager=null;
//            bindService();
        }
    };

    private IBinder.DeathRecipient deathRecipient =new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            //运行在Binder池中
            bookManager.asBinder().unlinkToDeath(deathRecipient,0);
            bookManager=null;
            bindService();
        }
    };

    private IBookCountChangeListener bookCountChangeListener =new IBookCountChangeListener.Stub() {
        @Override
        public void onBookCountChanged(int bookCount) throws RemoteException {
            Log.d("test","onBookCountChanged bookCount is "+bookCount);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bookManager!=null) {
            try {
                bookManager.unRegisterBookCountChangeListener(bookCountChangeListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
