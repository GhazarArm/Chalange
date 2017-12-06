package com.example.ghazar.chalange.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ghazar.chalange.Dialogs.SearchDialog;
import com.example.ghazar.chalange.FirstPage.FirstActivity;
import com.example.ghazar.chalange.HelperClases.BadgeDrawerArrowDrawable;
import com.example.ghazar.chalange.Objects.Account;
import com.example.ghazar.chalange.Objects.Database;
import com.example.ghazar.chalange.Objects.Events;
import com.example.ghazar.chalange.Objects.Frends;
import com.example.ghazar.chalange.R;
import com.example.ghazar.chalange.Tabs.FragmentAdapter;
import com.example.ghazar.chalange.Tabs.MyProfile;
import com.example.ghazar.chalange.Tabs.Tag1;
import com.example.ghazar.chalange.Tabs.Tag2;
import com.google.firebase.database.DataSnapshot;

import java.util.Vector;

public class MainActivity extends AppCompatActivity
                          implements NavigationView.OnNavigationItemSelectedListener {



    public final int REQUEST_CODE_OF_FREND_REQUEST_ACTIVITY = 0;
    public final int REQUEST_CODE_OF_GUEST_ACTIVITY = 1;

    public static MainActivity m_mainActivity;
    public Account m_curentAccount;

    public Tag1 m_tab1;
    public Tag2 m_tab2;
    public MyProfile m_myProfileTab;

    private ImageView m_navigationHeadericon;
    private TextView m_navigationHeaderTitle;
    private BadgeDrawerArrowDrawable badgeDrawable;
    private NavigationView navigationView;

    MenuItem m_searchItem;
    public SearchDialog m_searchDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_mainActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.a);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) ;

        badgeDrawable = new BadgeDrawerArrowDrawable(getSupportActionBar().getThemedContext());
        toggle.setDrawerArrowDrawable(badgeDrawable);
        badgeDrawable.setEnabled(false);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        m_navigationHeaderTitle = (TextView) hView.findViewById(R.id.account_name_text_view);
        m_navigationHeadericon = (ImageView) hView.findViewById(R.id.account_image_view);

        ViewPager viewPages = (ViewPager) findViewById(R.id.vp_pages);
        PagerAdapter pagerAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPages.setAdapter(pagerAdapter);
        viewPages.setCurrentItem(1);

        TabLayout tbl_pages = (TabLayout) findViewById(R.id.tbl_pages);
        tbl_pages.setupWithViewPager(viewPages);
        tbl_pages.getTabAt(0).setIcon(R.drawable.my_profile);
        tbl_pages.getTabAt(1).setIcon(R.drawable.friends);
        tbl_pages.getTabAt(2).setIcon(R.drawable.search);

        viewPages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    m_searchItem.setVisible(true);
                } else {
                    m_searchItem.setVisible(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        Intent intent = getIntent();
        String id = intent.getStringExtra(Database.NAME);
        String first_name = intent.getStringExtra(Database.NAME);
        String last_name = intent.getStringExtra(Database.LAST_NAME);
        int age = intent.getIntExtra(Database.AGE, 0);
        boolean gender = intent.getBooleanExtra(Database.GENDER, true);
        m_curentAccount = new Account(first_name, last_name, age, gender, id);
        initNavigationHeader(first_name, last_name);
        initCounter();
    }

    public void setTab1(Tag1 tab1) {
        m_tab1 = tab1;
    }

    public void setTab2(Tag2 tab2) {
        m_tab2 = tab2;
    }

    public void setTabMyProfile(MyProfile myProfile) {
        m_myProfileTab = myProfile;
        m_myProfileTab.InitButtonsText();
    }

    public void setBadgeDrawableCount(int count)
    {
        badgeDrawable.setEvents(count);
    }

    public void initNavigationHeader(String name, String lastName){
        m_navigationHeaderTitle.setText(name + "  " + lastName);
        m_navigationHeadericon.setImageResource(m_mainActivity.getIconId(name));
    }

    public void initFrendsList()
    {
        Vector<String> frends = new Vector<String>();
        for (DataSnapshot postSnapshot : FirstActivity.m_database.m_AccountFrendsDataSnapshot.child(Frends.FRENDS_VECTOR_KEY).getChildren()) {
            String frendId = postSnapshot.getValue(String.class);
            frends.add(frendId);
        }
        m_tab1.initListView(frends);
    }

    public void initCounter()
    {
        int frendRequestCount = 0;
        int guestsCount = 0;
        for (DataSnapshot postSnapshot : FirstActivity.m_database.m_AccountEventsDataSnapshot.getChildren()) {
            if (postSnapshot.child(Events.EVENT_KEY).getValue(String.class).equals(Events.FREND_REQUEST_EVENT_KEY))
                ++frendRequestCount;
            else if (postSnapshot.child(Events.EVENT_KEY).getValue(String.class).equals(Events.ACCOUNT_GUEST_KEY))
                ++guestsCount;
        }
        try {
            MainActivity.m_mainActivity.setFrendCounter(frendRequestCount);
            MainActivity.m_mainActivity.setGuestCounter(guestsCount);
            badgeDrawable.setEvents(guestsCount + frendRequestCount);
        }catch (NullPointerException ex){
            Log.e("MY ERROR", ex.toString());
        }
    }

    public void goGuest(String id){
        Intent intent = new Intent(this,  OtherProfileActivity.class);
        intent.putExtra(Database.ID, id);
        startActivityForResult(intent, 0);
    }

    public void setFrendCounter(int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(R.id.nav_frendRequest).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);
    }

    public void setGuestCounter(int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(R.id.nav_guest).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);
    }

    public int getIconId(String name)
    {
        switch (name.charAt(0)) {
            case 'a' : return R.drawable.a;
            case 'A' : return R.drawable.a;
            case 'b' : return R.drawable.b;
            case 'B' : return R.drawable.b;
            case 'c' : return R.drawable.c;
            case 'C' : return R.drawable.c;
            case 'd' : return R.drawable.d;
            case 'D' : return R.drawable.d;
            case 'e' : return R.drawable.e;
            case 'E' : return R.drawable.e;
            case 'f' : return R.drawable.f;
            case 'F' : return R.drawable.f;
            case 'g' : return R.drawable.g;
            case 'G' : return R.drawable.g;
            case 'h' : return R.drawable.h;
            case 'H' : return R.drawable.h;
            case 'i' : return R.drawable.i;
            case 'I' : return R.drawable.i;
            case 'j' : return R.drawable.j;
            case 'J' : return R.drawable.j;
            case 'k' : return R.drawable.k;
            case 'K' : return R.drawable.k;
            case 'l' : return R.drawable.l;
            case 'L' : return R.drawable.l;
            case 'm' : return R.drawable.m;
            case 'M' : return R.drawable.m;
            case 'n' : return R.drawable.n;
            case 'N' : return R.drawable.n;
            case 'o' : return R.drawable.o;
            case 'O' : return R.drawable.o;
            case 'p' : return R.drawable.p;
            case 'P' : return R.drawable.p;
            case 'q' : return R.drawable.q;
            case 'Q' : return R.drawable.q;
            case 'r' : return R.drawable.r;
            case 'R' : return R.drawable.r;
            case 's' : return R.drawable.s;
            case 'S' : return R.drawable.s;
            case 't' : return R.drawable.t;
            case 'T' : return R.drawable.t;
            case 'u' : return R.drawable.u;
            case 'U' : return R.drawable.u;
            case 'v' : return R.drawable.v;
            case 'V' : return R.drawable.v;
            case 'w' : return R.drawable.w;
            case 'W' : return R.drawable.w;
            case 'x' : return R.drawable.x;
            case 'X' : return R.drawable.x;
            case 'y' : return R.drawable.y;
            case 'Y' : return R.drawable.y;
            case 'z' : return R.drawable.z;
            case 'Z' : return R.drawable.z;
            default:   return R.drawable.ic_menu_slideshow;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        m_searchItem = (MenuItem)menu.findItem(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }else  if (id == R.id.action_search) {
            m_searchDialog = new SearchDialog(this);
            m_searchDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_guest) {
            Intent intent = new Intent(this, GuestActivity.class);
            startActivityForResult(intent, REQUEST_CODE_OF_GUEST_ACTIVITY);
        } else if (id == R.id.nav_frendRequest) {
            Intent intent = new Intent(this, FrendRequestActivity.class);
            startActivityForResult(intent, REQUEST_CODE_OF_FREND_REQUEST_ACTIVITY);
        } else if (id == R.id.nav_sound) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
