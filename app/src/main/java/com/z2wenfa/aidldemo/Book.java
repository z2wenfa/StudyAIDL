package com.z2wenfa.aidldemo;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable{
    private String name;
    private float price;
    private int num;

    public Book() {
    }

    protected Book(Parcel in) {
        name = in.readString();
        price = in.readFloat();
        num = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeFloat(price);
        dest.writeInt(num);
    }

}
