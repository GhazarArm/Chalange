package com.example.ghazar.chalange.FirstPage;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ghazar.chalange.Activitys.MainActivity;
import com.example.ghazar.chalange.Objects.Account;
import com.example.ghazar.chalange.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener{

    public static boolean isFirst = false;

    public final int REQUEST_CODE_OF_SINE_IN = 1;
    public final int REQUEST_CODE_OF_SINE_UP_VIA_PHONE = 2;
    public final int REQUEST_CODE_OF_SINE_IN_VIA_EMAIL = 3;

    LoginButton m_loginButton;
    CallbackManager m_callBackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        isFirst = true;

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

                                MainActivity.m_mainActivity.getCurentAccountDatabas();
                                MainActivity.m_mainActivity.initCurentAccountData(id);
                                MainActivity.m_mainActivity.m_curentAccount = new Account(first_name,
                                                last_name,
                                                age,
                                                (gender.startsWith("M") || gender.startsWith("m")) ? true : false,
                                                id);
                                MainActivity.m_mainActivity.initNavigationHeader(first_name, last_name);

                                finish();
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
        }
    }
}
