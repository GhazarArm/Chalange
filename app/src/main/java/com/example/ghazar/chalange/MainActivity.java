package com.example.ghazar.chalange;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghazar.chalange.Objects.Account;
import com.example.ghazar.chalange.Objects.AccountDB;
import com.example.ghazar.chalange.Objects.Events;
import com.example.ghazar.chalange.Objects.Frends;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final String PREFS_NAME = "MyPrefFile";
    public final String IS_SINE_IN_KEY = "is_sine_in";
    public static String MY_ACCOUNT_DATABASE_NAME = "m_account";
    public static String MY_ACCOUNT_FRENDS_DATABASE_NAME = "m_frends";
    public static String MY_ACCOUNT_EVENTS_DATABASE_NAME = "m_events";
    public static String MY_ACCOUNT_DATABASE_KEY;


    public final String NAME = "_name";
    public final String LAST_NAME = "_lastName";
    public final String AGE = "_age";
    public final String EMAIL = "_email";
    public final String PHONE = "_phone";
    public final String PASSWORD = "_password";
    public final String GENDER = "_gender";

    public final int REQUEST_CODE_OF_FIRST_ACTIVITY = 1;
    public final int REQUEST_CODE_OF_FREND_REQUEST_ACTIVITY = 2;
    public final int REQUEST_CODE_OF_GUEST_ACTIVITY = 3;

    public static DatabaseReference m_db = FirebaseDatabase.getInstance().getReference();
    public static DatabaseReference m_accountsDB = m_db.child("Accounts");
    public static DatabaseReference m_accountInfoDB = null;
    public static DatabaseReference m_accountFrendsDB = null;
    public static DatabaseReference m_accountEventsDB = null;
    public static DataSnapshot m_AccountInfoDataSnapshot = null;
    public static DataSnapshot m_AccountFrendsDataSnapshot = null;
    public static DataSnapshot m_AccountEventsDataSnapshot = null;
    public static DataSnapshot m_AccountDataSnapshot = null;
    private long m_accountCount;


    public static MainActivity m_mainActivity;
    public Account m_curentAccount;

    private Tag1 m_tab1;
    private Tag2 m_tab2;
    private MyProfile m_myProfileTab;

    private ImageView m_navigationHeadericon;
    private TextView m_navigationHeaderTitle;
    private TextView m_navigationHeaderDesc;
    private BadgeDrawerArrowDrawable badgeDrawable;

    MenuItem m_searchItem;
    SearchDialog m_searchDialog;

    public boolean m_isFirst = true;

    public Vector<String> m_frends;

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
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerStateChanged(int newState) {
                badgeDrawable.setEvents(0);
                invalidateOptionsMenu();
            }
        };
        badgeDrawable = new BadgeDrawerArrowDrawable(getSupportActionBar().getThemedContext());
        toggle.setDrawerArrowDrawable(badgeDrawable);
        badgeDrawable.setEnabled(false);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        m_navigationHeaderTitle = (TextView) hView.findViewById(R.id.account_name_text_view);
        m_navigationHeaderDesc = (TextView) hView.findViewById(R.id.account_info_text_view);
        m_navigationHeadericon = (ImageView) hView.findViewById(R.id.account_image_view);

        ViewPager viewPages = (ViewPager) findViewById(R.id.vp_pages);
        PagerAdapter pagerAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPages.setAdapter(pagerAdapter);
        viewPages.setCurrentItem(1);

        TabLayout tbl_pages = (TabLayout) findViewById(R.id.tbl_pages);
        tbl_pages.setupWithViewPager(viewPages);

        viewPages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    m_searchItem.setVisible(true);
                } else {
                    m_searchItem.setVisible(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        m_accountsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                m_AccountDataSnapshot = dataSnapshot;
                SharedPreferences pref = m_mainActivity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                if(!pref.getBoolean(IS_SINE_IN_KEY, false))
                {
                    startFirstActivity();
                }
                else if(m_isFirst)
                {
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if(!postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(EMAIL).getValue(String.class).equals(pref.getString(EMAIL, "null")) ||
                           !postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(PHONE).getValue(String.class).equals(pref.getString(PHONE, "null")))
                                continue;

                        m_isFirst = false;
                        m_accountInfoDB = m_accountsDB.child(postSnapshot.getKey()).child(MY_ACCOUNT_DATABASE_NAME);
                        m_accountFrendsDB = m_accountsDB.child(postSnapshot.getKey()).child(MY_ACCOUNT_FRENDS_DATABASE_NAME);
                        m_accountEventsDB = m_accountsDB.child(postSnapshot.getKey()).child(MY_ACCOUNT_EVENTS_DATABASE_NAME);
                        initDatabasesChangeEvent();
                        m_curentAccount = new Account();
                        m_curentAccount.set_name(pref.getString(NAME, "-"));
                        m_curentAccount.set_lastName(pref.getString(LAST_NAME, "-"));
                        m_curentAccount.set_email(pref.getString(EMAIL, "-"));
                        m_curentAccount.set_age(pref.getInt(AGE, 18));
                        m_curentAccount.set_gender(pref.getBoolean(GENDER, true));
                        m_curentAccount.set_phone(pref.getString(PHONE, "-"));
                        m_curentAccount.set_password(pref.getString(PASSWORD, "-"));
                        m_navigationHeaderTitle.setText(m_curentAccount.get_name() + "  " + m_curentAccount.get_lastName());
                        m_navigationHeaderDesc.setText((m_curentAccount.get_phone().equals("-")) ? "Email: " + m_curentAccount.get_email() : "Phone: " + m_curentAccount.get_phone() + "  Age: " + m_curentAccount.get_age());
                        m_navigationHeadericon.setImageResource(m_mainActivity.getIconId(m_curentAccount.get_name()));

                        m_myProfileTab.InitButtonsText();
                        getAllAccounts();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setTab1(Tag1 tab1) {
        m_tab1 = tab1;
    }

    public void setTab2(Tag2 tab2) {
        m_tab2 = tab2;
    }

    public void setTabMyProfile(MyProfile myProfile) {
        m_myProfileTab = myProfile;
    }

    public boolean isEmailExist(String email) {
        for (DataSnapshot postSnapshot : m_AccountDataSnapshot.getChildren()) {
            String s = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(EMAIL).getValue(String.class);
            if (email.equals(postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(EMAIL).getValue(String.class)))
                return true;
        }
        return false;
    }

    public boolean isPhoneExist(String phone) {
        for (DataSnapshot postSnapshot : m_AccountDataSnapshot.getChildren()) {
            String s = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(PHONE).getValue(String.class);
            if (phone.equals(postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(PHONE).getValue(String.class)))
                return true;
        }
        return false;
    }

    public boolean isPasswordTrue(String emailOrPhone, String password) {
        for (DataSnapshot postSnapshot : m_AccountDataSnapshot.getChildren()) {
            if (emailOrPhone.equals(postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(PHONE).getValue(String.class)) || emailOrPhone.equals(postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(EMAIL).getValue(String.class))) {
                String a = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(PASSWORD).getValue(String.class);
                if (password.equals(postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(PASSWORD).getValue(String.class)))
                    return true;
                else
                    return false;
            }
        }
        return false;
    }

    public boolean isPasswordTrue(String password) {
        if (m_curentAccount.get_password().equals(password))
            return true;

        return false;
    }

//    public Vector<Account> getAllFrends(String email)
//    {
//
//    }

    public Vector<Account> SearchAccount(String name, int maxAge, int minAge) {
        m_searchDialog.cancel();
        Vector<Account> accounts = new Vector<Account>();
        for (DataSnapshot postSnapshot : m_AccountDataSnapshot.getChildren()) {
            String _name = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(NAME).getValue(String.class);
            if (_name.contains(name)) {
                int age = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(AGE).getValue(int.class);
                if (age >= minAge && age <= maxAge) {
                    String lastName = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(LAST_NAME).getValue(String.class);
                    String email = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(EMAIL).getValue(String.class);
                    String phone = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(PHONE).getValue(String.class);
                    boolean gender = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(GENDER).getValue(boolean.class);
                    Account acc = new Account(_name, lastName, age, email, phone, " ", gender);
                    accounts.add(acc);
                }
            }
        }
        return accounts;
    }

    public void getAllAccounts() {
        for (DataSnapshot postSnapshot : m_AccountDataSnapshot.getChildren()) {
            String name = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(NAME).getValue(String.class);
            String lastName = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(LAST_NAME).getValue(String.class);
            int age = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(AGE).getValue(int.class);
            String email = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(EMAIL).getValue(String.class);
            String phone = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(PHONE).getValue(String.class);
            m_tab1.AddItem(getIconId(name), name + "  " + lastName, Integer.toString(age));
        }
    }

    public void initCurentAccountData(String name, String lastName, int age, String email, String phone, String password, boolean gender, String my_account_database_name) {
        SharedPreferences pref = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.putBoolean(IS_SINE_IN_KEY, true);
        editor.putString(NAME, name);
        editor.putString(LAST_NAME, lastName).apply();
        editor.putInt(AGE, age).apply();
        editor.putString(EMAIL, email).apply();
        editor.putString(PHONE, phone).apply();
        editor.putString(PASSWORD, password).apply();
        editor.putBoolean(GENDER, gender);
        editor.putString(MY_ACCOUNT_DATABASE_KEY, my_account_database_name);
        editor.commit();
    }

    public void startFirstActivity() {
        Intent intent = new Intent(this, FirstActivity.class);
        startActivityForResult(intent, 1);
    }

    public Account getAccount(String emailOrPassword) {
        for (DataSnapshot postSnapshot : m_AccountDataSnapshot.getChildren()) {
            if (emailOrPassword.equals(postSnapshot.child("m_account").child(PHONE).getValue(String.class)) || emailOrPassword.equals(postSnapshot.child("m_account").child(EMAIL).getValue(String.class))) {
                MY_ACCOUNT_DATABASE_KEY = postSnapshot.getKey();
                String name = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(NAME).getValue(String.class);
                String lastName = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(LAST_NAME).getValue(String.class);
                int age = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(AGE).getValue(int.class);
                String phone = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(PHONE).getValue(String.class);
                String password = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(PASSWORD).getValue(String.class);
                boolean gender = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(GENDER).getValue(boolean.class);
                return new Account(name, lastName, age, emailOrPassword, phone, password, gender);
            }
        }
        return null;
    }

    public void initDatabasesChangeEvent()
    {
        if(m_accountInfoDB != null)
        {
            m_accountInfoDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    m_AccountInfoDataSnapshot = dataSnapshot;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        if(m_accountFrendsDB != null)
        {
            m_accountFrendsDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    m_AccountFrendsDataSnapshot = dataSnapshot;
                    m_frends = new Vector<String>();
                    for(DataSnapshot postSnapshot : dataSnapshot.child(Frends.FRENDS_VECTOR_KEY).getChildren())
                    {
                        String frendId = postSnapshot.getValue(String.class);
                        m_frends.add(frendId);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        if(m_accountEventsDB != null)
        {
            m_accountEventsDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    m_AccountEventsDataSnapshot = dataSnapshot;
                    if(m_AccountEventsDataSnapshot.getChildrenCount() > 1)
                        badgeDrawable.setEvents((int)m_AccountEventsDataSnapshot.getChildrenCount() - 1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    public void AddAccountViaEmail(String name, String lastName, int age, String email, String password, boolean gender)
    {
        Account acc = new Account(name, lastName, age, email, "-", password, gender);
        Events events = new Events(Events.ACCOUNT_CREATED_EVENT_KEY, Events.ACCOUNT_CREATED_EVENT_TEXT);
        Frends frend = new Frends(email);
        frend.AddFrend("terxachatryan20@gamil.com");
        AccountDB accountDB = new AccountDB(acc, events, frend);
        m_accountsDB.push().setValue(accountDB);
    }

    public void AddAccountViaPhone(String name, String lastName, int age, String phone, String password, boolean gender)
    {
        Account acc = new Account(name, lastName, age, "-", phone,password, gender);
        Events events = new Events(Events.ACCOUNT_CREATED_EVENT_KEY, Events.ACCOUNT_CREATED_EVENT_TEXT);
        Frends frend = new Frends(phone);
        frend.AddFrend("terxachatryan20@gamil.com");
        AccountDB accountDB = new AccountDB(acc, events, frend);
        m_accountsDB.push().setValue(accountDB);
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


    //<<<<<<<<<<<<<<<<<<<<<<< Change Parameters <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void changeName(String name)
    {
        if(m_accountInfoDB != null)
        {
            m_accountInfoDB.child(NAME).setValue(name);
        }
    }

    public void changeLastName(String lastname)
    {
        if(m_accountInfoDB != null)
        {
            m_accountInfoDB.child(LAST_NAME).setValue(lastname);
        }
    }

    public void changeAge(int age)
    {
        if(m_accountInfoDB != null)
        {
            m_accountInfoDB.child(AGE).setValue(age);
        }
    }

    public void changeGender()
    {
        if(m_accountInfoDB != null)
        {
            m_accountInfoDB.child(GENDER).setValue(!m_curentAccount.get_gender());
        }
    }

    public void changePassword(String curentPassword, String newPassword)
    {
        if(m_accountInfoDB != null)
        {
            m_accountInfoDB.child(PASSWORD).setValue(newPassword);
        }
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    public void sendFrendRequest(String idToAccount)
    {
        for (DataSnapshot postSnapshot: m_AccountDataSnapshot.getChildren())
        {
            String email = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(EMAIL).getValue(String.class);
            if(email.equals(idToAccount))
            {
                DatabaseReference toAccountEventsDatabase = m_accountsDB.child(postSnapshot.getKey()).child(MY_ACCOUNT_EVENTS_DATABASE_NAME);
//                toAccountEventsDatabase.child()
                Events event = new Events(Events.FREND_REQUEST_EVENT_KEY, m_curentAccount.get_email());
                toAccountEventsDatabase.push().setValue(event);
                break;
            }
        }
    }

    public void addFrend(String id)
    {
        m_frends.add(id);
        m_accountFrendsDB.child(Frends.FRENDS_VECTOR_KEY).setValue(m_frends);
    }

    public void sendGuestRequest(String idToAccount)
    {
        for (DataSnapshot postSnapshot: m_AccountDataSnapshot.getChildren())
        {
            String email = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(EMAIL).getValue(String.class);
            if(email.equals(idToAccount))
            {
                DatabaseReference toAccountEventsDatabase = m_accountsDB.child(postSnapshot.getKey()).child(MY_ACCOUNT_EVENTS_DATABASE_NAME);
                Events event = new Events(Events.ACCOUNT_GUEST_KEY, m_curentAccount.get_email());
                toAccountEventsDatabase.push().setValue(event);
                break;
            }
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

        //noinspection SimplifiableIfStatement
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
        if((requestCode == REQUEST_CODE_OF_FIRST_ACTIVITY))
        {
            if(resultCode == RESULT_CANCELED) {
                startFirstActivity();
            }
            else if(resultCode == RESULT_OK)
            {
                String email = data.getStringExtra(EMAIL);
                m_curentAccount = getAccount(email);
                m_accountInfoDB = m_accountsDB.child(MY_ACCOUNT_DATABASE_KEY).child(MY_ACCOUNT_DATABASE_NAME);
                m_accountEventsDB = m_accountsDB.child(MY_ACCOUNT_DATABASE_KEY).child(MY_ACCOUNT_EVENTS_DATABASE_NAME);
                m_accountFrendsDB = m_accountsDB.child(MY_ACCOUNT_DATABASE_KEY).child(MY_ACCOUNT_FRENDS_DATABASE_NAME);
                initDatabasesChangeEvent();

                initCurentAccountData(m_curentAccount.get_name(), m_curentAccount.get_lastName(),
                        m_curentAccount.get_age(), m_curentAccount.get_email(),
                        m_curentAccount.get_phone(), m_curentAccount.get_password(),
                        m_curentAccount.get_gender(), MY_ACCOUNT_DATABASE_NAME);

                m_navigationHeaderTitle.setText(m_curentAccount.get_name() + "  " + m_curentAccount.get_lastName());
                m_navigationHeaderDesc.setText((m_curentAccount.get_phone().equals("-")) ? "Email: " + m_curentAccount.get_email() : "Phone: " + m_curentAccount.get_phone() + "  Age: " + m_curentAccount.get_age());
                m_navigationHeadericon.setImageResource(m_mainActivity.getIconId(m_curentAccount.get_name()));

                m_myProfileTab.InitButtonsText();

                getAllAccounts();
            }
        }
    }
}
