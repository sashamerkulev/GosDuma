package ru.merkulyevsasha.gosduma.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Deputy implements Parcelable {

    public int id;
    public String name;
    public String position;
    public boolean isCurrent;

    public long birthdate;
    public long credentialsStart;
    public long credentialsEnd;

    public String fractionName;
    public String fractionRole;
    public String fractionRegion;

    public String degrees;
    public String ranks;

    public Deputy(){}

    private Deputy(Parcel in) {
        id = in.readInt();
        birthdate = in.readLong();
        credentialsStart = in.readLong();
        credentialsEnd = in.readLong();
        name = in.readString();
        position = in.readString();
        isCurrent = in.readByte() != 0;
        fractionName = in.readString();
        fractionRole = in.readString();
        fractionRegion = in.readString();
        degrees = in.readString();
        ranks = in.readString();
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
        parcel.writeLong(birthdate);
        parcel.writeLong(credentialsStart);
        parcel.writeLong(credentialsEnd);
        parcel.writeString(name);
        parcel.writeString(position);
        parcel.writeByte((byte) (isCurrent ? 1 : 0));
        parcel.writeString(fractionName);
        parcel.writeString(fractionRole);
        parcel.writeString(fractionRegion);
        parcel.writeString(degrees);
        parcel.writeString(ranks);
    }

    public String getNameWithBirthday(){
        if (birthdate != 0) {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            return name + " ("+format.format(new Date(birthdate))+")";
        } else {
            return name;
        }
    }

    public String getPositionWithStartAndEndDates(){
        final StringBuilder position = new StringBuilder();
        final DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        if (credentialsStart > 0) {
            position.append(position);
            position.append(" (период с ");
            position.append(format.format(new Date(credentialsStart)));
            if (credentialsEnd > 0) {
                position.append(" по ");
                position.append(format.format(new Date(credentialsEnd)));
            }
            position.append(")");
        } else{
            position.append(position);
        }
        return position.toString();
    }

    public String getRanksWithDegrees(){
        if (degrees.isEmpty()){
            return "";
        } else {
            return ranks.isEmpty()? degrees : degrees + " (" + ranks + ")";
        }
    }

    public String getCurrentPosition(){
        if (isCurrent) {
            return "Действующий " + position;
        } else {
            return "Не действующий " + position;
        }
    }
}
