package com.example.ghazar.chalange.FirstPage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ghazar.chalange.Activitys.MainActivity;
import com.example.ghazar.chalange.Objects.Account;
import com.example.ghazar.chalange.Objects.Database;
import com.example.ghazar.chalange.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener , GoogleApiClient.OnConnectionFailedListener{

    public static boolean isFirst = false;

    public final String NAME = "_name";
    public final String LAST_NAME = "_lastName";
    public final String AGE = "_age";
    public final String ID = "_id";
    public final String GENDER = "_gender";

    public final int REQUEST_CODE_OF_SINE_IN = 1;
    public final int REQUEST_CODE_OF_SINE_UP_VIA_PHONE = 2;
    public final int REQUEST_CODE_OF_SINE_IN_VIA_EMAIL = 3;
    public final int REQUEST_CODE_OF_GOOGLE_SINE_IN = 4;

    LoginButton m_loginButton;
    CallbackManager m_callBackManager;

    GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mGoogleApiClient;

    private String curentId;

    public static Database m_database;

    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_first);

        m_database = new Database();

        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);

        //////////////////////////////////////////////
        //////       SIGN IN VIA GMAIL          //////
        //////////////////////////////////////////////
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton signInButton = (SignInButton)findViewById(R.id.sine_up_by_google);
        signInButton.setOnClickListener(this);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




        //////////////////////////////////////////////
        //////       SIGN IN VIA FACEBOOK       //////
        //////////////////////////////////////////////

        m_loginButton = (LoginButton)findViewById(R.id.sine_up_by_facebook);
        m_callBackManager = CallbackManager.Factory.create();
        m_loginButton.setReadPermissions("public_profile");
        m_loginButton.setReadPermissions("email");
        m_loginButton.setReadPermissions("user_birthday");
        m_loginButton.registerCallback(m_callBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                String id = "-";
                                String gender = "-";
                                String first_name = "-";
                                String last_name = "-";
                                int age = 0;
                                try {
                                    id = object.getString("id") + Account.FACEBOOK;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    gender = object.getString("gender");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    first_name = object.getString("first_name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    last_name = object.getString("last_name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    age = getAge(object.getString("birthday"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                curentId = id;
                                m_database.setID(curentId);
                                showProgress(true);
                                Thread thread = new Thread(new MyRunnableForFacebookSignIn(first_name,
                                        last_name,
                                        age,
                                        ((gender.startsWith("M") || gender.startsWith("m")) ? true : false),
                                        id), "thread");
                                thread.start();
                                try{
                                    thread.join(2000);
                                }catch (InterruptedException ex){
                                    Log.e("MY ERROR", ex.toString());
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields",  "id, gender, birthday, first_name, last_name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(FirstActivity.this, "Facebook connection canceled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        Button sinein = (Button) findViewById(R.id.is_sine_in);
        sinein.setOnClickListener(this);
        Button sineupemail = (Button) findViewById(R.id.sine_up_by_email);
        sineupemail.setOnClickListener(this);
        Button sineinphone = (Button) findViewById(R.id.sine_up_by_phone);
        sineinphone.setOnClickListener(this);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    }

    public boolean isLoggedInViaFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null)
            curentId = accessToken.getUserId() + Account.FACEBOOK;
        return accessToken != null;
    }

    public boolean isLoggedInViaGoogle() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null)
            curentId = account.getId() + Account.GOOGLE;
        return account != null;
    }

    public boolean isLogedIn(){
        return (isLoggedInViaFacebook() || isLoggedInViaGoogle());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isLogedIn()) {
            showProgress(true);
            Thread thread = new Thread(new MyRunnableForSignIn(), "thread");
            thread.start();
            try{
                thread.join(2000);
            }catch (InterruptedException ex){
                Log.e("MY ERROR", ex.toString());
            }
        }
    }

    public void GoMainActivity(String first_name, String last_name, int age, boolean gender, String id) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(NAME, first_name);
        intent.putExtra(LAST_NAME, last_name);
        intent.putExtra(AGE, age);
        intent.putExtra(GENDER, gender);
        intent.putExtra(ID, id);
        startActivityForResult(intent, 0);
    }

    private int getAge(String birthDay){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        try {
            date = dateFormat.parse(birthDay);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        int year = date.getYear() + 1900;
        int month = date.getMonth();
        int day = date.getDay();

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);

        return ageInt;
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, REQUEST_CODE_OF_GOOGLE_SINE_IN);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.sine_up_by_email)
        {
            Intent intentSineUp = new Intent(this, SineUpActivity.class);
            intentSineUp.putExtra("RequestCode", REQUEST_CODE_OF_SINE_IN_VIA_EMAIL);
            startActivityForResult(intentSineUp, REQUEST_CODE_OF_SINE_IN_VIA_EMAIL);
        }
        else if(v.getId() == R.id.sine_up_by_phone)
        {
            Intent intentSineUp = new Intent(this, SineUpActivity.class);
            intentSineUp.putExtra("RequestCode", REQUEST_CODE_OF_SINE_UP_VIA_PHONE);
            startActivityForResult(intentSineUp, REQUEST_CODE_OF_SINE_UP_VIA_PHONE);
        }
        else if(v.getId() == R.id.is_sine_in)
        {
            Intent intentSineIn = new Intent(this, LoginActivity.class);
            startActivityForResult(intentSineIn, REQUEST_CODE_OF_SINE_IN);
        }else if(v.getId() == R.id.sine_up_by_google) {
            signIn();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        m_callBackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_OF_SINE_IN)
        {
            if(resultCode == RESULT_OK)
            {
                setResult(RESULT_OK, data);
                finish();
            }
        }else if (requestCode == REQUEST_CODE_OF_GOOGLE_SINE_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            GoogleSignInResult resoult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(resoult);
        }
    }


    private void handleSignInResult(GoogleSignInResult resoult) {
        if(resoult.isSuccess()) {
            GoogleSignInAccount account = resoult.getSignInAccount();
            // Signed in successfully, show authenticated UI.
            String id = "-";
            String first_name = "-";
            String last_name = "-";
            int age = 0;
//            Person person  = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
//            boolean gender = (person.getGender() == Person.Gender.FEMALE )? false : true;
//            first_name = person.getName().getGivenName();
//            last_name = person.getName().getFamilyName();
//            id = person.getId() + "Google";
//            age = getAge(person.getBirthday());
            first_name = account.getGivenName();
            last_name = account.getFamilyName();
            id = account.getId() + "Google";

            curentId = id;
            m_database.setID(curentId);
            showProgress(true);
            Thread thread = new Thread(new MyRunnableForGoogleSignIn(first_name, last_name, age, true, id), "thread");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    class MyRunnableForSignIn implements Runnable{
        @Override
        public void run() {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true){
                try{
                    m_database.setID(curentId);
                    break;
                }catch (NullPointerException ex){
                    Log.e("MY ERROR", ex.toString());
                }
            }
            while (true) {
                try {
                    Account acc = null;
                    if (m_database.isAccountExist(curentId)) {
                        acc = m_database.getAccount(curentId);
                        GoMainActivity(acc.get_name(),
                                acc.get_lastName(),
                                acc.get_age(),
                                acc.get_gender(),
                                curentId);
                    } else {
                        Log.e("MY ERROR", "Account not exist!!!!!!!");
                    }
                    break;
                } catch (NullPointerException ex) {
                    Log.e("MY ERROR", ex.toString());
                }
            }
        }
    }

    class MyRunnableForGoogleSignIn implements Runnable{
        private String m_id = "-";
        private String m_firstName = "-";
        private String m_lastName = "-";
        private int m_age = 0;
        private boolean m_gender = true;

        public MyRunnableForGoogleSignIn(String name, String lastName, int age, boolean gender, String id){
            m_id = id;
            m_firstName = name;
            m_lastName = lastName;
            m_age = age;
            m_gender = gender;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true){
                try{
                    m_database.setID(curentId);
                    break;
                }catch (NullPointerException ex){
                    Log.e("MY ERROR", ex.toString());
                }
            }
            while(true){
                try{
                    Account acc = null;
                    if(m_database.isAccountExist(curentId)) {
                        acc = m_database.getAccount(curentId);
                        GoMainActivity(acc.get_name(),
                                acc.get_lastName(),
                                acc.get_age(),
                                acc.get_gender(),
                                curentId);
                    }
                    else{
                        m_database.AddAccount(m_firstName, m_lastName, m_age, true, m_id);
                        GoMainActivity(m_firstName, m_lastName, m_age, true, m_id);
                    }

                    break;
                }catch (NullPointerException ex){
                    Log.e("MY ERROR", ex.toString());
                }
            }
        }
    }

    class MyRunnableForFacebookSignIn implements Runnable{
        private String m_id = "-";
        private String m_firstName = "-";
        private String m_lastName = "-";
        private int m_age = 0;
        private boolean m_gender = true;

        public MyRunnableForFacebookSignIn(String name, String lastName, int age, boolean gender, String id){
            m_id = id;
            m_firstName = name;
            m_lastName = lastName;
            m_age = age;
            m_gender = gender;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true){
                try{
                    m_database.setID(curentId);
                    break;
                }catch (NullPointerException ex){
                    Log.e("MY ERROR", ex.toString());
                }
            }
            while(true) {
                try {
                    Account acc = null;
                    if (m_database.isAccountExist(curentId)) {
                        acc = m_database.getAccount(curentId);
                        GoMainActivity(acc.get_name(),
                                acc.get_lastName(),
                                acc.get_age(),
                                acc.get_gender(),
                                curentId);
                    } else {
                        m_database.AddAccount(m_firstName, m_lastName, m_age, m_gender, m_id);
                        GoMainActivity(m_firstName, m_lastName, m_age, m_gender, m_id);
                    }
                    break;
                } catch (NullPointerException ex) {
                    Log.e("MY ERROR", ex.toString());
                }
            }
        }
    }
}
