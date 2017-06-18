package com.example.ali.homeschool.InstructorTopic.CreationHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.ali.homeschool.Constants;
import com.example.ali.homeschool.InstructorHome.CourseCreated;
import com.example.ali.homeschool.InstructorTopic.InstructorTopicCreationActivity;
import com.example.ali.homeschool.InstructorTopic.TextAppInterface;
import com.example.ali.homeschool.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import edu.sfsu.cs.orange.ocr.Answer;

import static com.example.ali.homeschool.Constants.PUT_SOUND_LINK_HERE;
import static com.example.ali.homeschool.Constants.mButton;
import static com.example.ali.homeschool.Constants.mTextView;
import static com.example.ali.homeschool.Constants.textViewProperties;

/**
 * Created by Ali on 6/18/2017.
 */

public class TextDetectionDialog extends MainTextDialog {
    String selectlanguageString;

    public TextDetectionDialog(Integer id, Activity activity,
                               OnLayoutReadyInterface onLayoutReadyInterface) {
        super(id, activity, onLayoutReadyInterface);
    }
    public void openTextDetectionDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                activity);
        builder.setTitle("Questions :");
        LayoutInflater li = LayoutInflater.from(activity);
        final LinearLayout linearLayout = (LinearLayout) li
                .inflate(R.layout.text_detection_dialog, null);
        final EditText word = (EditText) linearLayout.findViewById(R.id.word);
        final EditText questionStart = (EditText) linearLayout.findViewById(R.id.questionStart);
        final Spinner selectLanguage = (Spinner) linearLayout.findViewById(R.id.textLan);

        ArrayAdapter<CharSequence> textSizes = ArrayAdapter
                .createFromResource(activity,
                        R.array.text_lan_array, android.R.layout.simple_spinner_item);

        selectLanguage.setAdapter(textSizes);
        selectLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    selectlanguageString = "eng";
                } else if (i == 1) {
                    selectlanguageString = "ara";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        textViewProperties(linearLayout, activity, this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onLayoutReadyInterface.setLayout(mTextView(++id, questionStart.getText().toString().trim(), textColor,
                        textAppearance));
                onLayoutReadyInterface.setLayout( mTextView(++id, word.getText().toString().trim(), textColor,
                        textAppearance));
                onLayoutReadyInterface.setLayout( mButton(++id, "Start", "TextDetection",
                        new Answer(word.getText().toString(), selectlanguageString),
                        PUT_SOUND_LINK_HERE));

                dialogInterface.cancel();
            }
        });
        builder.setView(linearLayout);
        builder.show();


    }
}
