package com.example.lowryj.myplanner;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jack on 3/28/18.
 */

public class Assignment implements Parcelable {
    private String name;
    private Calendar reminder;
    private Date dueDate;

    public Assignment(String n, Calendar r, Date d) {
        name = n;
        reminder = r;
        dueDate = d;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Calendar getReminder() {
        return reminder;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeSerializable(this.reminder);
        dest.writeLong(this.dueDate != null ? this.dueDate.getTime() : -1);
    }

    protected Assignment(Parcel in) {
        this.name = in.readString();
        this.reminder = (Calendar) in.readSerializable();
        long tmpDueDate = in.readLong();
        this.dueDate = tmpDueDate == -1 ? null : new Date(tmpDueDate);
    }

    public static final Creator<Assignment> CREATOR = new Creator<Assignment>() {
        @Override
        public Assignment createFromParcel(Parcel source) {
            return new Assignment(source);
        }

        @Override
        public Assignment[] newArray(int size) {
            return new Assignment[size];
        }
    };
}
