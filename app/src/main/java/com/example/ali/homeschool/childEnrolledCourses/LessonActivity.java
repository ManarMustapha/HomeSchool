package com.example.ali.homeschool.childEnrolledCourses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.ali.homeschool.InstructorHome.CourseCreated;
import com.example.ali.homeschool.InstructorLessons.LessonAdapter;
import com.example.ali.homeschool.InstructorLessons.LessonModel;
import com.example.ali.homeschool.R;
import com.example.ali.homeschool.childClass.ClassActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class LessonActivity extends AppCompatActivity {

    List<CourseCreated> enrolledLessonList;
    RecyclerView enrolledRecyclerView;
    Toolbar toolbar ;
    CourseCreated course;
    ValueEventListener listener;
    List<LessonModel> lessonModelList;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        enrolledRecyclerView = (RecyclerView) findViewById(R.id.lessonsRV2);
        enrolledRecyclerView.setHasFixedSize(true);
        LinearLayoutManager categoryLayoutManger = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        enrolledRecyclerView.setLayoutManager(categoryLayoutManger);
        Log.e("Test","myLessonActivity");
        db = FirebaseDatabase.getInstance().getReference();
        lessonModelList = new ArrayList<LessonModel>();

        Intent intent = getIntent();
        course = intent.getParcelableExtra("course");
        Log.e("courseinLessonActivity",course.toString());

    }

    @Override
    protected void onStart() {
        super.onStart();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lessonModelList = new ArrayList<LessonModel>();
                    Log.e("Test","ONdaatachange");
                    Log.v("Test","Datasnapshot "+dataSnapshot.toString() );
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        Log.v("Test","Data Lesson" + dataSnapshot1.toString());
                        LessonModel lessonModel = dataSnapshot1.getValue(LessonModel.class);
                        Log.e("Test","Topics "+lessonModel.getTopics().toString());
                        Log.v("Test","Topics "+lessonModel.getTopics().size());
                        lessonModelList.add(lessonModel);
                    }

                    LessonAdapter lessonAdapter = new LessonAdapter(lessonModelList, new LessonAdapter.OnClickHandler() {
                        @Override
                        public void onClick(LessonModel test) {
                            Intent intent = new Intent(getApplicationContext(),
                                    ClassActivity.class);
                            intent.putExtra("courseid",course.getCourse_id() );
                            intent.putExtra("lessonid", test.getId());

                            startActivity(intent);
                        }
                    });
//
                enrolledRecyclerView.setAdapter(lessonAdapter);


                enrolledRecyclerView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {



                        return false;
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };
        db.child("courses").child(course.getCourse_id()).child("lessons").addValueEventListener(listener);

    }
}
