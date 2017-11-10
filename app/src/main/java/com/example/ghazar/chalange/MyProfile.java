package com.example.ghazar.chalange;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;

import com.example.ghazar.chalange.Objects.Account;

import java.util.Vector;

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
    private Button emailButton;
    private Button phoneButton;
    private Button genderButton;
    private Button teamButton;
    private Button changePasswordButton;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        m_myProfile = this;
        View m_view = inflater.inflate(R.layout.my_profile, container, false);

        nameButton = (Button) m_view.findViewById(R.id.my_profile_name_button);
        lastNameButton = (Button) m_view.findViewById(R.id.my_profile_last_name_button);
        ageButton = (Button) m_view.findViewById(R.id.my_profile_age_button);
        emailButton = (Button) m_view.findViewById(R.id.my_profile_email_button);
        phoneButton = (Button) m_view.findViewById(R.id.my_profile_phone_button);
        genderButton = (Button) m_view.findViewById(R.id.my_profile_gender_button);
        teamButton = (Button) m_view.findViewById(R.id.my_profile_team_button);
        changePasswordButton = (Button) m_view.findViewById(R.id.my_profile_change_password_button);


        try {
            nameButton.setOnClickListener(this);
            lastNameButton.setOnClickListener(this);
            ageButton.setOnClickListener(this);
            emailButton.setOnClickListener(this);
            phoneButton.setOnClickListener(this);
            genderButton.setOnClickListener(this);
            teamButton.setOnClickListener(this);
            changePasswordButton.setOnClickListener(this);
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
            emailButton.setText(MainActivity.m_mainActivity.m_curentAccount.get_email());
            phoneButton.setText(MainActivity.m_mainActivity.m_curentAccount.get_phone());
            genderButton.setText((MainActivity.m_mainActivity.m_curentAccount.get_gender() ? "Male" : "Famel"));
            teamButton.setText("Red");
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
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure you want to change your gender?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        } else if (v.getId() == R.id.my_profile_change_password_button) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.password_change_layout);
            dialog.setTitle("Change Password");
            dialog.show();
            Button okButton = (Button) dialog.findViewById(R.id.password_change_ok_button);
            Button cancelButton = (Button) dialog.findViewById(R.id.password_change_cancel_button);
            final EditText editText = (EditText) dialog.findViewById(R.id.password_change_curent_password_edit_text);
            final EditText newPassword = (EditText) dialog.findViewById(R.id.password_change_new_password_edit_text);
            final EditText confirmNewPassword = (EditText) dialog.findViewById(R.id.password_change_new_password_confirm_edit_text);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (confirmNewPassword.getText().toString().equals(newPassword.getText().toString())) {
                        if (MainActivity.m_mainActivity.isPasswordTrue(editText.getText().toString())) {
                            dialog.dismiss();
                            MainActivity.m_mainActivity.changePassword(editText.getText().toString(), newPassword.getText().toString());
                        }
                        else
                        {
                            editText.setError(getString(R.string.error_incorrect_password));
                        }
                    }
                    else
                    {
                        confirmNewPassword.setError(getString(R.string.error_incorrect_password));
                    }
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }
}