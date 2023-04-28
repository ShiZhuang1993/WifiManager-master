package com.iwdael.wifimanager;

import android.os.Parcel;
import android.os.Parcelable;

public interface IWifi extends Parcelable {

    String name();

    boolean isEncrypt();

    boolean isSaved();

    boolean isConnected();

    String encryption();

    int level();

    String description();

    String ip();

    String description2();

    void state(String state);

    @Deprecated
    String SSID();

    @Deprecated
    String capabilities();

    @Deprecated
    IWifi merge(IWifi merge);

    String state();

    @Override
    int describeContents();

    @Override
    void writeToParcel(Parcel dest, int flags);
}
