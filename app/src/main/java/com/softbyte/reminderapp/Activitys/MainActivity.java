package com.softbyte.reminderapp.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.softbyte.reminderapp.Adapter.ViewPagerAdapter;
import com.softbyte.reminderapp.Fragments.AlertsFragment;
import com.softbyte.reminderapp.Fragments.InboxFragment;
import com.softbyte.reminderapp.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements AlertsFragment.AlertFragment {

    DrawerLayout mDrawerLayout;
    Toolbar mToolbar;
    NavigationView mNavigationView;
    ViewPager viewPager;
    TabLayout tabLayout;
    FloatingActionButton floatingActionButton;
    String date;
    int sday, smonth, syear;
    boolean flag = false;
    Intent intent;
    String name, email, photo;
    TextView disname, disemail;
    CircleImageView propic;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ViewPagerAdapter viewPagerAdapter;

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("demo", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        intent = getIntent();
        viewPager = findViewById(R.id.view);
        tabLayout = findViewById(R.id.tab);
        floatingActionButton = findViewById(R.id.addevent);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mDrawerLayout = findViewById(R.id.drawer);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        mNavigationView = findViewById(R.id.navigation);
        view = mNavigationView.getHeaderView(0);
        disname = view.findViewById(R.id.disname);
        disemail = view.findViewById(R.id.disemail);
        propic = view.findViewById(R.id.disimg);

        name = sharedPreferences.getString("name", null);
        email = sharedPreferences.getString("email", null);
        photo = sharedPreferences.getString("photo", null);

        disname.setText(name);
        disemail.setText(email);
        Picasso.get().load(photo).into(propic);


        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.aboutus) {
                    startActivity(new Intent(MainActivity.this, About_Screen.class));
                }
                if (menuItem.getItemId() == R.id.shareapp) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, download this app!\n https://drive.google.com/file/d/1i4Kf6DDhIO5_7kec5zm-CS1jQ7xumKfZ/view?usp=sharing");
                    startActivity(shareIntent);
                }
                if (menuItem.getItemId() == R.id.logout) {

                    signOut();
                    editor.clear();
                    editor.commit();
                    finish();
                }
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });

        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventActivity.class);
                intent.putExtra("date", date);
                intent.putExtra("sday", sday);
                intent.putExtra("smonth", smonth);
                intent.putExtra("syear", syear);
                intent.putExtra("flag", flag);
                startActivity(intent);
            }
        });
    }

    public void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new InboxFragment(), "INBOX");
        viewPagerAdapter.addFragment(new AlertsFragment(), "ALERTS");
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onInputSent(CharSequence input) {

        date = (String) input;
        flag = true;

    }

    @Override
    public void Inputdate(int day, int month, int year) {
        sday = day;
        smonth = month;
        syear = year;

    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        LoginActivity.mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
