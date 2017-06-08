package com.example.ali.homeschool.InstructorTopic;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;



public class TopicModel implements Parcelable {
    String id;
    String name;
    String layout;

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
    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.layout);
    }

    public TopicModel() {
    }

    protected TopicModel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.layout = in.readString();
    }

    public static final Parcelable.Creator<TopicModel> CREATOR = new Parcelable.Creator<TopicModel>() {
        @Override
        public TopicModel createFromParcel(Parcel source) {
            return new TopicModel(source);
        }

        @Override
        public TopicModel[] newArray(int size) {
            return new TopicModel[size];
        }
    };
    public Map<String,Object> toMap(){
        HashMap<String,Object> topic = new HashMap<>();
        topic.put("id",id);
        topic.put("name",name);
        topic.put("layout",layout);
        return topic;
    }
}
