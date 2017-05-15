package edu.csumb.ybyun.ctof;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;

public class CtoFActivity extends Activity implements OnCheckedChangeListener, OnSeekBarChangeListener {
    private RadioGroup cfRadioGroup;
    private RadioButton cRadioButton, fRadioButton;
    private SeekBar percentSeekBar;
    private TextView resultTextView, percentTextView;
    private SharedPreferences savedValues;
    private int tempSliderValue = 70;
    private boolean celcius = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctof);

        cfRadioGroup = (RadioGroup) findViewById(R.id.cfRadioGroup);
        cRadioButton = (RadioButton) findViewById(R.id.cRadioButton);
        fRadioButton = (RadioButton) findViewById(R.id.fRadioButton);
        resultTextView = (TextView) findViewById(R.id.result_TextView);
        percentTextView = (TextView) findViewById(R.id.percentTextView);
        percentSeekBar = (SeekBar) findViewById(R.id.percentSeekBar);

        cfRadioGroup.setOnCheckedChangeListener(this);
        percentSeekBar.setOnSeekBarChangeListener(this);

        // get SharedPreferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
        resultTextView.setText(String.valueOf(tempSliderValue));
    }

    @Override
    public void onPause() {
        // save the instance variables
        Editor editor = savedValues.edit();
        editor.putString("tempSliderString", percentTextView.toString());
        //editor.putString("resultString", resultTextView.toString());
        editor.putInt("progressValue", percentSeekBar.getProgress());
        editor.putInt("tempSliderValue", tempSliderValue);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        resultTextView.setText(String.valueOf(savedValues.getString("resultTextView","")));
        //percentTextView.setText(savedValues.getString("tempSliderString",""));// + "°");
        percentTextView.setText(String.valueOf(savedValues.getInt("progressValue", tempSliderValue)));
        //int progress = savedValues.getInt("tempSliderValue",tempSliderValue);
        percentSeekBar.setProgress(savedValues.getInt("progressValue", tempSliderValue));
        calculateAndDisplay();
    }

    public void calculateAndDisplay(){
        int progress = percentSeekBar.getProgress();
        percentTextView.setText(String.valueOf(progress));
        Double i = Double.valueOf(percentTextView.getText().toString());
        if(celcius){
            i = (i - 32) * 5 / 9;
            if (i == -0.0) {
                i = 0.0;
            }
            resultTextView.setText(String.format("%.0f°C", i));
            //i = 0.0;
        }
        else{
            i = Double.valueOf(percentTextView.getText().toString());
            i = 9 * (i / 5) + 32;
            if (i == -0.0) {
                i = 0.0;
            }
            resultTextView.setText(String.format("%.0f°F", i));
        }
    }

    //*****************************************************
    // Event handler for the RadioGroup
    //*****************************************************
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.cRadioButton:
                celcius = true;
                calculateAndDisplay();
                break;
            case R.id.fRadioButton:
                celcius = false;
                calculateAndDisplay();
                break;

        }
    }

    //*****************************************************
    // Event handler for the SeekBar
    //*****************************************************
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        percentTextView.setText(String.valueOf(progress));// + "°");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        calculateAndDisplay();
    }
}
