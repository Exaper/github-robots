package com.exaper.robots.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class RobotDetails extends Robot {
    @SerializedName(value = "name")
    private final String mName;

    @SerializedName(value = "email")
    private final String mEmail;

    @SerializedName(value = "type")
    private final String mType;

    private RobotDetails(Parcel parcel) {
        super(parcel);
        mName = parcel.readString();
        mEmail = parcel.readString();
        mType = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeString(mName);
        parcel.writeString(mEmail);
        parcel.writeString(mType);
    }

    public static final Parcelable.Creator<RobotDetails> CREATOR =
            new Parcelable.Creator<RobotDetails>() {
                @Override
                public RobotDetails createFromParcel(Parcel in) {
                    return new RobotDetails(in);
                }

                @Override
                public RobotDetails[] newArray(int size) {
                    return new RobotDetails[size];
                }
            };
}
