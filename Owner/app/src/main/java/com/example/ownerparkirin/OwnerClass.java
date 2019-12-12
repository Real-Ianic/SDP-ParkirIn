package com.example.ownerparkirin;

import android.os.Parcel;
import android.os.Parcelable;

public class OwnerClass implements Parcelable {
    private String name;
    private String email;
    private String password;
    private String nohp;


    public OwnerClass()
    {

    }

    protected OwnerClass(Parcel in) {
        name = in.readString();
        email = in.readString();
        password = in.readString();
        nohp = in.readString();
    }

    public static final Creator<OwnerClass> CREATOR = new Creator<OwnerClass>() {
        @Override
        public OwnerClass createFromParcel(Parcel in) {
            return new OwnerClass(in);
        }

        @Override
        public OwnerClass[] newArray(int size) {
            return new OwnerClass[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(nohp);
    }
}
