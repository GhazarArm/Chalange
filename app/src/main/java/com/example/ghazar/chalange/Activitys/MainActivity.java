package com.example.ghazar.chalange.Activitys;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.andremion.counterfab.CounterFab;
import com.bumptech.glide.Glide;
import com.example.ghazar.chalange.Dialogs.ChallangeRequestDialog;
import com.example.ghazar.chalange.Dialogs.SearchDialog;
import com.example.ghazar.chalange.FirstPage.FirstActivity;
import com.example.ghazar.chalange.HelperClases.BadgeDrawerArrowDrawable;
import com.example.ghazar.chalange.HelperClases.MultiTouchListener;
import com.example.ghazar.chalange.Objects.Account;
import com.example.ghazar.chalange.Objects.Database;
import com.example.ghazar.chalange.Objects.Events;
import com.example.ghazar.chalange.Objects.Frends;
import com.example.ghazar.chalange.R;
import com.example.ghazar.chalange.Tabs.FragmentAdapter;
import com.example.ghazar.chalange.Tabs.MyProfile;
import com.example.ghazar.chalange.Tabs.Tag1;
import com.example.ghazar.chalange.Tabs.Tag2;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.Vector;

public class MainActivity extends AppCompatActivity
                          implements NavigationView.OnNavigationItemSelectedListener {



    public final int REQUEST_CODE_OF_FREND_REQUEST_ACTIVITY = 0;
    public final int REQUEST_CODE_OF_GUEST_ACTIVITY = 1;
    public final int REQUEST_CODE_OF_CHALLANGE_ACTIVITY = 2;

    public static MainActivity m_mainActivity;
    public Account m_curentAccount;

    public Tag1 m_tab1;
    public Tag2 m_tab2;
    public MyProfile m_myProfileTab;

    private ImageView m_navigationHeadericon;
    private TextView m_navigationHeaderTitle;
    private BadgeDrawerArrowDrawable badgeDrawable;
    private NavigationView navigationView;
    private CounterFab m_fab;

    MenuItem m_searchItem;
    public SearchDialog m_searchDialog;
    public ChallangeRequestDialog m_challangeDialog;

    private float x;
    private float y;

    public String m_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_mainActivity = this;

        m_fab = (CounterFab) findViewById(R.id.fab);
        m_fab.setCount(FirstActivity.m_database.getChallangesRequestsCount());
        m_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChallangeRequestDialog();
            }

        });
        MultiTouchListener touchListener=new MultiTouchListener();
        m_fab.setOnTouchListener(touchListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.a);

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
        m_id = intent.getStringExtra(Database.ID);
        String first_name = intent.getStringExtra(Database.NAME);
        String last_name = intent.getStringExtra(Database.LAST_NAME);
        int age = intent.getIntExtra(Database.AGE, 0);
        boolean gender = intent.getBooleanExtra(Database.GENDER, true);
        m_curentAccount = new Account(first_name, last_name, age, gender, m_id);
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
        getIcon(name, m_navigationHeadericon);
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

    public void startChallangeWith(String id){
        Intent intent = new Intent(this, WaitingChallengeRequestActivity.class);
        intent.putExtra(Database.ID, id);
        startActivityForResult(intent, REQUEST_CODE_OF_CHALLANGE_ACTIVITY);
        FirstActivity.m_database.sendChallangeRequest(id);
    }

    public void acceptChallangeWith(String id){
        Intent intent = new Intent(this, WaitingChallengeRequestActivity.class);
        intent.putExtra(Database.ID, id);
        intent.putExtra("Accept", true);
        startActivityForResult(intent, REQUEST_CODE_OF_CHALLANGE_ACTIVITY);
        FirstActivity.m_database.sendChallangeAcceptRequest(id);
        FirstActivity.m_database.deleteMyEvents(id, Events.CHALANGE_REQUEST_EVENT_KEY);
    }

    public void cancelChallangeWith(String id){
        FirstActivity.m_database.sendChallangeCancelRequest(id);
        FirstActivity.m_database.deleteMyEvents(id, Events.CHALANGE_REQUEST_EVENT_KEY);
    }

    public void openChallangeRequestDialog(){
        Vector<String> id = FirstActivity.m_database.getChallangeRequests();
        if(id.size() <= 0)
            return;
        if(m_challangeDialog == null){
            m_challangeDialog = new ChallangeRequestDialog(this);
            m_challangeDialog.initListView(id);
            m_challangeDialog.show();
        }else{
            m_challangeDialog.initListView(id);
            m_challangeDialog.show();
        }
    }

    public void setFabCount(int count){
        m_fab.setCount(count);
        if(m_challangeDialog != null){
            Vector<String> id = FirstActivity.m_database.getChallangeRequests();
            m_challangeDialog.initListView(id);
        }
    }

    public void getIcon(String name, ImageView image){
        String url;
        if(image == null)
            image = new ImageView(getApplicationContext());
        switch (name.charAt(0)) {
            case 'a' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fa.png?alt=media&token=fce54a01-6611-4d66-8c6a-f24f9d28fc28"; break;
            case 'A' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fa.png?alt=media&token=fce54a01-6611-4d66-8c6a-f24f9d28fc28"; break;
            case 'b' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fb.png?alt=media&token=02c31d64-b374-49bd-9343-0766ba6160ac"; break;
            case 'B' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fb.png?alt=media&token=02c31d64-b374-49bd-9343-0766ba6160ac"; break;
            case 'c' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fc.png?alt=media&token=090a55da-5113-433c-9fc6-2d38369abb05"; break;
            case 'C' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fc.png?alt=media&token=090a55da-5113-433c-9fc6-2d38369abb05"; break;
            case 'd' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fd.png?alt=media&token=7bf58171-2923-41e2-9330-7b8ead3ede57"; break;
            case 'D' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fd.png?alt=media&token=7bf58171-2923-41e2-9330-7b8ead3ede57"; break;
            case 'e' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fe.png?alt=media&token=1d259524-0094-4743-9c26-c959174ea8d2"; break;
            case 'E' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fe.png?alt=media&token=1d259524-0094-4743-9c26-c959174ea8d2"; break;
            case 'f' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Ff.png?alt=media&token=5f45a358-c08e-45b0-95af-92874871a89e"; break;
            case 'F' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Ff.png?alt=media&token=5f45a358-c08e-45b0-95af-92874871a89e"; break;
            case 'g' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fg.png?alt=media&token=8c444db9-617c-442a-86ba-5ad308f2cd2a"; break;
            case 'G' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fg.png?alt=media&token=8c444db9-617c-442a-86ba-5ad308f2cd2a"; break;
            case 'h' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fh.png?alt=media&token=03406ec5-d9c5-41b7-81c3-0c49977d1ef4"; break;
            case 'H' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fh.png?alt=media&token=03406ec5-d9c5-41b7-81c3-0c49977d1ef4"; break;
            case 'i' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fi.png?alt=media&token=80b540ff-36dd-49e1-8e95-23e6c67922b5"; break;
            case 'I' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fi.png?alt=media&token=80b540ff-36dd-49e1-8e95-23e6c67922b5"; break;
            case 'j' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fj.png?alt=media&token=bce4d02d-04f0-43c3-aa06-84bc86f0101d"; break;
            case 'J' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fj.png?alt=media&token=bce4d02d-04f0-43c3-aa06-84bc86f0101d"; break;
            case 'k' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fk.png?alt=media&token=07222357-8854-4fbd-94af-e6a271637159"; break;
            case 'K' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fk.png?alt=media&token=07222357-8854-4fbd-94af-e6a271637159"; break;
            case 'l' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fl.png?alt=media&token=af0baee0-c120-494f-b043-dcf49d07f685"; break;
            case 'L' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fl.png?alt=media&token=af0baee0-c120-494f-b043-dcf49d07f685"; break;
            case 'm' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fm.png?alt=media&token=7ee07af9-3a09-44b3-b1bc-c84594e3660f"; break;
            case 'M' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fm.png?alt=media&token=7ee07af9-3a09-44b3-b1bc-c84594e3660f"; break;
            case 'n' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fn.png?alt=media&token=e6401826-4916-4bab-b605-ef759c66266a"; break;
            case 'N' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fn.png?alt=media&token=e6401826-4916-4bab-b605-ef759c66266a"; break;
            case 'o' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fo.png?alt=media&token=dd87ce08-8ea3-4aab-b3d3-fdd322abfee3"; break;
            case 'O' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fo.png?alt=media&token=dd87ce08-8ea3-4aab-b3d3-fdd322abfee3"; break;
            case 'p' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fp.png?alt=media&token=9f64c565-31a7-4d65-8b20-b9c1218642ec"; break;
            case 'P' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fp.png?alt=media&token=9f64c565-31a7-4d65-8b20-b9c1218642ec"; break;
            case 'q' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fq.png?alt=media&token=f32f94bb-3e43-476d-9fa6-57cc0f803c1f"; break;
            case 'Q' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fq.png?alt=media&token=f32f94bb-3e43-476d-9fa6-57cc0f803c1f"; break;
            case 'r' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fr.png?alt=media&token=70f96f97-d37f-476f-b222-743402234dfa"; break;
            case 'R' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fr.png?alt=media&token=70f96f97-d37f-476f-b222-743402234dfa"; break;
            case 's' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fs.png?alt=media&token=18ffa759-4494-43a7-831d-847c111732a0"; break;
            case 'S' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fs.png?alt=media&token=18ffa759-4494-43a7-831d-847c111732a0"; break;
            case 't' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Ft.png?alt=media&token=440d56e0-4764-43f5-9b2d-e2367d8e23cb"; break;
            case 'T' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Ft.png?alt=media&token=440d56e0-4764-43f5-9b2d-e2367d8e23cb"; break;
            case 'u' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fu.png?alt=media&token=6b016ff7-45c7-405a-b297-92b89ad2ee05"; break;
            case 'U' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fu.png?alt=media&token=6b016ff7-45c7-405a-b297-92b89ad2ee05"; break;
            case 'v' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fv.png?alt=media&token=ca655063-f59f-4295-a857-7710fd037a0a"; break;
            case 'V' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fv.png?alt=media&token=ca655063-f59f-4295-a857-7710fd037a0a"; break;
            case 'w' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fw.png?alt=media&token=9d4d77d8-16df-490f-9333-7a5bc0dcf850"; break;
            case 'W' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fw.png?alt=media&token=9d4d77d8-16df-490f-9333-7a5bc0dcf850"; break;
            case 'x' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fx.png?alt=media&token=cdb17dd7-20fc-457c-b5b4-4cf39b2efd32"; break;
            case 'X' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fx.png?alt=media&token=cdb17dd7-20fc-457c-b5b4-4cf39b2efd32"; break;
            case 'y' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fy.png?alt=media&token=69d1aca1-cca8-49d8-84d3-a570e2d723b4"; break;
            case 'Y' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fy.png?alt=media&token=69d1aca1-cca8-49d8-84d3-a570e2d723b4"; break;
            case 'z' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fz.png?alt=media&token=2dd63f58-435c-440f-80b4-74ee432e0fb8"; break;
            case 'Z' : url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fz.png?alt=media&token=2dd63f58-435c-440f-80b4-74ee432e0fb8"; break;
            default:   url = "https://firebasestorage.googleapis.com/v0/b/chalange-b84f3.appspot.com/o/Leaters%2Fz.png?alt=media&token=2dd63f58-435c-440f-80b4-74ee432e0fb8"; break;
        }
        Glide.with(getApplicationContext())
                .load(url)
                .into(image);
    }

