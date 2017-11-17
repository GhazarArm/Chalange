package com.example.ghazar.chalange.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.ghazar.chalange.R;
import com.example.ghazar.chalange.Tabs.Tag2;

/**
 * Created by ghazar on 11/9/17.
 */

public class SearchDialog extends Dialog implements View.OnClickListener {

    private EditText     m_nameEditText;
    private EditText     m_ageMaxEditText;
    private EditText     m_ageMinEditText;
    private SeekBar      m_ageMaxSeekBar;
    private SeekBar      m_ageMinSeekBar;

    private Context m_context;

    public SearchDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.search_layout);

        Button SearchButton = (Button)findViewById(R.id.search_button_of_dialog);
        SearchButton.setOnClickListener(this);

        m_nameEditText = (EditText)findViewById(R.id.dialog_name_search);
        m_ageMinEditText = (EditText)findViewById(R.id.min_edit_text_age);
        m_ageMaxEditText = (EditText)findViewById(R.id.max_edit_text_age);
        m_ageMaxSeekBar = (SeekBar)findViewById(R.id.max_age_bar);
        m_ageMinSeekBar = (SeekBar)findViewById(R.id.min_age_bar);

        m_ageMinSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_ageMinEditText.setText(String.valueOf(progress));
                if(m_ageMinSeekBar.getProgress() > m_ageMaxSeekBar.getProgress())
                    m_ageMaxSeekBar.setProgress(m_ageMinSeekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        m_ageMaxSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_ageMaxEditText.setText(String.valueOf(progress));
                if(m_ageMinSeekBar.getProgress() > m_ageMaxSeekBar.getProgress())
                    m_ageMinSeekBar.setProgress(m_ageMaxSeekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.search_button_of_dialog)
        {
            Tag2.m_tab2.SearchAccount(m_nameEditText.getText().toString(), m_ageMaxSeekBar.getProgress(), m_ageMinSeekBar.getProgress());
        }
    }

}
