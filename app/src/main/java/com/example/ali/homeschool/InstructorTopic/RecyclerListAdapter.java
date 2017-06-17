/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.ali.homeschool.InstructorTopic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.ali.homeschool.InstructorTopic.helper.DoneOrderInterface;
import com.example.ali.homeschool.InstructorTopic.helper.ItemTouchHelperAdapter;
import com.example.ali.homeschool.InstructorTopic.helper.ItemTouchHelperViewHolder;
import com.example.ali.homeschool.InstructorTopic.helper.OnStartDragListener;
import com.example.ali.homeschool.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.sfsu.cs.orange.ocr.Answer;


/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Paul Burke (ipaulpro)
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private List<String> mItems;
    //    Button doneBtn;
    private final OnStartDragListener mDragStartListener;
    public Context context;
    ArrayList<String> layouts;
    Activity activity;
    DoneOrderInterface doneOrderInterface;

    public RecyclerListAdapter(Context context, OnStartDragListener dragStartListener,
                               ArrayList<String> layouts, Activity activity,
                               final DoneOrderInterface doneOrderInterface) {
        mDragStartListener = dragStartListener;
//        mItems.addAll(Arrays.asList(context.getResources().getStringArray(R.array.color_array)));
        this.context = context;
        mItems = new ArrayList<>();
        mItems.addAll(layouts);
        this.activity = activity;
//        this.doneBtn = doneBtn;
        this.doneOrderInterface = doneOrderInterface;
//        doneBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doneOrderInterface.onReorder(mItems);
//                Log.v("Reorder","Items :" + mItems.toString());
//            }
//        });
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.v("Test", "View Type :" + viewType);
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.holder_ordering_layout, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        Log.v("Test", "Position :");
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        Log.v("Adapter","onBindView items :\n:" + mItems.toString());
        holder.setIsRecyclable(false);
        String layout = mItems.get(position);
        Log.v("Adapter", "onBindViewHolder : Layout :" + layout);
        InputStream stream = new ByteArrayInputStream(layout.getBytes(Charset.forName("UTF-8")));
        ParseXMLInstructor parseXMLInstructor = new ParseXMLInstructor();
        try {
            holder.view.addView(parseXMLInstructor.parse(activity, stream, context, new XMLClick() {
                @Override
                public void playSound(String url) {
                }

                @Override
                public void openActivity(String activity, Answer answer) {
                }

                @Override
                public void onImageClick(View imageView) {
                }
            }));
        } catch (XmlPullParserException e) {
            Log.v("Adapter", "Error +++" + e.getMessage());
        } catch (IOException e) {
            Log.v("Adapter", "Error +--+" + e.getMessage());
        }
        // Start a drag whenever the handle view it touched
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Log.v("Adapter","1onItemMove \n:" + mItems.toString());
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        doneOrderInterface.onReorder(mItems);
        Log.v("Adapter","2onItemMove \n:" + mItems.toString());
        return true;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addItem(String item) {
        Log.v("Adapter", "addItem : Layout :" + item);
        if (item != null) {
            mItems.add(item);
            notifyDataSetChanged();
            Log.v("Adapter", "True addItem : Layout :" + item);

        }
    }

    public void clearItems() {
        mItems.clear();
    }

    public void setArrayItems(ArrayList<String> layouts) {
        mItems.addAll(layouts);
        Log.v("0909Ada","setArrayItems : " + layouts.toString());
    }
    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        //public final TextView textView;
        public final ImageView handleView;
        public LinearLayout view;

        public ItemViewHolder(View itemView) {
            super(itemView);
//            textView = (TextView) itemView.findViewById(R.id.text);
            handleView = (ImageView) itemView.findViewById(R.id.handle);
            view = (LinearLayout) itemView.findViewById(R.id.test);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
