package com.softbyte.reminderapp.Activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.softbyte.reminderapp.Database.MyDatabase;
import com.softbyte.reminderapp.Modal.DataModal;
import com.softbyte.reminderapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class EditEventActivity extends AppCompatActivity {

    EditText note, evenname;
    TextView date, time, setdate, settime;
    int calenderhours, calenderMinute;
    TimePickerDialog timePickerDialog;
    String ampm;
    LinearLayout dateimg, timeimg, setdateimg, settimeimg, retime;
    String datepicker1;
    String datepicker2;
    String timepicker1;
    String timepicker2;
    AutoCompleteTextView autotext;
    String[] name = {"India", "Pakistan", "USA", "UAE", "UK", "Sri lanka", "New Zealand", "Australia", "Bangladesh", "South Africa"};
    Button save;
    TextView tenmin, threemin, oneh, oned, popcancel, remindertime;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    Calendar cale, cale1;
    Calendar cdate, cdate1;
    int syear, smonth, sday, eyear, emonth, eday;
    int shour, smin, ehour, emin;
    Long starttime, endtime;
    Intent l;
    Long _eventId;
    String notitime;
    String notidate;
    int startmonth;
    Intent i;
    Cursor cursor;
    MyDatabase database = new MyDatabase(this);
    DataModal modal;
    ArrayList<DataModal> dataModals;
    Long even_id;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        evenname = findViewById(R.id.editeventname);
        note = findViewById(R.id.editnote);
        date = findViewById(R.id.editdate);
        time = findViewById(R.id.edittime);
        dateimg = findViewById(R.id.editdateimg);
        timeimg = findViewById(R.id.edittimeimg);
        setdate = findViewById(R.id.editsetdate);
        settime = findViewById(R.id.editsettime);
        setdateimg = findViewById(R.id.editsetdateimg);
        settimeimg = findViewById(R.id.editsettimeimg);
        autotext = findViewById(R.id.editautotext);
        save = findViewById(R.id.editupdate);
        retime = findViewById(R.id.editretime);
        remindertime = findViewById(R.id.editremindertime);
        i = getIntent();

        Date now = new Date();
        database = new MyDatabase(this);
        dataModals = new ArrayList<>();
        cursor = database.viewdata(i.getLongExtra("eventid", 0));
        while (cursor.moveToNext()) {

            modal = new DataModal();
            modal.setEvenid(cursor.getLong(0));
            modal.setName(cursor.getString(1));
            modal.setSdate(cursor.getString(2));
            modal.setEdate(cursor.getString(3));
            modal.setStime(cursor.getString(4));
            modal.setEtime(cursor.getString(5));
            modal.setLocation(cursor.getString(6));
            modal.setRemindertime(cursor.getString(7));
            modal.setNote(cursor.getString(8));
            dataModals.add(modal);

        }

        even_id = dataModals.get(0).getEvenid();
        evenname.setText(dataModals.get(0).getName());
        date.setText(dataModals.get(0).getSdate());
        time.setText(dataModals.get(0).getStime());
        setdate.setText(dataModals.get(0).getEdate());
        settime.setText(dataModals.get(0).getEtime());
        autotext.setText(dataModals.get(0).getLocation());
        remindertime.setText(dataModals.get(0).getRemindertime());
        note.setText(dataModals.get(0).getNote());

        autotext.setAdapter(new ArrayAdapter<>(EditEventActivity.this, android.R.layout.simple_list_item_1, name));

        Calendar startdate1 = Calendar.getInstance();
        syear = startdate1.get(Calendar.YEAR);
        smonth = startdate1.get(Calendar.MONTH);
        sday = startdate1.get(Calendar.DAY_OF_MONTH);
        shour = startdate1.get(Calendar.HOUR);
        smin = startdate1.get(Calendar.MINUTE);

        Calendar enddate1 = Calendar.getInstance();
        eyear = enddate1.get(Calendar.YEAR);
        emonth = enddate1.get(Calendar.MONTH);
        eday = enddate1.get(Calendar.DAY_OF_MONTH);
        ehour = enddate1.get(Calendar.HOUR);
        emin = enddate1.get(Calendar.MINUTE);

        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                Uri deleteUri1 = null;
                deleteUri1 = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI,
                        Long.parseLong(String.valueOf(i.getLongExtra("eventid", 0))));
                int rows = getContentResolver().delete(deleteUri1, null, null);

                database.delete(Long.parseLong(String.valueOf(i.getLongExtra("eventid", 0))));


                startdate1.set(syear, smonth, sday, shour, smin);
                    startmonth = smonth++;
                    if (smonth >= 10) {
                        notidate = syear + "-" + smonth + "-" + sday;
                    } else {
                        if (sday >= 10) {
                            notidate = syear + "-" + "0" + smonth + "-" + sday;
                        } else {
                            notidate = syear + "-" + "0" + smonth + "-" + "0" + sday;
                        }
                    }
                    notitime = shour + ":" + smin;
                starttime = startdate1.getTimeInMillis();

                enddate1.set(eyear, emonth, eday, ehour, emin);
                endtime = enddate1.getTimeInMillis();

                if (evenname.getText().toString().isEmpty()) {
                    Toast.makeText(EditEventActivity.this, "Enter Event Name", Toast.LENGTH_SHORT).show();
                } else if (autotext.getText().toString().isEmpty()) {
                    Toast.makeText(EditEventActivity.this, "Enter Location", Toast.LENGTH_SHORT).show();
                } else {

                    database = new MyDatabase(EditEventActivity.this);
                    ContentResolver resolver = EditEventActivity.this.getContentResolver();
                    ContentValues cv1 = new ContentValues();
                    cv1.put(CalendarContract.Events.TITLE, evenname.getText().toString());
                    cv1.put(CalendarContract.Events.DESCRIPTION, note.getText().toString());
                    cv1.put(CalendarContract.Events.EVENT_LOCATION, autotext.getText().toString());
                    cv1.put(CalendarContract.Events.DTSTART, starttime);
                    cv1.put(CalendarContract.Events.DTEND, endtime);
                    cv1.put(CalendarContract.Events.CALENDAR_ID, 1);
                    cv1.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());

                    if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PERMISSION_GRANTED) {

                        return;
                    }
                    Uri uri = resolver.insert(CalendarContract.Events.CONTENT_URI, cv1);

                    _eventId = Long.parseLong(uri.getLastPathSegment());
                    if (remindertime.getText().toString().matches("10 minutes before the event")) {
                        setReminder(resolver,_eventId, 10);
                    } else if (remindertime.getText().toString().matches("30 minutes before the event")) {
                        setReminder(resolver, _eventId, 30);
                    } else if (remindertime.getText().toString().matches("1 hour before the event")) {
                        setReminder(resolver, _eventId, 60);
                    } else if (remindertime.getText().toString().matches("1 day before the event")) {
                        setReminder(resolver, _eventId, 1440);
                    } else {
                        Toast.makeText(EditEventActivity.this, "Select Reminder Duration", Toast.LENGTH_SHORT).show();
                    }

                    database.insert_data(
                            _eventId,
                            evenname.getText().toString(),
                            notidate,
                            setdate.getText().toString(),
                            time.getText().toString(),
                            settime.getText().toString(),
                            autotext.getText().toString(),
                            remindertime.getText().toString(),
                            note.getText().toString());

                    Toast.makeText(EditEventActivity.this, "save", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });

        retime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(EditEventActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.reminder_time);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Drawable drawable = ContextCompat.getDrawable(EditEventActivity.this, R.drawable.roundbg);
                InsetDrawable inset = new InsetDrawable(drawable, 30);
                dialog.getWindow().setBackgroundDrawable(inset);
                tenmin = dialog.findViewById(R.id.tenmin);
                threemin = dialog.findViewById(R.id.threemin);
                oneh = dialog.findViewById(R.id.oneh);
                oned = dialog.findViewById(R.id.oned);
                popcancel = dialog.findViewById(R.id.popupcancel);

                tenmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String tenmins = tenmin.getText().toString();
                        remindertime.setText(tenmins);
                        dialog.dismiss();
                    }
                });

                threemin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String threemins = threemin.getText().toString();
                        remindertime.setText(threemins);
                        dialog.dismiss();
                    }
                });

                oneh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String onehs = oneh.getText().toString();
                        remindertime.setText(onehs);
                        dialog.dismiss();
                    }
                });

                oned.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String oneds = oned.getText().toString();
                        remindertime.setText(oneds);
                        dialog.dismiss();
                    }
                });

                popcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        dateimg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditEventActivity.this);
                datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cdate1 = Calendar.getInstance();
                        cdate1.set(Calendar.YEAR, year);
                        cdate1.set(Calendar.MONTH, month);
                        cdate1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        datepicker1 = DateFormat.getDateInstance().format(cdate1.getTime());
                        date.setText(datepicker1);
                        setdate.setText(datepicker1);
                        syear = year;
                        smonth = month;
                        sday = dayOfMonth;
                        eyear = year;
                        emonth = month;
                        eday = dayOfMonth;
                    }
                });
                datePickerDialog.show();
            }
        });

        timeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                calenderhours = cal.get(Calendar.HOUR);
                calenderMinute = cal.get(Calendar.MINUTE);

                boolean is24HoursFormat = android.text.format.DateFormat.is24HourFormat(EditEventActivity.this);

                timePickerDialog = new TimePickerDialog(EditEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            ampm = "p.m.";
                        } else {
                            ampm = "a.m.";
                        }
                        cale = Calendar.getInstance();
                        cale.set(Calendar.HOUR, hourOfDay);
                        cale.set(Calendar.MINUTE, minute);
                        timepicker1 = android.text.format.DateFormat.format("hh:mm", cale) + " " + ampm;
                        time.setText(timepicker1);
                        shour = hourOfDay;
                        smin = minute;
                    }
                }, calenderhours, calenderMinute, is24HoursFormat);
                timePickerDialog.show();
            }
        });

        setdateimg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(EditEventActivity.this);
                datePickerDialog1.getDatePicker().setMinDate(new Date().getTime());
                datePickerDialog1.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cdate = Calendar.getInstance();
                        cdate.set(Calendar.YEAR, year);
                        cdate.set(Calendar.MONTH, month);
                        cdate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        datepicker2 = DateFormat.getDateInstance().format(cdate.getTime());
                        setdate.setText(datepicker2);
                        eyear = year;
                        emonth = month;
                        eday = dayOfMonth;
                    }
                });
                datePickerDialog1.show();
            }
        });

        settimeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                calenderhours = cal.get(Calendar.HOUR);
                calenderMinute = cal.get(Calendar.MINUTE);

                boolean is24HoursFormat = android.text.format.DateFormat.is24HourFormat(EditEventActivity.this);

                timePickerDialog = new TimePickerDialog(EditEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            ampm = "p.m.";
                        } else {
                            ampm = "a.m.";
                        }
                        cale1 = Calendar.getInstance();
                        cale1.set(Calendar.HOUR, hourOfDay);
                        cale1.set(Calendar.MINUTE, minute);
                        timepicker2 = android.text.format.DateFormat.format("hh:mm", cale1) + " " + ampm;
                        settime.setText(timepicker2);
                        ehour = hourOfDay;
                        emin = minute;
                    }
                }, calenderhours, calenderMinute, is24HoursFormat);
                timePickerDialog.show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setReminder(ContentResolver cr, long eventID, int timeBefore) {
        try {
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Reminders.MINUTES, timeBefore);
            values.put(CalendarContract.Reminders.EVENT_ID, eventID);
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Uri uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
            Cursor c = CalendarContract.Reminders.query(cr, eventID,
                    new String[]{CalendarContract.Reminders.MINUTES});
            if (c.moveToFirst()) {
                System.out.println("calendar"
                        + c.getInt(c.getColumnIndex(CalendarContract.Reminders.MINUTES)));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

