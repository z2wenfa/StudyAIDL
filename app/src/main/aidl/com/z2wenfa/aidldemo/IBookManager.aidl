// IBookManager.aidl
package com.z2wenfa.aidldemo;

import com.z2wenfa.aidldemo.Book;
import com.z2wenfa.aidldemo.IBookCountChangeListener;

interface IBookManager {
    void addBook(in Book book);
    List<Book> getAllBooks();
    void registerBookCountChangeListener(IBookCountChangeListener bookCountChangeListener);
    void unRegisterBookCountChangeListener(IBookCountChangeListener bookCountChangeListener);
}
