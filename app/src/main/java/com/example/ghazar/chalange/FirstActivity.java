package com.example.ghazar.chalange;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener{

    public final int REQUEST_CODE_OF_SINE_IN = 1;
    public final int REQUEST_CODE_OF_SINE_UP_VIA_PHONE = 2;
    public final int REQUEST_CODE_OF_SINE_IN_VIA_EMAIL = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Button sinein = (Button) findViewById(R.id.is_sine_in);
        sinein.setOnClickListener(this);
        Button sineupemail = (Button) findViewById(R.id.sine_up_by_email);
        sineupemail.setOnClickListener(this);
        Button sineinphone = (Button) findViewById(R.id.sine_up_by_phone);
        sineinphone.setOnClickListener(this);

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
