package com.example.ali.homeschool.InstructorTopic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ali.homeschool.Constants;
import com.example.ali.homeschool.InstructorHome.CourseCreated;
import com.example.ali.homeschool.InstructorTopic.helper.DoneOrderInterface;
import com.example.ali.homeschool.InstructorTopic.helper.OnStartDragListener;
import com.example.ali.homeschool.InstructorTopic.helper.SimpleItemTouchHelperCallback;
import com.example.ali.homeschool.R;
import com.example.ali.homeschool.UserModelHelper.FileUploadHelper;
import com.example.ali.homeschool.UserModelHelper.UploadFile;
import com.example.ali.homeschool.exercises.color.ColorActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import edu.sfsu.cs.orange.ocr.Answer;

import static com.example.ali.homeschool.Constants.ARRANGE;
import static com.example.ali.homeschool.Constants.Color_Request;
import static com.example.ali.homeschool.Constants.PUT_ACTIVITY_HERE;
import static com.example.ali.homeschool.Constants.PUT_ANSWER_HERE;
import static com.example.ali.homeschool.Constants.PUT_ID_HERE;
import static com.example.ali.homeschool.Constants.PUT_SOUND_LINK_HERE;
import static com.example.ali.homeschool.Constants.Text_Detection;
import static com.example.ali.homeschool.Constants.mButton;
import static com.example.ali.homeschool.Constants.mImageView;
import static com.example.ali.homeschool.Constants.mTextView;
import static com.example.ali.homeschool.Constants.radioButton;
import static com.example.ali.homeschool.Constants.radioGroupEnd;
import static com.example.ali.homeschool.Constants.radioGroupStart;
import static com.example.ali.homeschool.Constants.setColorButton;
import static com.example.ali.homeschool.Constants.start;
import static com.example.ali.homeschool.Constants.textViewProperties;

