package com.moko.lib.loraiot.entity;


import android.os.Parcel;
import android.os.Parcelable;

public class SyncDevice implements Parcelable {

    public String macName;
    public String mac;
    public String lastWill;
    public String publishTopic;
    public String subscribeTopic;
    // MKGW-mini 01 10
    // MKGW-mini 03-20D 11
    // MKGW-mini 02-32D 12
    // MK107 20
    // MK107 Pro-32D 25
    // MK107D Pro-35D 30
    // MK110 Plus 01 40
    // MK110 Plus 02 50
    // MK110 Plus 03 60
    // MKGW3 70
    // MKGW1 80
    // LW003-B 90
    // SGWP-B 100
    // MKGW4 200
    // PS101-M 210
    public String model;

    public boolean isSelected;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.macName);
        dest.writeString(this.mac);
        dest.writeString(this.lastWill);
        dest.writeString(this.publishTopic);
        dest.writeString(this.subscribeTopic);
        dest.writeString(this.model);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.macName = source.readString();
        this.mac = source.readString();
        this.lastWill = source.readString();
        this.publishTopic = source.readString();
        this.subscribeTopic = source.readString();
        this.model = source.readString();
        this.isSelected = source.readByte() != 0;
    }

    public SyncDevice() {
    }

    protected SyncDevice(Parcel in) {
        this.macName = in.readString();
        this.mac = in.readString();
        this.lastWill = in.readString();
        this.publishTopic = in.readString();
        this.subscribeTopic = in.readString();
        this.model = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<SyncDevice> CREATOR = new Creator<SyncDevice>() {
        @Override
        public SyncDevice createFromParcel(Parcel source) {
            return new SyncDevice(source);
        }

        @Override
        public SyncDevice[] newArray(int size) {
            return new SyncDevice[size];
        }
    };
}
