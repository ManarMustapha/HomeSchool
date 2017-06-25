package com.example.ali.homeschool.studenthome;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ali.homeschool.InstructorHome.CourseCreated;
import com.example.ali.homeschool.R;
import com.example.ali.homeschool.childEnrolledCourses.EnrolledCoursesAdapter1;
import com.example.ali.homeschool.childEnrolledCourses.LessonActivity;
import com.example.ali.homeschool.childProgress.EnrolledCourseModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CoursesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView coursesRecycleView;
    DatabaseReference db;
    List<CourseCreated> coursesNames;
//    private OnFragmentInteractionListener mListener;

    public CoursesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoursesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoursesFragment newInstance(String param1, String param2) {
        CoursesFragment fragment = new CoursesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_courses, container, false);
        coursesRecycleView = (RecyclerView) view.findViewById(R.id.courses);
        coursesRecycleView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManger = new GridLayoutManager(getActivity(), 3);
//        gridLayoutManger.generateLayoutParams(new GridLayoutManager.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        coursesRecycleView.setLayoutManager(gridLayoutManger);

        db = FirebaseDatabase.getInstance().getReference();

        setupWindowAnimations();
        return view;
    }

    private void setupWindowAnimations() {

        // Re-enter transition is executed when returning back to this activity
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT); // Use START if using right - to - left locale
        slideTransition.setDuration(1000);

        getActivity().getWindow().setReenterTransition(slideTransition);  // When MainActivity Re-enter the Screen
        getActivity().getWindow().setExitTransition(slideTransition);     // When MainActivity Exits the Screen

        // For overlap of Re Entering Activity - MainActivity.java and Exiting TransitionActivity.java
        getActivity().getWindow().setAllowReturnTransitionOverlap(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        db.child("users").child("GjiwC0oIEYb52VXOsEG83hXOiij1").child("enrolledcourses")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        coursesNames = new ArrayList<>();
                        for (DataSnapshot s : dataSnapshot.getChildren()) {
                            EnrolledCourseModel c = s.getValue(EnrolledCourseModel.class);
                            db.child("courses").child(c.getCourse_id())
                                    .addValueEventListener(new ValueEventListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                        @Override
                                        public void onDataChange(DataSnapshot inside) {
                                            CourseCreated courses = inside.getValue(CourseCreated.class);
                                            for (DataSnapshot x : inside.getChildren()) {
                                            }
                                            coursesNames.add(courses);
                                            StudentCoursesAdapter studentCoursesAdapter = new StudentCoursesAdapter(getActivity(), coursesNames);
                                            coursesRecycleView.setAdapter(studentCoursesAdapter);
//                                            if(coursesNames.size() <0){
//                                                noMyCourse.setVisibility(View.VISIBLE);
//                                            }else{
//                                                noMyCourse.setVisibility(View.GONE);
//                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                        }
                        // [END_EXCLUDE]
                    }

                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