public class InstructorTopicCreationActivity extends AppCompatActivity
        implements XMLClick, ImageClicked, ColorPickerDialogListener, TextAppInterface, DoneOrderInterface, OnStartDragListener {
    int id = 0;
    int radioButtonId = 0;
    CourseCreated courseCreated;
    private static final int PICK_IMAGE_REQUEST = 234;
    private static final int PICK_SOUND_REQUEST = 235;
    private static final int REORDER = 236;
    Boolean flagTrial = false;
    Button openColorPicker;
    private MediaPlayer mediaPlayer;
    String globalImageUrl;
    UploadFile uploadFileImage;
    UploadFile uploadFileSound;
    private Uri filePath;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ArrayList<String> midLayouts;
    TextView submitTV;
    String m_Text = "";
    String audioLink = "";
    TextView image;
    TextView question;
    String courseId;
    String lessonid;
    String topicid;
    String topicname;
    TextView sound;
    RelativeLayout act_main;
    LinearLayout mainView;
    String soundText = "PlaceHolder";
    String mid = "";
    int textColor = -11177216;
    int textAppearance = android.R.style.TextAppearance_Material_Body1;

    public static String selectlanguageString = "";

    String end = "</LinearLayout></RelativeLayout>";


    RelativeLayout view1;
    LinearLayout view2;
    ArrayList<View> views;
    Boolean imageFlag = true;
    Boolean soundFlag = true;
    ObservedObject watchImageUrl;
    ObservedObject watchSoundUrl;
    String globalSoundUrl;


    private ItemTouchHelper mItemTouchHelper;
    ArrayList<String> layoutsList;
    RecyclerView recyclerViewOrdering;
    RecyclerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_topic);
        layoutsList = new ArrayList<>();
        recyclerViewOrdering = (RecyclerView) findViewById(R.id.recycleView);
        recyclerViewOrdering.setHasFixedSize(true);
        recyclerViewOrdering.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new RecyclerListAdapter(getApplicationContext(), new OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mItemTouchHelper.startDrag(viewHolder);
            }
        },
                layoutsList, this, new DoneOrderInterface() {
            @Override
            public void onReorder(List<String> layouts) {

            }
        });
        recyclerViewOrdering.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerViewOrdering);

        globalImageUrl = "";
        globalSoundUrl = "";

        // create watched and watcher objects
        watchImageUrl = new ObservedObject(globalImageUrl);
        watchSoundUrl = new ObservedObject(globalSoundUrl);


        views = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        question = (TextView) findViewById(R.id.question);
        submitTV = (TextView) findViewById(R.id.submit);
        midLayouts = new ArrayList<>();
        mainView = (LinearLayout) findViewById(R.id.mainLayout);
        act_main = (RelativeLayout) findViewById(R.id.activiy_main);
        image = (TextView) findViewById(R.id.image);
        sound = (TextView) findViewById(R.id.sound);
        findViewById(R.id.reorder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrderingActivity.class);
                String layouts = "";

                for (String x : midLayouts) {
                    layouts += x;
                }
                layouts = layouts.replace(start, " ");
                layouts = layouts.replace(end, " ");
                intent.putExtra("Layout", layouts);
                startActivityForResult(intent, REORDER);
            }
        });
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("lessonid")) {
            lessonid = intent.getStringExtra("lessonid");
        }
        if (intent != null && intent.hasExtra("courseID")) {
            courseId = intent.getStringExtra("courseID");
        }
        if (intent != null && intent.hasExtra("topicid")) {
            topicid = intent.getStringExtra("topicid");
        }
        if (intent != null && intent.hasExtra("topicname")) {
            topicname = intent.getStringExtra("topicname");
        }
        if (intent != null && intent.hasExtra("layout")) {
            String layout = intent.getStringExtra("layout");
            layout = layout.replaceAll(start, " ");
            layout = layout.replaceAll(end, " ");

            String[] lays = layout.split("(?<=" + ARRANGE + ")");

            for (String x : lays) {
                Log.v("0909Intent", "x :" + x);
                if (!x.trim().isEmpty()) {
                    addLayout(x);
                }
            }

//            addLayout(layout);
        }

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(
                        InstructorTopicCreationActivity.this);
                builder.setTitle("Choose type :");
                LayoutInflater li = LayoutInflater.from(InstructorTopicCreationActivity.this);
                LinearLayout someLayout = (LinearLayout) li.inflate(R.layout.question_list, null);
                final TextView colorQue = (TextView) someLayout.findViewById(R.id.color);
                final TextView speechQue = (TextView) someLayout.findViewById(R.id.speech);
                final TextView multiQue = (TextView) someLayout.findViewById(R.id.multipleChoices);
                final TextView textDetection = (TextView) someLayout
                        .findViewById(R.id.textDetection);

                builder.setView(someLayout);
                final AlertDialog dialog = builder.create();
                dialog.show();
                colorQue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        openColorQuestionDialog();

                    }
                });
                speechQue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        openSpeechQueDialog();
                    }
                });
                multiQue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        openMultiQuestionDialog();
                    }
                });

                textDetection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        openTextDetectionDialog();
                    }
                });

            }
        });
        submitTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("Course" + courseId, "lessons" + lessonid);
                Log.v("topics", topicid);
                DatabaseReference coursesDatabaseReference = databaseReference.child("courses")
                        .child(courseId);
                databaseReference = databaseReference.child("courses").child(courseId)
                        .child("lessons").child(lessonid).child("topics").child(topicid);
                TopicModel t = new TopicModel();
                String layouts = " ";
                for (String layout : layoutsList) {
                    layouts += layout;
                }
                Log.v("909Log909", "\n\n Layouts submit : " + layouts + "\n\nEnd");
                t.setLayout(start + layouts + end);
                t.setName(topicname);
                t.setId(topicid);
                databaseReference.updateChildren(t.toMap());

                //   coursesDatabaseReference.child("questions").setValue(courseCreated.getQuestions());
                finish();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(
                        InstructorTopicCreationActivity.this);
                builder.setTitle("Select image");
                LayoutInflater li = LayoutInflater.from(InstructorTopicCreationActivity.this);
                LinearLayout someLayout = (LinearLayout) li.inflate(R.layout.image_dialog, null);
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
                        openImageActivity();
                    }
                });

                TextView urlTV = (TextView) someLayout.findViewById(R.id.imageUrl);
                urlTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        openImageURLDialog();
                    }
                });

            }
        });
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(
                        InstructorTopicCreationActivity.this);
                builder.setTitle("Select sound file");
                LayoutInflater li = LayoutInflater.from(InstructorTopicCreationActivity.this);
                LinearLayout someLayout = (LinearLayout) li.inflate(R.layout.sound_dialog, null);
                final EditText soundET = (EditText) someLayout.findViewById(R.id.soundtext);
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
        });


        for (View view : views) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    String x = v.toString();
                    if (x.contains("Button")) {
                        final Button button = (Button) v;
                        views.indexOf(v);
                        String linkPath = "";
                        for (String lay : midLayouts)
                            if (lay.contains("<Button")) {
                                linkPath = linkPath + lay.substring(lay.indexOf("https:"));
                                linkPath = linkPath.trim();
                                break;
                            }
                        if (!linkPath.contains("http")) {
                            final EditText input = new EditText(
                                    InstructorTopicCreationActivity.this);
                            input.setInputType(
                                    InputType.TYPE_CLASS_TEXT);
                            input.setText(button.getText().toString());
                            final AlertDialog.Builder textBuilder = new AlertDialog.Builder(
                                    InstructorTopicCreationActivity.this);
                            textBuilder.setTitle("Title");
                            textBuilder.setView(input);
                            textBuilder
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            button.setText(input.getText().toString());
                                            views.set(views.indexOf(v), button);
                                        }
                                    });
                            textBuilder.setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            final AlertDialog dialog = textBuilder.create();
                            dialog.show();
                        } else {
                            String buttonTitle = "";

                            soundFlag = false;
                            linkPath = linkPath.replaceAll("\" /> ", "");
                            linkPath = linkPath.trim();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(
                                    InstructorTopicCreationActivity.this);
                            builder.setTitle("Select sound file");
                            LayoutInflater li = LayoutInflater
                                    .from(InstructorTopicCreationActivity.this);
                            LinearLayout someLayout = (LinearLayout) li
                                    .inflate(R.layout.sound_dialog, null);
                            final EditText soundET = (EditText) someLayout
                                    .findViewById(R.id.soundtext);
                            builder.setView(someLayout);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (String lay : midLayouts) {
                                        String temp1;
                                        Log.v("ButtonTryouts", button.getText().toString().trim());
                                        if (lay.contains(button.getText().toString().trim())) {
                                            temp1 = lay.replace(button.getText().toString(),
                                                    soundET.getText().toString());
                                            midLayouts.set(midLayouts.indexOf(lay), temp1);
                                            Log.v("ButtonTryouts", temp1);
                                        }
                                        Log.v("ButtonTryouts", "Nope");
                                    }
                                    button.setText(soundET.getText().toString());
                                    views.set(views.indexOf(v), button);
                                    dialog.cancel();
                                }
                            });
                            builder.setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            final AlertDialog dialog = builder.create();
                            dialog.show();
                            TextView gallery = (TextView) someLayout
                                    .findViewById(R.id.choosefromGallery);
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
                            final String finalLinkPath = linkPath;
                            watchSoundUrl.addObserver(new Observer() {
                                @Override
                                public void update(Observable o, Object arg) {
                                    for (String lay : midLayouts) {
                                        Log.v("finalLinkPath :", finalLinkPath);
                                        Log.v("globalImageUrl", globalSoundUrl);
                                        Log.v("temp1", lay);
                                        if (lay.contains(finalLinkPath)) {
                                            String temp1 = "";
                                            temp1 = lay.replace(finalLinkPath, globalSoundUrl);
                                            midLayouts.set(midLayouts.indexOf(lay), temp1);
                                            views.set(views.indexOf(v), button);
                                            Log.v("temp2", lay);
                                            Log.v("Views", views.get(views.indexOf(v)) + "");
                                            Log.v("midLayouts", midLayouts.indexOf(lay) + "");
                                        }
                                    }

                                }
                            });

                            soundFlag = true;
                        }


                    } else if (x.contains("ImageView")) {
                        imageFlag = false;
                        final ImageView imageView = (ImageView) v;
                        String substr = "";
                        //   Log.v("SubString",substr.substring(x.lastIndexOf('h'),x.lastIndexOf('>')));
                        String linkPath = "";
                        for (String lay : midLayouts)
                            if (lay.contains("<ImageView")) {
                                linkPath = linkPath + lay.substring(lay.indexOf("https:"));
                                break;
                            }
                        linkPath = linkPath.replaceAll("\" /> ", "");
                        linkPath = linkPath.trim();
                        final EditText input = new EditText(InstructorTopicCreationActivity.this);
                        input.setInputType(
                                InputType.TYPE_CLASS_TEXT);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(
                                InstructorTopicCreationActivity.this);
                        builder.setTitle("Select image");
                        builder.setView(input);
                        LayoutInflater li = LayoutInflater
                                .from(InstructorTopicCreationActivity.this);
                        LinearLayout someLayout = (LinearLayout) li
                                .inflate(R.layout.image_dialog, null);
                        builder.setView(someLayout);
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        final AlertDialog dialog = builder.create();
                        dialog.show();
                        TextView gallery = (TextView) someLayout
                                .findViewById(R.id.choosefromGallery);
                        gallery.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                                openImageActivity();
                            }
                        });
                        TextView urlTV = (TextView) someLayout.findViewById(R.id.imageUrl);
                        urlTV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                                openImageURLDialog();
                            }
                        });
                        //   globalImageUrl.S
                        final String finalLinkPath = linkPath;
                        watchImageUrl.addObserver(new Observer() {
                            @Override
                            public void update(Observable o, Object arg) {
                                for (String lay : midLayouts) {
                                    Log.v("finalLinkPath :", finalLinkPath);
                                    Log.v("globalImageUrl", globalImageUrl);
                                    Log.v("temp1", lay);
                                    if (lay.contains(finalLinkPath)) {
                                        String temp1 = "";
                                        temp1 = lay.replace(finalLinkPath, globalImageUrl);
                                        midLayouts.set(midLayouts.indexOf(lay), temp1);
                                        views.set(views.indexOf(v), imageView);
                                        Log.v("temp2", lay);
                                        Log.v("Views", views.get(views.indexOf(v)) + "");
                                        Log.v("midLayouts", midLayouts.indexOf(lay) + "");
                                        Glide.with(InstructorTopicCreationActivity.this)
                                                .load(globalImageUrl).fitCenter().into(imageView);
                                    }
                                }

                            }
                        });

                        imageFlag = true;
                    } else if (x.contains("TextView")) {
                        final TextView textView = (TextView) v;
                        final EditText input = new EditText(InstructorTopicCreationActivity.this);
                        input.setInputType(
                                InputType.TYPE_CLASS_TEXT);
                        input.setText(textView.getText().toString());
                        final AlertDialog.Builder textBuilder = new AlertDialog.Builder(
                                InstructorTopicCreationActivity.this);
                        textBuilder.setTitle("Title");
                        textBuilder.setView(input);
                        textBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (String lay : midLayouts) {
                                    String temp1;
                                    if (lay.contains(textView.getText().toString())) {
                                        temp1 = lay.replaceAll(textView.getText().toString(),
                                                input.getText().toString());
                                        midLayouts.set(midLayouts.indexOf(lay), temp1);
                                    }
                                }
                                textView.setText(input.getText().toString());
                                views.set(views.indexOf(v), textView);

                            }
                        });
                        textBuilder
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        final AlertDialog dialog = textBuilder.create();
                        dialog.show();
                    } else if (x.contains("Button")) {
                        final Button button = (Button) v;
                        views.indexOf(v);
                        String linkPath = "";
                        for (String lay : midLayouts)
                            if (lay.contains("<Button")) {
                                linkPath = linkPath + lay.substring(lay.indexOf("https:"));
                                linkPath = linkPath.trim();
                                break;
                            }
                    }

                    return false;
                }
            });
        }

    }

    private void addLayout(String layout) {
        views = new ArrayList<>();
//        midLayouts.add(id, layout);
        midLayouts.add(layout);

//        id++;
        String layouts = "";
        for (String lay : midLayouts) {
            layouts += lay;
            Log.v("radioButtonId ", lay + "");
        }
        Log.v("Orderings", "\n\naddLayout :" + layouts + "\n\n");

        Log.v("radioButtonId ", radioButtonId + "");
        Log.v("radioButtonId ", id + "");
        RelativeLayout linearLayout = parse(start + layouts + end);
        mainView.removeAllViews();
//        mainView.addView(linearLayout);
        Log.v("0909Ada","before & clear add addLayout : " + layoutsList.toString());
        adapter.clearItems();

        layoutsList.add(layout);
        adapter.setArrayItems(layoutsList);
//        adapter.addItem(layout);
        adapter = new RecyclerListAdapter(this , this,layoutsList,this,this);
        recyclerViewOrdering.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerViewOrdering);
        Log.v("0909Ada","addLayout : " + layoutsList.toString());

