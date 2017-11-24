package com.example.ghazar.chalange.Tabs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.example.ghazar.chalange.Activitys.MainActivity;
import com.example.ghazar.chalange.R;

/**
 * Created by ghazar on 11/8/17.
 */

public class MyProfile extends Fragment implements View.OnClickListener {
    private View m_view;

    public final int REQUEST_CODE_OF_CHAT_ACTIVITY = 1;

    public static MyProfile m_myProfile;

    private Button nameButton;
    private Button lastNameButton;
    private Button ageButton;
    private Button genderButton;
    private Button teamButton;
    private ImageView myProfileImageView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        m_myProfile = this;
        View m_view = inflater.inflate(R.layout.my_profile, container, false);

        nameButton = (Button) m_view.findViewById(R.id.my_profile_name_button);
        lastNameButton = (Button) m_view.findViewById(R.id.my_profile_last_name_button);
        ageButton = (Button) m_view.findViewById(R.id.my_profile_age_button);
        genderButton = (Button) m_view.findViewById(R.id.my_profile_gender_button);
        teamButton = (Button) m_view.findViewById(R.id.my_profile_team_button);
        myProfileImageView = (ImageView) m_view.findViewById(R.id.my_profile_image_view);

        InitButtonsText();

        try {
            nameButton.setOnClickListener(this);
            lastNameButton.setOnClickListener(this);
            ageButton.setOnClickListener(this);
            genderButton.setOnClickListener(this);
            teamButton.setOnClickListener(this);
        }catch(Exception ex) {
            Log.e("My Error", ex.toString());
        }

        return m_view;
    }

    public void InitButtonsText()
    {
        try {
            nameButton.setText(MainActivity.m_mainActivity.m_curentAccount.get_name());
            lastNameButton.setText(MainActivity.m_mainActivity.m_curentAccount.get_lastName());
            ageButton.setText(Integer.toString(MainActivity.m_mainActivity.m_curentAccount.get_age()));
            genderButton.setText((MainActivity.m_mainActivity.m_curentAccount.get_gender() ? "Male" : "Famel"));
            teamButton.setText("Red");
            myProfileImageView.setImageResource(MainActivity.m_mainActivity.getIconId(MainActivity.m_mainActivity.m_curentAccount.get_name()));
        }catch (Exception ex) {
            Log.e("My Error", ex.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            String result = data.getStringExtra("accountName");
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.my_profile_name_button) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.text_change);
            dialog.setTitle("Change Name");
            dialog.show();
            Button okButton = (Button) dialog.findViewById(R.id.text_change_ok_button);
            Button cancelButton = (Button) dialog.findViewById(R.id.text_change_cancel_button);
            final EditText editText = (EditText) dialog.findViewById(R.id.text_change_edit_text);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    MainActivity.m_mainActivity.changeName(editText.getText().toString());
                    nameButton.setText(editText.getText().toString());
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else if (v.getId() == R.id.my_profile_last_name_button) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.text_change);
            dialog.setTitle("Change Last Name");
            dialog.show();
            Button okButton = (Button) dialog.findViewById(R.id.text_change_ok_button);
            Button cancelButton = (Button) dialog.findViewById(R.id.text_change_cancel_button);
            final EditText editText = (EditText) dialog.findViewById(R.id.text_change_edit_text);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    MainActivity.m_mainActivity.changeLastName(editText.getText().toString());
                    lastNameButton.setText(editText.getText().toString());
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else if (v.getId() == R.id.my_profile_age_button) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.text_change);
            dialog.setTitle("Change Age");
            dialog.show();

            Button okButton = (Button) dialog.findViewById(R.id.text_change_ok_button);
            Button cancelButton = (Button) dialog.findViewById(R.id.text_change_cancel_button);

            final EditText editText = (EditText) dialog.findViewById(R.id.text_change_edit_text);
            editText.setVisibility(View.INVISIBLE);
            LinearLayout age_change_layout = (LinearLayout)dialog.findViewById(R.id.age_change_layout);
            age_change_layout.setVisibility(View.VISIBLE);
            final SeekBar ageSeekBar = (SeekBar)dialog.findViewById(R.id.age_seek_bar);
            final EditText ageEditText = (EditText)dialog.findViewById(R.id.age_edit_text);
            ageEditText.setEnabled(false);

            ageSeekBar.setProgress(MainActivity.m_mainActivity.m_curentAccount.get_age());
            ageEditText.setText(Integer.toString(ageSeekBar.getProgress()));

            ageSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    ageEditText.setText(Integer.toString(ageSeekBar.getProgress()));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    MainActivity.m_mainActivity.changeAge(ageSeekBar.getProgress());
                    ageButton.setText(Integer.toString(ageSeekBar.getProgress()));
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        } else if (v.getId() == R.id.my_profile_gender_button) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            MainActivity.m_mainActivity.changeGender();
                            genderButton.setText(MainActivity.m_mainActivity.m_curentAccount.get_gender() ? "Male" : "Famel");
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure you want to change your gender?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }
}
