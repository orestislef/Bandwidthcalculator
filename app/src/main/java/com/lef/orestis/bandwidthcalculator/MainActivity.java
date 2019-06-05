package com.lef.orestis.bandwidthcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class MainActivity extends AppCompatActivity {

    RadioButton RadioButtonBytes, RadioButtonKiloBytes, RadioButtonMegaBytes, RadioButtonGigaBytes;
    RadioButton RadioButtonBytesPs, RadioButtonKiloBytesPs, RadioButtonMegaBytesPs, RadioButtonGigaBytesPs;
    TextView TimeResult, TimeResultText,benchmarkDataLossInfo;
    private double FileSizeFormat;
    private double BandwidthFormat;
    EditText FileSizeInput, BandwidthInput;
    WebView wv;
    ImageButton speedTestButton,cancelSpeedTestButton;

    LinearLayout timeResultLayout;
    public static final String URL_SPEEDTEST = "http://94.67.96.91:8080/speedtest/speedtest.html";
//    public static final String URL_SPEEDTEST = "https://www.speedcheck.org/";

    public double years = 0, days = 0, hours = 0, minutes = 0, seconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();

        radioButtonOnCreateState();

        webViewShow();

        FileSizeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getNumbersFromEditText();

            }
        });
        BandwidthInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getNumbersFromEditText();

            }
        });
    }

    private void webViewShow() {
        speedTestButton = findViewById(R.id.benchmark_imageButton);
        cancelSpeedTestButton = findViewById(R.id.cancel_benchmark_imageButton);
        benchmarkDataLossInfo = findViewById(R.id.speedTest_info);
        speedTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wv= findViewById(R.id.webview);
                wv.setVisibility(View.VISIBLE);
                wv.setWebViewClient(new WebViewClient());
                wv.loadUrl(URL_SPEEDTEST);
                wv.getSettings().setJavaScriptEnabled(true);
                speedTestButton.setVisibility(View.GONE);
                cancelSpeedTestButton.setVisibility(View.VISIBLE);
                benchmarkDataLossInfo.setVisibility(View.GONE);
            }
        });
        cancelSpeedTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wv.setVisibility(View.GONE);
                cancelSpeedTestButton.setVisibility(View.GONE);
                speedTestButton.setVisibility(View.VISIBLE);
            }
        });
    }

    public void radioButtonOnCreateState() {
        RadioButtonBytes.setChecked(true);
        FileSizeFormat = 8;
        RadioButtonBytesPs.setChecked(true);
        BandwidthFormat = 1;
    }

    public void getNumbersFromEditText() {
        if (checkValidFields(FileSizeInput, BandwidthInput)) {
            Double fileSize = Double.parseDouble(FileSizeInput.getText().toString());
            Double Bandwidth = Double.parseDouble(BandwidthInput.getText().toString());
            setTime(fileSize, Bandwidth, FileSizeFormat, BandwidthFormat);
        }
    }

    public void initialization() {
        RadioButtonBytes = findViewById(R.id.byteRadioButton);
        RadioButtonKiloBytes = findViewById(R.id.kilobyteRadioButton);
        RadioButtonMegaBytes = findViewById(R.id.megabyteRadioButton);
        RadioButtonGigaBytes = findViewById(R.id.gigabyteRadioButton);

        RadioButtonBytesPs = findViewById(R.id.bytepsRadioButton);
        RadioButtonKiloBytesPs = findViewById(R.id.kilobytepsRadioButton);
        RadioButtonMegaBytesPs = findViewById(R.id.megabytepsRadioButton);
        RadioButtonGigaBytesPs = findViewById(R.id.gigabytepsRadioButton);

        timeResultLayout = findViewById(R.id.layoutTime);

        FileSizeInput = findViewById(R.id.fileSizeEditText);
        BandwidthInput = findViewById(R.id.bandwidthEditText);

        TimeResultText = findViewById(R.id.timeText);
        TimeResult = findViewById(R.id.timeResult);
    }

    private boolean checkValidFields(EditText FileSizeInput, EditText BandwidthInput) {
        boolean notEmpty = true;
        if (FileSizeInput.getText().toString().equals("")) {
            YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(FileSizeInput);
            notEmpty = false;
        }
        if (BandwidthInput.getText().toString().equals("")) {
            YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(BandwidthInput);
            notEmpty = false;
        }
        if (BandwidthInput.getText().toString().equals("0")) {
            notEmpty = false;
            TimeResult.setText(R.string.max_time);
            YoYo.with(Techniques.Shake).duration(2000).repeat(3).playOn(TimeResultText);
            YoYo.with(Techniques.Tada).duration(2000).repeat(3).playOn(TimeResult);
        }
        return notEmpty;
    }

    public void setTime(double mFileSize, double mBandwidth, double mFileSizeFormat, double mBandwidthFormat) {
        double timeInSeconds = (mFileSize * mFileSizeFormat) / (mBandwidth * mBandwidthFormat);
        if (timeInSeconds < 1) {
            Log.d("timeInSeconds", "timeInSeconds " + String.valueOf(timeInSeconds));
            TimeResult.setText(R.string.min_time);
        } else {
            Log.d("timeInSeconds", "timeInSeconds " + String.valueOf(timeInSeconds));

            seconds = timeInSeconds % 60;
            minutes = timeInSeconds / 60 % 60;
            hours = timeInSeconds / 60 / 60 % 24;
            days = timeInSeconds / 60 / 60 / 24 % 365;
            years = timeInSeconds / 60 / 60 / 24 / 365;
            Log.d("timeInSeconds", "secnds : " + String.valueOf(seconds));
            Log.d("timeInSeconds", "minutes : " + String.valueOf(minutes));
            Log.d("timeInSeconds", "hours : " + String.valueOf(hours));
            Log.d("timeInSeconds", "days : " + String.valueOf(days));
            Log.d("timeInSeconds", "years : " + String.valueOf(years));
            showTime(years, days, hours, minutes, seconds);
        }
    }

    private void showTime(double years, double days, double hours, double minutes, double seconds) {
        String TimeResultText = null;
        if ((int) seconds > 0 | (int) minutes > 0 | (int) hours > 0 | (int) days > 0 | (int) years > 0) {
            TimeResultText = (int) seconds + " seconds";
            if ((int) minutes > 0 | (int) hours > 0 | (int) days > 0 | (int) years > 0) {
                TimeResultText = (int) minutes + " minutes, " + (int) seconds + " seconds";
                if ((int) hours > 0 | (int) days > 0 | (int) years > 0) {
                    TimeResultText = (int) hours + " hours, " + (int) minutes + " minutes, " + (int) seconds + " seconds";
                    if ((int) days > 0 | (int) years > 0) {
                        TimeResultText = (int) days + " days, " + (int) hours + " hours, " + (int) minutes + " minutes, " + (int) seconds + " seconds";
                        if ((int) years > 0) {
                            TimeResultText = (int) years + " years, " + (int) days + " days, " + (int) hours + " hours, " + (int) minutes + " minutes, " + (int) seconds + " seconds";
                        }
                    }
                }
            }

        }
        TimeResult.setText(TimeResultText);
    }

    public void onRadioButtonClickedFS(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.byteRadioButton:
                if (checked)
                    FileSizeFormat = 8;
                break;
            case R.id.kilobyteRadioButton:
                if (checked)
                    FileSizeFormat = 8192;
                break;
            case R.id.megabyteRadioButton:
                if (checked)
                    FileSizeFormat = 8388608;
                break;
            case R.id.gigabyteRadioButton:
                if (checked)
                    FileSizeFormat = 8589934592L;
                break;
        }
        getNumbersFromEditText();
    }

    public void onRadioButtonClickedBW(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.bytepsRadioButton:
                if (checked)
                    BandwidthFormat = 1;
                break;
            case R.id.kilobytepsRadioButton:
                if (checked)
                    BandwidthFormat = 1000;
                break;
            case R.id.megabytepsRadioButton:
                if (checked)
                    BandwidthFormat = 1e+6;
                break;
            case R.id.gigabytepsRadioButton:
                if (checked)
                    BandwidthFormat = 1e+9;
                break;
        }
        getNumbersFromEditText();
    }
}