//        adapter.notifyItemChanged(adapter.getItemCount() - 1);
//        view1 = (RelativeLayout) mainView.getChildAt(0);
//        view2 = (LinearLayout) view1.getChildAt(0);
//        for (int i = 0; i < view2.getChildCount(); i++) {
//            views.add(view2.getChildAt(i));
//        }

    }

    private void openTextDetectionDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                InstructorTopicCreationActivity.this);
        builder.setTitle("Questions :");
        LayoutInflater li = LayoutInflater.from(InstructorTopicCreationActivity.this);
        final LinearLayout linearLayout = (LinearLayout) li
                .inflate(R.layout.text_detection_dialog, null);
        final EditText word = (EditText) linearLayout.findViewById(R.id.word);
        final EditText questionStart = (EditText) linearLayout.findViewById(R.id.questionStart);
        final Spinner selectLanguage = (Spinner) linearLayout.findViewById(R.id.textLan);

        ArrayAdapter<CharSequence> textSizes = ArrayAdapter
                .createFromResource(InstructorTopicCreationActivity.this,
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
                Log.v("Moooo", "" + i);
                Log.v("Moooo", "" + selectlanguageString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        textViewProperties(linearLayout, InstructorTopicCreationActivity.this, this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


//                addLayout(mTextView(id, questionStart.getText().toString().trim(), textColor, textAppearance));
                addLayout(mTextView(++id, questionStart.getText().toString().trim(), textColor,
                        textAppearance));
                addLayout(mTextView(++id, word.getText().toString().trim(), textColor,
                        textAppearance));
                addLayout(mButton(++id, "Start", "TextDetection",
                        new Answer(word.getText().toString(), selectlanguageString),
                        PUT_SOUND_LINK_HERE));

                dialogInterface.cancel();
            }
        });
        builder.setView(linearLayout);
        builder.show();

    }

    private void openSpeechQueDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                InstructorTopicCreationActivity.this);
        builder.setTitle("Questions :");
        LayoutInflater li = LayoutInflater.from(InstructorTopicCreationActivity.this);
        final LinearLayout linearLayout = (LinearLayout) li
                .inflate(R.layout.speech_question_dialog, null);
        final EditText word = (EditText) linearLayout.findViewById(R.id.word);
        textViewProperties(linearLayout, InstructorTopicCreationActivity.this, this);
        setColorButton(linearLayout, textColor);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.v("TESTAPP", "onClick :" + textAppearance);
                addLayout(mTextView(++id, "قول كلمة :", textColor, textAppearance));
                String textview = mTextView(++id, word.getText().toString(), textColor,
                        textAppearance);
                addLayout(textview);
                Answer answer = new Answer();
                answer.setAnswer(word.getText().toString());
                addLayout(mButton(++id, "Start", "Speech", answer, PUT_SOUND_LINK_HERE));
                dialogInterface.cancel();
            }
        });
        builder.setView(linearLayout);
        builder.show();
    }

    private void openMultiQuestionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InstructorTopicCreationActivity.this);
        builder.setTitle("Questions :");
        LayoutInflater li = LayoutInflater.from(InstructorTopicCreationActivity.this);
        final RelativeLayout relativeLT = (RelativeLayout) li
                .inflate(R.layout.mulit_choice_dialog, null);
        final EditText question1 = (EditText) relativeLT.findViewById(R.id.questionText);
        final LinearLayout linearLayout = (LinearLayout) relativeLT.findViewById(R.id.questions);
        final EditText answer1 = (EditText) linearLayout.findViewById(R.id.answer1ET);
        FloatingActionButton addQuestion = (FloatingActionButton) relativeLT
                .findViewById(R.id.addQuestion);
        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(InstructorTopicCreationActivity.this);
                LinearLayout question = (LinearLayout) li.inflate(R.layout.question_layout, null);
                linearLayout.addView(question);
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LinearLayout la = (LinearLayout) linearLayout.getChildAt(0);
                TextInputLayout te = (TextInputLayout) la.getChildAt(0);
                FrameLayout et = (FrameLayout) te.getChildAt(0);
                android.support.v7.widget.AppCompatEditText om = (AppCompatEditText) et
                        .getChildAt(0);
                ArrayList<String> radioLayout = new ArrayList<String>();