//    public int getIconId(String name)
//    {
//        switch (name.charAt(0)) {
//            case 'a' : return R.drawable.a;
//            case 'A' : return R.drawable.a;
//            case 'b' : return R.drawable.b;
//            case 'B' : return R.drawable.b;
//            case 'c' : return R.drawable.c;
//            case 'C' : return R.drawable.c;
//            case 'd' : return R.drawable.d;
//            case 'D' : return R.drawable.d;
//            case 'e' : return R.drawable.e;
//            case 'E' : return R.drawable.e;
//            case 'f' : return R.drawable.f;
//            case 'F' : return R.drawable.f;
//            case 'g' : return R.drawable.g;
//            case 'G' : return R.drawable.g;
//            case 'h' : return R.drawable.h;
//            case 'H' : return R.drawable.h;
//            case 'i' : return R.drawable.i;
//            case 'I' : return R.drawable.i;
//            case 'j' : return R.drawable.j;
//            case 'J' : return R.drawable.j;
//            case 'k' : return R.drawable.k;
//            case 'K' : return R.drawable.k;
//            case 'l' : return R.drawable.l;
//            case 'L' : return R.drawable.l;
//            case 'm' : return R.drawable.m;
//            case 'M' : return R.drawable.m;
//            case 'n' : return R.drawable.n;
//            case 'N' : return R.drawable.n;
//            case 'o' : return R.drawable.o;
//            case 'O' : return R.drawable.o;
//            case 'p' : return R.drawable.p;
//            case 'P' : return R.drawable.p;
//            case 'q' : return R.drawable.q;
//            case 'Q' : return R.drawable.q;
//            case 'r' : return R.drawable.r;
//            case 'R' : return R.drawable.r;
//            case 's' : return R.drawable.s;
//            case 'S' : return R.drawable.s;
//            case 't' : return R.drawable.t;
//            case 'T' : return R.drawable.t;
//            case 'u' : return R.drawable.u;
//            case 'U' : return R.drawable.u;
//            case 'v' : return R.drawable.v;
//            case 'V' : return R.drawable.v;
//            case 'w' : return R.drawable.w;
//            case 'W' : return R.drawable.w;
//            case 'x' : return R.drawable.x;
//            case 'X' : return R.drawable.x;
//            case 'y' : return R.drawable.y;
//            case 'Y' : return R.drawable.y;
//            case 'z' : return R.drawable.z;
//            case 'Z' : return R.drawable.z;
//            default:   return R.drawable.ic_menu_slideshow;
//        }
//    }

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
        super.onCreateOptionsMenu(menu);
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
