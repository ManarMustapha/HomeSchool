package com.almanara.homeschool.childProgress;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.almanara.homeschool.parent.home.ChildModel;
import com.almanara.ali.homeschool.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChildCoursesFragment extends Fragment {
    ListView lv;
    Context context;
    RecyclerView recyclerView;
    ChildProgressAdapter childProgressAdapter;
    DatabaseReference db;
    ChildModel c;
    List<EnrolledCourseModel> enrolledCourseModel;
    ValueEventListener listener;
    int counter = 1;
    int progress = 1;

    public ChildCoursesFragment() {
    }

    View view;
    ArrayList prgmName;
    public static String[] prgmNameList = {"Let Us C", "c++", "JAVA", "Jsp", "Microsoft .Net", "Android", "PHP", "Jquery", "JavaScript"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_child_courses, container, false);
        context = getContext();
        db = FirebaseDatabase.getInstance().getReference();

        counter = 0;
        progress = 0;
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("childModel")) {
            Log.v("Test", "Intent found");
            c = intent.getParcelableExtra("childModel");
            Log.v("Test", "Name" + c.getId());
        }
//        lv=(ListView) view.findViewById(R.id.listView);
//        lv.setAdapter(new CustomListviewAdapter(context , prgmNameList));

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        childProgressAdapter = new ChildProgressAdapter();
        return view;
    }

    @Override
    public void onPause() {
        if (listener != null)
            db.removeEventListener(listener);
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                enrolledCourseModel = new ArrayList<EnrolledCourseModel>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Log.v("Test11111", "Progress " + d.toString());
                    EnrolledCourseModel e = d.getValue(EnrolledCourseModel.class);


                    for (DataSnapshot d1 : d.getChildren()) {
                        Log.v("Test22222", "Progress " + d1.toString());

                        for (DataSnapshot d2 : d1.getChildren()) {
                            Log.v("Test33333", "Progress " + d2.toString());
                            ProgressModel progressModel = d2.getValue(ProgressModel.class);
                            if (progressModel.getTopicProgressFlag().equals("true")) {
                                progress++;
                            }

                            counter++;
                            Log.v("Progresscounnter", "1-counter : " + progress / counter + "2-progress : " + progress);
                        }
                    }
                    if(counter!=0)
                    e.setProgressaftercalc(""+progress*100/counter);
                    else
                        e.setProgressaftercalc(""+0);
                    enrolledCourseModel.add(e);
                }
                Log.v("Progresscounnter", "1-counter : " + progress / counter + "2-progress : " + progress);
                ChildProgressAdapter1 childProgressAdapter1 = new ChildProgressAdapter1(
                        enrolledCourseModel, new ChildProgressAdapter1.OnClickHandler() {
                    @Override
                    public void onClick(EnrolledCourseModel test) {

                    }
                });
                Log.v("Progresscounnter", "1-counter111 : " + progress*100 / counter + "2-progress : " + progress);
                Log.v("Progresscounnter1234", childProgressAdapter1.getProgress()+"");
                recyclerView.setAdapter(childProgressAdapter1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        db.child("users").child(c.getId()).child("enrolledcourses").addValueEventListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
