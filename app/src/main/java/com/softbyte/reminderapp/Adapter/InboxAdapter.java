package com.softbyte.reminderapp.Adapter;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.softbyte.reminderapp.Activitys.EditEventActivity;
import com.softbyte.reminderapp.Activitys.EventActivity;
import com.softbyte.reminderapp.Database.MyDatabase;
import com.softbyte.reminderapp.Modal.DataModal;
import com.softbyte.reminderapp.R;

import java.util.ArrayList;
import java.util.Objects;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.Myclass> {

     Context context;
     TextView yes,no;
     ArrayList<DataModal> dataname;
     MyDatabase myDatabase;
     Cursor cursor;
     DataModal modal;
     TextView name;

    public InboxAdapter(Context context, ArrayList<DataModal> dataname) {
        this.context = context;
        this.dataname = dataname;
    }

    @NonNull
    @Override
    public Myclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inbox_adapter,parent,false);
        return new Myclass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myclass holder, final int position) {

        holder.ocation.setText(dataname.get(position).getName().toUpperCase());
        holder.date.setText(dataname.get(position).getSdate());
        holder.time.setText(dataname.get(position).getStime());
        holder.location.setText(dataname.get(position).getLocation());

        myDatabase = new MyDatabase(context);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,EditEventActivity.class);
                intent.putExtra("eventid",dataname.get(position).getEvenid());
                context.startActivity(intent);
            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.canceldialog);
                Objects.requireNonNull(dialog.getWindow()).setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Drawable drawable = ContextCompat.getDrawable(context,R.drawable.roundbg);
                InsetDrawable inset = new InsetDrawable(drawable, 30);
                dialog.getWindow().setBackgroundDrawable(inset);
                yes=dialog.findViewById(R.id.yes);
                no=dialog.findViewById(R.id.no);
                name = dialog.findViewById(R.id.eventname);

                name.setText(dataname.get(position).getName().toUpperCase());

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Uri deleteUri = null;
                        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI,
                                Long.parseLong(String.valueOf(dataname.get(position).getEvenid())));
                        int rows = context.getContentResolver().delete(deleteUri, null, null);

                        Toast.makeText(context, "Event deleted", Toast.LENGTH_LONG).show();

                        myDatabase.delete(Long.parseLong(String.valueOf(dataname.get(position).getEvenid())));


                        cursor = myDatabase.view_data();
                        dataname.clear();
                        while (cursor.moveToNext()){

                            modal = new DataModal();
                            modal.setEvenid(cursor.getLong(0));
                            modal.setName(cursor.getString(1));
                            modal.setSdate(cursor.getString(2));
                            modal.setStime(cursor.getString(4));
                            modal.setLocation(cursor.getString(6));

                            dataname.add(modal);

                        }

                        notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Event namae:"+dataname.get(position).getName().toUpperCase()+"\n"
                +"Date and Time :"+""+dataname.get(position).getSdate()+" "+dataname.get(position).getStime());
                context.startActivity(shareIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataname.size();
    }

    class Myclass extends RecyclerView.ViewHolder {
        ImageView share;
        TextView ocation,date,time,location;
        Button edit,cancel;
        Myclass(@NonNull View itemView) {
            super(itemView);
            share = itemView.findViewById(R.id.share);
            edit = itemView.findViewById(R.id.edit);
            cancel = itemView.findViewById(R.id.cancel);
            ocation = itemView.findViewById(R.id.tvocation);
            date = itemView.findViewById(R.id.tvdate);
            time = itemView.findViewById(R.id.tvtime);
            location = itemView.findViewById(R.id.tvlocation);
        }
    }
}
