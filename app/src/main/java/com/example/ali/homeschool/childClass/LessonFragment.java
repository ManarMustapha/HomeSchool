package com.example.ali.homeschool.childClass;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ali.homeschool.Answer;
import com.example.ali.homeschool.Constants;
import com.example.ali.homeschool.InstructorTopic.InstructorTopicCreationActivity;
import com.example.ali.homeschool.InstructorTopic.ParseXMLInstructor;
import com.example.ali.homeschool.InstructorTopic.XMLClick;
import com.example.ali.homeschool.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Random;


import static com.example.ali.homeschool.Constants.Color_Request;
import static com.example.ali.homeschool.Constants.Text_Detection;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link LessonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LessonFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String layout;
    RelativeLayout linearLayout;
    MediaPlayer mediaPlayer;
//    private OnFragmentInteractionListener mListener;

    public LessonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param layout string 1.
     * @return A new instance of fragment LessonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LessonFragment newInstance(String layout) {
        LessonFragment fragment = new LessonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, layout);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            layout = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_lesson, container, false);
        ParseXMLInstructor parseXML = new ParseXMLInstructor(getActivity());
        parseXML.setXmlClick(new XMLClick() {
            @Override
            public void playSound(String url) {
                try {
                    playAudio(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void openActivity(String activity, Answer answer) {
//                if (activity.equals("ColorActivity")) {
//                    Intent intent = new Intent(getActivity(), ColorActivity.class);
//                    intent.putExtra("Answer", answer);
//                    startActivityForResult(intent, Color_Request);
//                } else if (activity.equals("TextDetection")) {
//                    Intent intent = new Intent(getActivity(), edu.sfsu.cs.orange.ocr.CaptureActivity.class);
//                    intent.putExtra("Answer", answer);
////                                intent.putExtra("lan", InstructorTopicCreationActivity.selectlanguageString);
//                    //Comment
//                    startActivityForResult(intent, Text_Detection);
//                }
            }

            @Override
            public void onImageClick(View imageView) {

            }

            @Override
            public void onMultQuestionClicked(boolean isCorrect) {
                if(isCorrect){
                    MediaPlayer mediaPlayer= MediaPlayer.create(getActivity(), R.raw.yay);
                    mediaPlayer.start();
                }else{
                    MediaPlayer mediaPlayer= MediaPlayer.create(getActivity(), R.raw.wronganswer);
                    mediaPlayer.start();
                }
            }
        });
        try {
            linearLayout= (RelativeLayout) parseXML.parse(layout);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Random rnd = new Random();
//
//        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//        container.setBackgroundColor(color);
        return linearLayout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("LessonFragment", "Result " +requestCode + " , "+ resultCode );
        if(requestCode==2){
            Uri result = data.getData();
            Log.v("LessonFragment", "Result " +result.toString());
        }
        Log.v("LessonFragment", "Activity Result " +requestCode + " , "+ resultCode );
        if(resultCode == Constants.CORRECTANSWER){
            if (requestCode == Constants.Text_Detection){
                Log.v("t3ala yabni","etnyl t3ala enta eltani");
                Toast.makeText(getActivity(), "Result Correct", Toast.LENGTH_SHORT).show();
            }
        }

        Log.v("ezhr w ban ",resultCode+"");
        Log.v("ezhr w ban ",requestCode+"");
        if(resultCode == Constants.WRONGANSWER){
            if (requestCode == Constants.Text_Detection){
                Log.v("t3ala yabni","etnyl t3ala");
                Toast.makeText(getActivity(), "Result Incorrect", Toast.LENGTH_SHORT).show();
            }
        }
//        if(requestCode== Constants.SPEECH){
//            if(resultCode==Constants.CORRECTANSWER){
//
//                Toast.makeText(getActivity(), data.getData().toString() ,  Toast.LENGTH_LONG).show();
//
//
//            }else if (resultCode == Constants.WRONGANSWER){
//                Toast.makeText(getActivity(), data.getData().toString() ,  Toast.LENGTH_LONG).show();
//
//            }
//        }
    }

    private void playAudio(String url) throws Exception
    {
        killMediaPlayer();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    private void killMediaPlayer() {
        if(mediaPlayer!=null) {
            try {
                mediaPlayer.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