//                String quET = actionTextXML.replaceAll(PU, String.valueOf(id));
//                quET = quET.replaceAll(PUTSIZEHERE, String.valueOf(30));
//                quET = quET.replaceAll(PUTACTIONTEXTHERE, question1.getText().toString());
//                quET = quET.replaceAll(PUTCOLOR, "123");
//                radioLayout.add(quET);
                radioLayout.add(radioGroupStart.replace(PUT_ID_HERE, String.valueOf(++id)));
//                addRadio(radioGroupStart.replace(PUT_ID_HERE,String.valueOf(++id)));

                for (int count = 0; count < linearLayout.getChildCount(); count++) {
                    LinearLayout lat = (LinearLayout) linearLayout.getChildAt(count);
                    TextInputLayout tet = (TextInputLayout) lat.getChildAt(0);
                    CheckBox checkbox = (CheckBox) lat.getChildAt(1);
                    Log.v("MultiQue", "Check Box Checked:" + checkbox.isChecked());
                    FrameLayout ett = (FrameLayout) tet.getChildAt(0);
                    android.support.v7.widget.AppCompatEditText omm = (AppCompatEditText) ett
                            .getChildAt(0);
                    Log.v("MultiQue", "Child 0 Edit text :" + omm.getText());
                    String r1 = radioButton
                            .replaceAll(PUT_ID_HERE, String.valueOf(++radioButtonId));
                    r1 = r1.replaceAll(PUT_ANSWER_HERE, omm.getText().toString());
                    Log.v("MultiQue", "radio " + r1);
                    radioLayout.add(r1);
                    // addLayout(r1);
                }
                radioLayout.add(radioGroupEnd);
