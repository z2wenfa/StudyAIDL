package com.z2wenfa.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BookManagerService extends Service {
    private RemoteCallbackList<IBookCountChangeListener> bookCountChangeListeners = new RemoteCallbackList<>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBookManager.asBinder();
    }

    private CopyOnWriteArrayList<Book> bookList = new CopyOnWriteArrayList<>();

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

}
