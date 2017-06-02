package com.example.ali.homeschool.closed;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ali.homeschool.childClass.ClassActivity;
import com.example.ali.homeschool.controller.activities.Guest;
import com.example.ali.homeschool.R;
import com.example.ali.homeschool.Utils;
import com.example.ali.homeschool.data.Entry.TopicColumns;
import com.example.ali.homeschool.data.DataProvider;

import java.io.InputStream;

public class MainActivity2Triallll_______________ extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int CURSOR_LOADER_ID = 0;
    Button startTopic ;
    Button startHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_trial);
        if(savedInstanceState==null){
            try {
                Utils.addDefaultsSubjects(getApplicationContext());
                Utils.addCoursesTest(getApplicationContext());
                Utils.addLessonsTest(getApplicationContext());
                Utils.addTopicsTest(getApplicationContext());
                Utils.addTopicsContentsTest(getApplicationContext());
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }

        }
        InputStream stream = null;
        String layout="<LinearLayout\n" +
                "        android:id=\"@+id/LinearLayoutID\"><ImageView\n" +
                "        android:id=\"@+id/dd\"\n" +
                "        android:src=\"@drawable/ic_launcher\"\n" +
                "        android:layout_width=\"wrap_content\"\n" +
                "        android:layout_height=\"wrap_content\" />\n" +
                "    <TextView\n" +
                "        android:id=\"@+id/djd\"\n" +
                "        android:layout_width=\"wrap_content\"\n" +
                "        android:layout_height=\"wrap_content\" /> <LinearLayout\n android:id=\"@+id/innerLinearLayoutID\"" +
                "      android:layout_width=\"wrap_content\"\n" +
                "      android:layout_height=\"wrap_content\">\n" +
                "    <TextView\n" +
                "        android:id=\"@+id/textViewInsideLinear\"\n" +
                "        android:layout_width=\"wrap_content\"\n" +
                "        android:layout_height=\"wrap_content\" />\n" +"  <Button\n" +
                "        android:id=\"@+id/buttonID\"\n" +
                "        android:onClick=\"Function\"\n" +
                "        android:layout_width=\"wrap_content\"\n" +
                "        android:layout_height=\"wrap_content\" />"+
                "  </LinearLayout>  </LinearLayout>";
//        stream = new ByteArrayInputStream(layout.getBytes(Charset.forName("UTF-8")));
//        ParseXMLInstructor parseXML = new ParseXMLInstructor();
//        List<ParseXMLInstructor.ViewX> views =  null;
//        String id=null;
//        ParseXMLInstructor.ViewX viewX ;
//        try {
//            viewX=parseXML.parse(stream);
//
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    //    if(views!=null) {
            //Log.v("Test", "Views.Empty: " + views.isEmpty());
            //Log.v("Test", "Views.length: " + views.size());
            //Log.v("Test","Views.get(0).id : "+views.get(0).id);
            //for (ParseXMLInstructor.ViewX x : views) {
              //  Log.v("Test", "Id : " + x.id);
            //}

     //   } else {
           // Log.v("Test", "Views == null");
     //   }
       // for (ParseXMLInstructor.ImageViewX x:views){
         //   Log.v("Test","Id : " +x.id);
        //}

        startTopic = (Button) findViewById(R.id.startTopic);
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        startTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ClassActivity.class);
                startActivity(intent);
            }
        });
        startHome = (Button) findViewById(R.id.startHome);
        startHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Guest.class);
                startActivity(intent);
            }
        });
       // RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
      //  listView=(ListView) findViewById(R.id.list_view);
        //cursorAdapter=new CategoryCursorAdapter(this, null, new CategoryCursorAdapter.CategoryOnClickHander() {
        //    @Override
          //  public void onClick(String test, CategoryCursorAdapter.ViewHolder vh) {
            //    Log.v("Test",test);
            //}
   //     });
     //   recyclerView.setAdapter(cursorAdapter);
        //listView.setAdapter(cursorAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
    }
    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Subject Test
        //CursorLoader cursorLoader= new CursorLoader(this, DataProvider.Subject.CONTENT_URI,
        //        new String[]{SubjectColumns._ID,SubjectColumns.SUBJECT_NAME},null,null,null);
        //Course Test
        //CursorLoader cursorLoader = new CursorLoader(this,DataProvider.Course.CONTENT_URI,
        //      new String[]{CourseColumns._ID,CourseColumns.COURSE_NAME},null,null,null);
        //Lesson Test
        //CursorLoader cursorLoader = new CursorLoader(this,DataProvider.Lesson.CONTENT_URI,
        //        new String[]{LessonColumns._ID,LessonColumns.LESSON_NAME},null,null,null);
        //Topic Test
        CursorLoader cursorLoader = new CursorLoader(this,DataProvider.Topic.CONTENT_URI,
               new String[]{TopicColumns._ID,TopicColumns.TOPIC_NAME},null,null,null);
        //TopicContent Test
       // CursorLoader cursorLoader = new CursorLoader(this,DataProvider.TopicContent.CONTENT_URI,
         //       new String[]{TopicContentColumns._ID,TopicContentColumns.CONTENT},null,null,null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       //cursorAdapter.swapCursor(data);
        data.moveToFirst();
        //Subject Test
        //String test = data.getString(data.getColumnIndex(SubjectColumns.SUBJECT_NAME));
        //Course Test
        //String test = data.getString(data.getColumnIndex(CourseColumns.COURSE_NAME));
        //Lesson Test
        //String test = data.getString(data.getColumnIndex(LessonColumns.LESSON_NAME));
        //Topic Test
        String test = data.getString(data.getColumnIndex(TopicColumns.TOPIC_NAME));
        //TopicContent Test
        //byte[] test = data.getBlob(data.getColumnIndex(TopicContentColumns.CONTENT));
        Log.v("Test"," "+test);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //cursorAdapter.swapCursor(null);
    }
}
