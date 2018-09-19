package com.z2wenfa.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BinderPoolService extends Service {
    public static final int BOOKMANAGER = 1;
    public static final int CALCULATEMANAGER = 2;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int checkPermission=checkSelfPermission("com.z2wenfa.aidldemo.permission");
        Log.d("test","checkPermission:"+checkPermission);
        if(checkPermission== PackageManager.PERMISSION_DENIED)return null;
        return iBinderManager.asBinder();
    }

    IBinderManager iBinderManager =new IBinderManager.Stub(){

        @Override
        public IBinder getBinderByTag(int tag) throws RemoteException {
            switch (tag){
                case BOOKMANAGER:
                    return iBookManager.asBinder();
                case CALCULATEMANAGER:
                    return iCalculateManager.asBinder();
            }
            return null;
        }
    };


    private CopyOnWriteArrayList<Book> bookList = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IBookCountChangeListener> bookCountChangeListeners = new RemoteCallbackList<>();

    IBookManager iBookManager = new IBookManager.Stub() {

        @Override
        public void addBook(Book book) throws RemoteException {
            bookList.add(book);
            int listenerCount = bookCountChangeListeners.beginBroadcast();
            for (int i = 0; i < listenerCount; i++) {
                bookCountChangeListeners.getBroadcastItem(i).onBookCountChanged(bookList.size());
            }
            bookCountChangeListeners.finishBroadcast();
        }

        @Override
        public List<Book> getAllBooks() throws RemoteException {
            return bookList;
        }

        @Override
        public void registerBookCountChangeListener(IBookCountChangeListener bookCountChangeListener) throws RemoteException {
            bookCountChangeListeners.register(bookCountChangeListener);
        }

        @Override
        public void unRegisterBookCountChangeListener(IBookCountChangeListener bookCountChangeListener) throws RemoteException {
            bookCountChangeListeners.unregister(bookCountChangeListener);
        }
    };

    ICalculateManager iCalculateManager=new ICalculateManager.Stub() {
        @Override
        public float getMaxNum(int value1, int value2) throws RemoteException {
            return value1>value2?value1:value2;
        }
    };
}
