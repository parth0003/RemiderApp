package com.softbyte.reminderapp.Fragments;


import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.softbyte.reminderapp.Adapter.InboxAdapter;
import com.softbyte.reminderapp.Database.MyDatabase;
import com.softbyte.reminderapp.Modal.DataModal;
import com.softbyte.reminderapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends Fragment {

    RecyclerView mRecyclerView;
    InboxAdapter inboxAdapter;
    ImageView imageinbox;
    TextView textinbox;
    ArrayList<DataModal> dataname;
    MyDatabase myDatabase;
    Cursor cursor;
    DataModal modal;

    public InboxFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        imageinbox = view.findViewById(R.id.inboximg);
        textinbox = view.findViewById(R.id.inboxtext);
        mRecyclerView = view.findViewById(R.id.inboxrecycler);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        myDatabase = new MyDatabase(getContext());
        dataname  = new ArrayList<DataModal>();
        cursor = myDatabase.view_data();

        while (cursor.moveToNext()){

            modal = new DataModal();
            modal.setEvenid(cursor.getLong(0));
            modal.setName(cursor.getString(1));
            modal.setSdate(cursor.getString(2));
            modal.setStime(cursor.getString(4));
            modal.setLocation(cursor.getString(6));

            dataname.add(modal);

        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        inboxAdapter = new InboxAdapter(getContext(),dataname);
        mRecyclerView.setAdapter(inboxAdapter);

        if(dataname.isEmpty()){
            imageinbox.setVisibility(View.VISIBLE);
            textinbox.setVisibility(View.VISIBLE);
        }
        else{
            imageinbox.setVisibility(View.GONE);
            textinbox.setVisibility(View.GONE);
        }
    }
}