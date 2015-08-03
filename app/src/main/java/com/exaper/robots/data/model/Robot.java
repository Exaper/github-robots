package com.exaper.robots.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Robot implements Parcelable {
    @SerializedName(value = "id")
    private final long mId;

    @SerializedName(value = "login")
    private final String mLogin;

    protected Robot(Parcel parcel) {
        mId = parcel.readLong();
        mLogin = parcel.readString();
    }

    public long getId() {
        return mId;
    }

    public String getLogin() {
        return mLogin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(mId);
        parcel.writeString(mLogin);
    }

    public static final Parcelable.Creator<Robot> CREATOR =
            new Parcelable.Creator<Robot>() {
                @Override
                public Robot createFromParcel(Parcel in) {
                    return new Robot(in);
                }

                @Override
                public Robot[] newArray(int size) {
                    return new Robot[size];
                }
            };

}