//                addLayout(radioGroupEnd);
                //          addRadio(radioLayout.toString());
//                for(String x:radioLayout){
//                    Log.v("Parser","ITA Radio Layout :" + x);
//                }
                addLayout(radioLayout.toString());
            }
        });
        builder.setView(relativeLT);
        builder.show();
    }

    public RelativeLayout parse(String layout) {
        InputStream stream = null;
        stream = new ByteArrayInputStream(layout.getBytes(Charset.forName("UTF-8")));
        ParseXMLInstructor parseXMLInstructor = new ParseXMLInstructor();
        RelativeLayout mainLayout = null;

        try {
            mainLayout = (RelativeLayout) parseXMLInstructor
                    .parse(this, stream, getApplicationContext(), this);
            Log.v("ITA", "pass");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.v("ITA", "XML ERROR :" + e);
        } catch (IOException e) {
            Log.v("ITA", "XML IO ERROR :" + e);
        }
        return mainLayout;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            filePath = data.getData();
            String storagePath = "images/" + courseId + "/" + UUID.randomUUID();
            uploadFileImage = new UploadFile(filePath, this, new FileUploadHelper() {
                @Override
                public void fileUploaded(String url) {
                    if (imageFlag)
                        addLayout(mImageView(++id, url));
                    else {
                        flagTrial = true;
                        globalImageUrl = url;
                        Log.v("GlobalURL", globalImageUrl);
                        watchImageUrl.setValue(globalImageUrl);
                    }
                    //and displaying a success toast
                    Toast.makeText(getApplicationContext(), "File Uploaded",
                            Toast.LENGTH_LONG).show();
                }
            }, storagePath);
        }
        if (requestCode == PICK_SOUND_REQUEST && resultCode == Activity.RESULT_OK) {
            filePath = data.getData();
            String storagePath = "sounds/" + courseId + "/" + UUID.randomUUID();
            uploadFileSound = new UploadFile(filePath, this, new FileUploadHelper() {
                @Override
                public void fileUploaded(String url) {
                    if (soundFlag)
                        addLayout(
                                mButton(id, "Start", PUT_ACTIVITY_HERE, new Answer(PUT_ANSWER_HERE),
                                        url));
                    else {
                        flagTrial = true;
                        globalImageUrl = url;
                        Log.v("GlobalURL", globalImageUrl);
                        watchImageUrl.setValue(globalImageUrl);
                    }

                    //and displaying a success toast
                    Toast.makeText(getApplicationContext(), "File Uploaded",
                            Toast.LENGTH_LONG).show();
                }
            }, storagePath);
        }
        if (requestCode == REORDER && resultCode == Activity.RESULT_OK) {
            String layouts = String.valueOf(data.getData());
            Log.v("Layouts", layouts);
            for (int i = 0; i < midLayouts.size(); i++) {
                midLayouts.remove(i);
            }
            Log.v("AfterForLoop",
                    "\n\nOnActivityResult :\n\n : midLayouts" + midLayouts.toString() + "\n\n");
            midLayouts.clear();
            Log.v("AfterForLoop", "\nClear\nOnActivityResult :\n\n : midLayouts" + midLayouts
                    .toString() + "\n\n");

//            id=1;
            Log.v("Orderings", "\n\nOnActivityResult :" + layouts + "\n\n");
            RelativeLayout linearLayout = parse(start + layouts + end);
            mainView.removeAllViews();
            mainView.addView(linearLayout);
            Log.v("Orderings",
                    "\n\nOnActivityResult :\n\n : midLayouts" + midLayouts.toString() + "\n\n");

//            addLayout(layouts);
        }
    }


    private void openColorQuestionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InstructorTopicCreationActivity.this);
