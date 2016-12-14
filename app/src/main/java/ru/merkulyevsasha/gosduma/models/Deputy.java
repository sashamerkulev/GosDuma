package ru.merkulyevsasha.gosduma.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Deputy implements Parcelable {

    public int id;
    public String name;
    public String position;
    public boolean isCurrent;

    public Date birthdate;
    public Date credentialsStart;
    public Date credentialsEnd;

    public String fractionName;
    public String fractionRole;
    public String fractionRegion;

    public Deputy(){}

    protected Deputy(Parcel in) {
        id = in.readInt();
        name = in.readString();
        position = in.readString();
        isCurrent = in.readByte() != 0;
        fractionName = in.readString();
        fractionRole = in.readString();
        fractionRegion = in.readString();
    }

    public static final Creator<Deputy> CREATOR = new Creator<Deputy>() {
        @Override
        public Deputy createFromParcel(Parcel in) {
            return new Deputy(in);
        }

        @Override
        public Deputy[] newArray(int size) {
            return new Deputy[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(position);
        parcel.writeByte((byte) (isCurrent ? 1 : 0));
        parcel.writeString(fractionName);
        parcel.writeString(fractionRole);
        parcel.writeString(fractionRegion);
    }
}
