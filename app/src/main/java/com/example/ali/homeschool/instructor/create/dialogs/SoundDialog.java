package com.example.ali.homeschool.instructor.create.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ali.homeschool.instructor.create.OnLayoutReadyInterface;
import com.example.ali.homeschool.R;
import com.example.ali.homeschool.UserModelHelper.FileUploadHelper;
import com.example.ali.homeschool.UserModelHelper.UploadFile;

import java.util.UUID;

import static com.example.ali.homeschool.Constants.mButton;

/**
 * Created by Ali on 6/17/2017.
 */

public class SoundDialog extends MainDialog {
    private String soundText;

    private String audioLink;

    String courseId;

    private static final int PICK_SOUND_REQUEST = 235;

    public SoundDialog(OnLayoutReadyInterface onLayoutReadyInterface) {
        super(onLayoutReadyInterface);
    }

    public SoundDialog(Integer id, Activity activity,
                       OnLayoutReadyInterface onLayoutReadyInterface) {
        super(id, activity, onLayoutReadyInterface);
    }

    public void setSoundText(String soundText) {
        this.soundText = soundText;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setAudioLink(String audioLink) {
        this.audioLink = audioLink;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setFilePath(Uri filePath) {
        String storagePath = "sounds/" + courseId + "/" + UUID.randomUUID();
        new UploadFile(filePath, activity, new FileUploadHelper() {
            @Override
            public void fileUploaded(String url) {

                String layout = mButton(id,url, soundText);
                if (!isEditing) {
                    onLayoutReadyInterface.setLayout(layout);
                    progressImage.setImageOrSound(true,false);
                } else {
                    onEditLayoutReady.setLayoutAt(layout, index);
                }
                //and displaying a success toast
                Toast.makeText(activity, "File Uploaded",
                        Toast.LENGTH_LONG).show();
            }
        }, storagePath);
    }


    public void openSoundDialog() {
        if (id == null || activity == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                activity);

        builder.setTitle("Select sound file");
        LayoutInflater li = LayoutInflater.from(activity);
        LinearLayout someLayout = (LinearLayout) li.inflate(R.layout.sound_dialog, null);
        final EditText soundET = (EditText) someLayout.findViewById(R.id.soundtext);
        soundET.setText(soundText);
        builder.setView(someLayout);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        TextView gallery = (TextView) someLayout.findViewById(R.id.choosefromGallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                soundText = soundET.getText().toString();
                openSoundActivity();
            }
        });

        TextView urlTV = (TextView) someLayout.findViewById(R.id.imageUrl);
        urlTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                soundText = soundET.getText().toString();
                openSoundURLDialog();
            }
        });
    }

    private void openSoundActivity() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Sound"),
                PICK_SOUND_REQUEST);
    }

    private void openSoundURLDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Sound Button");
        LayoutInflater li = LayoutInflater.from(activity);
        LinearLayout someLayout;
        someLayout = (LinearLayout) li.inflate(R.layout.dialog_button, null);

        // Set up the input
        final EditText audioIn = (EditText) someLayout.findViewById(R.id.audio);
        audioIn.setText(audioLink);
        builder.setView(someLayout);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                audioLink = audioIn.getText().toString();
                String layout = mButton(++id, audioLink, soundText);
                if (!isEditing) {
                    onLayoutReadyInterface.setLayout(layout);
                    progressImage.setImageOrSound(true,false);
                } else {
                    onEditLayoutReady.setLayoutAt(layout, index);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