//        builder.setTitle("Title");
        LayoutInflater li = LayoutInflater.from(InstructorTopicCreationActivity.this);
        LinearLayout someLayout = (LinearLayout) li.inflate(R.layout.color_question_dialog, null);
        final EditText questionET = (EditText) someLayout.findViewById(R.id.question);
        textViewProperties(someLayout, this, this);
        Spinner colorSpinner = (Spinner) someLayout.findViewById(R.id.colors);
        ArrayAdapter<CharSequence> actionColors = ArrayAdapter.createFromResource(this,
                R.array.color_array, android.R.layout.simple_spinner_item);
        actionColors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        colorSpinner.setAdapter(actionColors);
        final String[] selection = {" "};
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position,
                                       long l) {
                selection[0] = (String) adapterView.getItemAtPosition(position);
                Log.v("ITA", "Selected :" + selection[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        builder.setView(someLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String questionText = questionET.getText().toString();
                addLayout(mButton(++id, questionText, "ColorActivity", new Answer(selection[0]),
                        PUT_SOUND_LINK_HERE));
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void openSoundURLDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InstructorTopicCreationActivity.this);
        builder.setTitle("Sound Button");
        LayoutInflater li = LayoutInflater.from(InstructorTopicCreationActivity.this);
        LinearLayout someLayout;
        someLayout = (LinearLayout) li.inflate(R.layout.dialog_button, null);

        // Set up the input
        final EditText audioIn = (EditText) someLayout.findViewById(R.id.audio);
        builder.setView(someLayout);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                audioLink = audioIn.getText().toString();
                if (soundFlag)
                    addLayout(
                            mButton(++id, soundText, PUT_ACTIVITY_HERE, new Answer(PUT_ANSWER_HERE),
                                    audioLink));
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

    private void openSoundActivity() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Sound"),
                PICK_SOUND_REQUEST);
    }

    private void openImageURLDialog() {
        final EditText input = new EditText(InstructorTopicCreationActivity.this);
        input.setInputType(
                InputType.TYPE_CLASS_TEXT);
        final AlertDialog.Builder urlBuilder = new AlertDialog.Builder(
                InstructorTopicCreationActivity.this);
        urlBuilder.setTitle("Title");
        urlBuilder.setView(input);
        urlBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                Log.v("m_Text", m_Text);
                if (imageFlag)
                    addLayout(mImageView(++id, m_Text));
            }
        });
        urlBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        urlBuilder.show();
    }

    private void openImageActivity() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_REQUEST);
        flagTrial = true;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "OnClick ITCA", Toast.LENGTH_SHORT).show();
    }


    private void addRadio(String layout) {
        RelativeLayout linearLayout = parse(start + layout + end);
        mainView.removeAllViews();
        mainView.addView(linearLayout);
    }


    @Override
    public void onColorSelected(int dialogId, @ColorInt int color) {
        textColor = color;
//        openColorPicker.setBackgroundColor(color);
        Toast.makeText(InstructorTopicCreationActivity.this, "oOOColor :" + color,
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onDialogDismissed(int dialogId) {
    }

    @Override
    public void onSelected(int i) {
        textAppearance = Constants.textAppearance[i];
    }

    @Override
    public void playSound(String url) {
        Log.v("SoundHS", "Start");
        try {
            playAudio(url);
        } catch (Exception e) {
            Log.v("SoundHS", "Error " + e.getMessage());

            e.printStackTrace();
        }
    }

    @Override
    public void openActivity(String activity, Answer answer) {
        if (activity.equals("ColorActivity")) {
            Intent intent = new Intent(this, ColorActivity.class);
            intent.putExtra("Answer", answer);
            startActivityForResult(intent, Color_Request);
        } else if (activity.equals("TextDetection")) {
            Intent intent = new Intent(this, edu.sfsu.cs.orange.ocr.CaptureActivity.class);
            Log.v("Parser", "Answer :" + answer.getAnswer());
            intent.putExtra("Answer", answer);
            intent.putExtra("lan", "eng");
            startActivityForResult(intent, Text_Detection);
        }
    }

    @Override
    public void onImageClick(View imageView) {

    }

    private void playAudio(String url) throws Exception {
        Log.v("SoundHS", "URL : " + url);
        killMediaPlayer();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    private void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                Log.v("SoundHS", "Release");
                mediaPlayer.release();
            } catch (Exception e) {
                Log.v("SoundHS", "Error Release : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onReorder(List<String> layouts) {
        Log.v("ReOrder09",layouts.toString());
        layoutsList.clear();
        layoutsList.addAll(layouts);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
