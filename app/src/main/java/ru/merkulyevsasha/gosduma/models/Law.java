package ru.merkulyevsasha.gosduma.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Law implements Parcelable{

    public int id;
    public String number;
    public String name;
    public String comments;
    public long introductionDate;

    public int lastEventStageId;
    public int lastEventPhaseId;
    public String lastEventSolution;
    public long lastEventDate;
    public String lastEventDocName;
    public String lastEventDocType;

    public int responsibleId;
    public String responsibleName;

    public String type;

    public Law(){}

    private Law(Parcel in) {
        id = in.readInt();
        number = in.readString();
        name = in.readString();
        comments = in.readString();
        introductionDate = in.readLong();
        lastEventStageId = in.readInt();
        lastEventPhaseId = in.readInt();
        lastEventSolution = in.readString();
        lastEventDate = in.readLong();
        lastEventDocName = in.readString();
        lastEventDocType = in.readString();
        responsibleId = in.readInt();
        responsibleName = in.readString();
        type = in.readString();
    }

    public static final Creator<Law> CREATOR = new Creator<Law>() {
        @Override
        public Law createFromParcel(Parcel in) {
            return new Law(in);
        }

        @Override
        public Law[] newArray(int size) {
            return new Law[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(number);
        parcel.writeString(name);
        parcel.writeString(comments);
        parcel.writeLong(introductionDate);
        parcel.writeInt(lastEventStageId);
        parcel.writeInt(lastEventPhaseId);
        parcel.writeString(lastEventSolution);
        parcel.writeLong(lastEventDate);
        parcel.writeString(lastEventDocName);
        parcel.writeString(lastEventDocType);
        parcel.writeInt(responsibleId);
        parcel.writeString(responsibleName);
        parcel.writeString(type);
    }

    public String getLawNameWithNumberAndDate(){
        StringBuilder sb = new StringBuilder();
        if (introductionDate > 0) {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            sb.append(number);
            sb.append(" (");
            sb.append(format.format(new Date(introductionDate)));
            sb.append(")");
        } else {
            sb.append(number);
        }
        sb.append(" ");
        sb.append(name);

        return sb.toString();
    }
}
