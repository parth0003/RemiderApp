package com.softbyte.reminderapp.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.softbyte.reminderapp.Database.MyDatabase;
import com.softbyte.reminderapp.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class EventActivity extends AppCompatActivity {

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
    long _eventId;
    int syear, smonth, sday, eyear, emonth, eday;
    int shour, smin, ehour, emin;
    Long starttime, endtime;
    Intent l;
    int inday, inmonth, inyear;
    String notistime,notietime;
    String notisdate,notiedate;
    int startmonth;
    private NotificationManager notificationManager;
    int NotID = 1;
    public static String id1 = "test_channel_01";
    public static String id2 = "test_channel_02";
    public static String id3 = "test_channel_03";
    MyDatabase myDatabase;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Date now = new Date();
        l = getIntent();
        evenname = findViewById(R.id.eventname);
        note = findViewById(R.id.note);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        dateimg = findViewById(R.id.dateimg);
        timeimg = findViewById(R.id.timeimg);
        setdate = findViewById(R.id.setdate);
        settime = findViewById(R.id.settime);
        setdateimg = findViewById(R.id.setdateimg);
        settimeimg = findViewById(R.id.settimeimg);
        autotext = findViewById(R.id.autotext);
        save = findViewById(R.id.save);
        retime = findViewById(R.id.retime);
        remindertime = findViewById(R.id.remindertime);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        autotext.setAdapter(new ArrayAdapter<>(EventActivity.this, android.R.layout.simple_list_item_1, name));

        if (checkSelfPermission(Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAMERA_REQUEST_CODE);
            }
        }

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
                inday = l.getIntExtra("sday", 0);
                inmonth = l.getIntExtra("smonth", 0);
                inyear = l.getIntExtra("syear", 0);



                if (!l.getBooleanExtra("flag", false)) {
                    startdate1.set(syear, smonth, sday, shour, smin);
                    startmonth = smonth++;
                    if (smonth >= 10) {
                        notisdate = syear + "-" + smonth + "-" + sday;
                    } else {
                        if (sday >= 10) {
                            notisdate = syear + "-" + "0" + smonth + "-" + sday;
                        } else {
                            notisdate = syear + "-" + "0" + smonth + "-" + "0" + sday;
                        }
                    }
                    notistime = shour + ":" + smin;
                } else {
                    startdate1.set(inyear, inmonth, inday, shour, smin);
                    startmonth = inmonth++;
                    if (inmonth >= 10) {
                        notisdate = inyear + "-" + inmonth + "-" + inday;
                    } else {
                        if (inday >= 10) {
                            notisdate = inyear + "-" + "0" + inmonth + "-" + inday;
                        } else {
                            notisdate = inyear + "-" + "0" + inmonth + "-" + "0" + inday;
                        }
                    }
                    notistime = shour + ":" + smin;
                }
                starttime = startdate1.getTimeInMillis();

                enddate1.set(eyear, emonth, eday, ehour, emin);
                if (emonth >= 10) {
                    notiedate = eyear + "-" + emonth + "-" + eday;
                } else {
                    if (eday >= 10) {
                        notiedate = eyear + "-" + "0" + emonth + "-" + eday;
                    } else {
                        notiedate = eyear + "-" + "0" + emonth + "-" + "0" + eday;
                    }
                }
                notietime = ehour + ":" + emin;
                endtime = enddate1.getTimeInMillis();

                if (evenname.getText().toString().isEmpty()) {
                    Toast.makeText(EventActivity.this, "Enter Event Name", Toast.LENGTH_SHORT).show();
                } else if (autotext.getText().toString().isEmpty()) {
                    Toast.makeText(EventActivity.this, "Enter Location", Toast.LENGTH_SHORT).show();
                } else {

                    myDatabase = new MyDatabase(EventActivity.this);
                    ContentResolver resolver = EventActivity.this.getContentResolver();
                    ContentValues cv = new ContentValues();
                    cv.put(CalendarContract.Events.TITLE, evenname.getText().toString());
                    cv.put(CalendarContract.Events.DESCRIPTION, note.getText().toString());
                    cv.put(CalendarContract.Events.EVENT_LOCATION, autotext.getText().toString());
                    cv.put(CalendarContract.Events.DTSTART, starttime);
                    cv.put(CalendarContract.Events.DTEND, endtime);
                    cv.put(CalendarContract.Events.CALENDAR_ID, 1);
                    cv.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());

                    if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PERMISSION_GRANTED) {

                        return;
                    }
                    Uri uri = resolver.insert(CalendarContract.Events.CONTENT_URI, cv);

                    _eventId = Long.parseLong(uri.getLastPathSegment());

                    if (remindertime.getText().toString().matches("10 minutes before the event")) {
                        setReminder(resolver, _eventId, 10);
                    } else if (remindertime.getText().toString().matches("30 minutes before the event")) {
                        setReminder(resolver, _eventId, 30);
                    } else if (remindertime.getText().toString().matches("1 hour before the event")) {
                        setReminder(resolver, _eventId, 60);
                    } else if (remindertime.getText().toString().matches("1 day before the event")) {
                        setReminder(resolver, _eventId, 1440);
                    } else {
                        Toast.makeText(EventActivity.this, "Select Reminder Duration", Toast.LENGTH_SHORT).show();
                    }

                    myDatabase.insert_data(
                            _eventId,
                            evenname.getText().toString(),
                            notisdate,
                            notiedate,
                            time.getText().toString(),
                            settime.getText().toString(),
                            autotext.getText().toString(),
                            remindertime.getText().toString(),
                            note.getText().toString());

                    simplenoti();

                    Toast.makeText(EventActivity.this, "save", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });

        retime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(EventActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.reminder_time);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Drawable drawable = ContextCompat.getDrawable(EventActivity.this, R.drawable.roundbg);
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

        setdate.setText(DateFormat.getDateInstance().format(now));
        if (!l.getBooleanExtra("flag", false)) {
            date.setText(DateFormat.getDateInstance().format(now));
        } else {
            date.setText(l.getStringExtra("date"));
            setdate.setText(l.getStringExtra("date"));
        }

        time.setText(android.text.format.DateFormat.format("hh:mm a", now));
        settime.setText(android.text.format.DateFormat.format("hh:mm a", now));
        dateimg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EventActivity.this);
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

                boolean is24HoursFormat = android.text.format.DateFormat.is24HourFormat(EventActivity.this);

                timePickerDialog = new TimePickerDialog(EventActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(EventActivity.this);
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

                boolean is24HoursFormat = android.text.format.DateFormat.is24HourFormat(EventActivity.this);

                timePickerDialog = new TimePickerDialog(EventActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

        createchannel();

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
            } else if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void createchannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id1,
                    getString(R.string.channel_name),  //name of the channel
                    NotificationManager.IMPORTANCE_DEFAULT);   //importance level
            mChannel.setDescription(getString(R.string.channel_description));
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setShowBadge(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(mChannel);

            mChannel = new NotificationChannel(id2,
                    getString(R.string.channel_name2),
                    NotificationManager.IMPORTANCE_LOW);
            mChannel.setDescription(getString(R.string.channel_description2));
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            mChannel.enableVibration(true);
            mChannel.setShowBadge(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(mChannel);

            mChannel = new NotificationChannel(id3,
                    getString(R.string.channel_name2),
                    NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription(getString(R.string.channel_description3));
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.GREEN);
            mChannel.enableVibration(true);
            mChannel.setShowBadge(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(mChannel);
        }
    }

    public void simplenoti() {

        String timer = notisdate + " " + notistime;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), id1)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(evenname.getText().toString())
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentText("StartDate :" + timer)
                .setChannelId(id1);

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(EventActivity.this, NotID, notificationIntent, 0);
        builder.setContentIntent(contentIntent);

        Notification noti = builder.build();
        notificationManager.notify(NotID, noti);
        NotID++;
    }
}