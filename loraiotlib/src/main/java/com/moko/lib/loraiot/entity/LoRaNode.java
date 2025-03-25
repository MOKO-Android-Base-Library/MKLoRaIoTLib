package com.moko.lib.loraiot.entity;


import android.os.Parcel;
import android.os.Parcelable;

public class LoRaNode implements Parcelable {

    public String devEUI;
    public String appEUI;
    public String appKey;
    public String region;
    // LW001-BG PRO(L) 10
    // LW001-BG PRO(M) 20
    // LW004-PB 30
    // LW005-MP 40
    // LW006 45
    // LW007-PIR 50
    // LW008-MT 60
    public String model;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.devEUI);
        dest.writeString(this.appEUI);
        dest.writeString(this.appKey);
        dest.writeString(this.region);
        dest.writeString(this.model);
    }

    public void readFromParcel(Parcel source) {
        this.devEUI = source.readString();
        this.appEUI = source.readString();
        this.appKey = source.readString();
        this.region = source.readString();
        this.model = source.readString();
    }

    public LoRaNode() {
    }

    protected LoRaNode(Parcel in) {
        this.devEUI = in.readString();
        this.appEUI = in.readString();
        this.appKey = in.readString();
        this.region = in.readString();
        this.model = in.readString();
    }

    public static final Creator<LoRaNode> CREATOR = new Creator<LoRaNode>() {
        @Override
        public LoRaNode createFromParcel(Parcel source) {
            return new LoRaNode(source);
        }

        @Override
        public LoRaNode[] newArray(int size) {
            return new LoRaNode[size];
        }
    };
}
