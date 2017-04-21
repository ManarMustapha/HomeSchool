package com.example.ali.homeschool.InstructorHome;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ali on 4/20/2017.
 */

public class CourseCreated implements Parcelable {
    String id;
    String name;

    public CourseCreated(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public CourseCreated() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    protected CourseCreated(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<CourseCreated> CREATOR = new Parcelable.Creator<CourseCreated>() {
        @Override
        public CourseCreated createFromParcel(Parcel source) {
            return new CourseCreated(source);
        }

        @Override
        public CourseCreated[] newArray(int size) {
            return new CourseCreated[size];
        }
    };
}
