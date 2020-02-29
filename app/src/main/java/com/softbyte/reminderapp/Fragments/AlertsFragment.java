package com.softbyte.reminderapp.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.softbyte.reminderapp.Database.MyDatabase;
import com.softbyte.reminderapp.Modal.DataModal;
import com.softbyte.reminderapp.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.vo.DateData;

public class AlertsFragment extends Fragment{

    private AlertFragment alertFragment;
    private String date;
    private Calendar cdate1;
    private int sday,smonth,syear;
    private List<DataModal> getList;
    private MyDatabase database;
    private View view;
    private MCalendarView calendarView;
    private String[] parts;
    private String date0;
    private int y,m,d;

    public interface AlertFragment{

        void onInputSent(CharSequence input);
        void Inputdate(int day,int month,int year);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.fragment_alerts, container, false);

        CalendarView mCalendarView = view.findViewById(R.id.calenderview);
        calendarView = view.findViewById(R.id.calendar);


        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                cdate1 = Calendar.getInstance();
                cdate1.set(Calendar.YEAR, year);
                cdate1.set(Calendar.MONTH, month);
                cdate1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                date = DateFormat.getDateInstance().format(cdate1.getTime());
                CharSequence sequence = date;
                alertFragment.onInputSent(sequence);
                sday = dayOfMonth;
                smonth = month;
                syear = year;
                alertFragment.Inputdate(sday,smonth,syear);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getList = new ArrayList<>();
        database = new MyDatabase(getActivity());
        Cursor cursor = database.view_data();
        while (cursor.moveToNext()){

            DataModal modal = new DataModal();
            modal.setSdate(cursor.getString(2));
            getList.add(modal);

        }

        if (getList.isEmpty()){

        }else {

            for (int i = 0; i < getList.size(); i++) {
                date0 = getList.get(i).getSdate();

                parts =  date0.split("-");
                y = Integer.parseInt(parts[0]);
                m = Integer.parseInt(parts[1]);
                d = Integer.parseInt(parts[2]);
                calendarView.markDate( new DateData(y,m,d).setMarkStyle(new MarkStyle(MarkStyle.DOT, Color.GRAY)));

            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AlertFragment){
            alertFragment = (AlertFragment) context;
        }else{
            throw new RuntimeException(context.toString()+"must implement AlertFragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        alertFragment = null;
    }
}