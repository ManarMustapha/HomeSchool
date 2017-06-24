package com.example.ali.homeschool.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ali.homeschool.InstructorHome.CourseCreated;
import com.example.ali.homeschool.R;
import com.example.ali.homeschool.data.HeaderRVData;
import com.example.ali.homeschool.descriptionActivity.CourseDescriptionActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ali on 3/1/2017.
 */

public class CourseSectionListAdapter extends RecyclerView.Adapter<CourseSectionListAdapter.ItemRowHolder> {
    Context context;
    List<HeaderRVData> headerRVDataList;
    int userViewType;
    ArrayList<String> subject ;
    String userid;

    public void setUserId(String userid) {
        this.userid = userid;
    }

    public CourseSectionListAdapter(Context context, List<HeaderRVData> headerRVDataList, int userviewType , ArrayList subject){
        this.context = context;
        this.userViewType=userviewType;
        this.headerRVDataList = headerRVDataList;
        this.subject = subject ;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_section_list_item,null);
        return new ItemRowHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int position) {
        holder.sectionHeader.setText(subject.get(position));
        List list = headerRVDataList.get(position).getCategoryInformations();
        CategoryAdapter itemListDataAdapter = new CategoryAdapter(context,list, new CategoryAdapter.OnClickHandler() {
            @Override
            public void onClick(CourseCreated course) {
                Intent intent= new Intent (context, CourseDescriptionActivity.class);
                intent.putExtra("course",course);
                intent.putExtra("type",userViewType);
                if(userid!=null) {
                    intent.putExtra("userid", userid);
                    Log.v("userid: ", userid);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.v( "onClick: ","ID ___ "+course.getDescriptionS());
                Log.e( "onClick: ",intent.toString()+course );
                context.startActivity(intent);
            }
        });

        holder.sectionRV.setHasFixedSize(true);
        holder.sectionRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.sectionRV.setAdapter(itemListDataAdapter);
    }

    @Override
    public int getItemCount() {
        return headerRVDataList.size();
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder{
        TextView sectionHeader;
        RecyclerView sectionRV;
        public ItemRowHolder(View itemView) {
            super(itemView);
            sectionHeader=(TextView) itemView.findViewById(R.id.header);
            sectionRV = (RecyclerView) itemView.findViewById(R.id.recycleViewItem);
        }
    }
}
