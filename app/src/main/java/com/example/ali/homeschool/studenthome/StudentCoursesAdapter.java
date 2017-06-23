package com.example.ali.homeschool.studenthome;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.ali.homeschool.InstructorHome.CourseCreated;
import com.example.ali.homeschool.R;
import com.example.ali.homeschool.childEnrolledCourses.LessonActivity;

import java.util.List;

/**
 * Created by Ali on 6/23/2017.
 */

public class StudentCoursesAdapter extends RecyclerView.Adapter<StudentCoursesAdapter.CourseViewHolder> {
    Context context;
    List<CourseCreated> courses;

    public StudentCoursesAdapter(Context context, List<CourseCreated> courses) {
        this.context = context;
        this.courses = courses;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView courseImage;

        public CourseViewHolder(View itemView) {
            super(itemView);
            courseImage = (ImageView) itemView.findViewById(R.id.course_image);
            courseImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.course_image);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    int p = getAdapterPosition();
                    CourseCreated courseCreated = courses.get(p);
                    Intent intent = new Intent(context,
                            LessonActivity.class);
                    intent.putExtra("course", courseCreated);
                    context.startActivity(intent);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
//                    context.startActivity(new Intent(context, ClassActivity.class));
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            v.startAnimation(animation);
        }
    }
}
