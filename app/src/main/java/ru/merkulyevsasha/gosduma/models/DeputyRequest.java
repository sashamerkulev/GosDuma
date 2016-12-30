package ru.merkulyevsasha.gosduma.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DeputyRequest implements Parcelable{

    public int requestId;
    public String initiator;
    private String initiator_lowcase;
    public long requestDate;
    public String name;
    private String name_lowcase;
    private String controlDate;
    private String signedDate;
    public String documentNumber;
    public String resolution;
    public String answer;
    private String resolution_lowcase;
    private String answer_lowcase;

    private int signedBy_id;
    public String signedBy_name;
    private String signedBy_name_lowcase;
    private int addressee_id;
    public String addressee_name;
    private String addressee_name_lowcase;

    public DeputyRequest(){}

    private DeputyRequest(Parcel in) {
        requestId = in.readInt();
        initiator = in.readString();
        initiator_lowcase = in.readString();
        requestDate = in.readLong();
        name = in.readString();
        name_lowcase = in.readString();
        controlDate = in.readString();
        signedDate = in.readString();
        documentNumber = in.readString();
        resolution = in.readString();
        answer = in.readString();
        resolution_lowcase = in.readString();
        answer_lowcase = in.readString();
        signedBy_id = in.readInt();
        signedBy_name = in.readString();
        signedBy_name_lowcase = in.readString();
        addressee_id = in.readInt();
        addressee_name = in.readString();
        addressee_name_lowcase = in.readString();
    }

    public static final Creator<DeputyRequest> CREATOR = new Creator<DeputyRequest>() {
        @Override
        public DeputyRequest createFromParcel(Parcel in) {
            return new DeputyRequest(in);
        }

        @Override
        public DeputyRequest[] newArray(int size) {
            return new DeputyRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(requestId);
        parcel.writeString(initiator);
        parcel.writeString(initiator_lowcase);
        parcel.writeLong(requestDate);
        parcel.writeString(name);
        parcel.writeString(name_lowcase);
        parcel.writeString(controlDate);
        parcel.writeString(signedDate);
        parcel.writeString(documentNumber);
        parcel.writeString(resolution);
        parcel.writeString(answer);
        parcel.writeString(resolution_lowcase);
        parcel.writeString(answer_lowcase);
        parcel.writeInt(signedBy_id);
        parcel.writeString(signedBy_name);
        parcel.writeString(signedBy_name_lowcase);
        parcel.writeInt(addressee_id);
        parcel.writeString(addressee_name);
        parcel.writeString(addressee_name_lowcase);
    }

    public String getNameWithNumberAndDate(){
        if (requestDate != 0) {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            return documentNumber + " ("+format.format(new Date(requestDate))+") " + name;
        } else {
            return documentNumber + " " + name;
        }
    }


}
