package se.dewire.studs2016template;

import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private DewireContestConnection s = null;

    private static String GROUP_TAG = "Studsify";     //Change this to your group name
    //private String[][] letters = {{"a", "b", "c", "d", "e", "f"}, { "g", "h", "i", "j","k", "l"}, {"m", "n", "o", "p", "q", "r"}, {"s", "t","u", "v", "w", "x"}, {"y", "z", "å", "ä", "ö", " "}};
    private String[][] letters = {{"z", "u", "p", "k", "f", "a"},
        {"å", "v", "q", "l", "g", "b"},
        {"ä", "w", "r", "m", "h", "c"},
        {"ö", "x", "s", "n", "i", "d"},
        {" ", "y", "t", "o", "j", "e"}};
    //private String[][] letters = {{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"},{"k", "l", "m", "n", "o", "p", "q", "r", "s", "t"},{"u", "v", "w", "x", "y", "z", "å", "ä", "ö", " "}};
    private static String currentString = "";
    private static String previousString = "";
    private String currentLetter;
    private String displayText;
    private boolean hasEnteredALetterIntoTheCurrentString = false;
    TextView txt1;


    final float[] mValuesMagnet      = new float[3];
    final float[] mValuesAccel       = new float[3];
    final float[] mValuesOrientation = new float[3];
    final float[] mValuesProximity   = new float[1];
    final float[] mValuesLight       = new float[1];
    final float[] mRotationMatrix    = new float[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt1 = (TextView) findViewById(R.id.hello);

        s = new DewireContestConnection(GROUP_TAG){
            @Override public void onConnectionOpened(){
                //Here you put code that will happen when the connection opens
            }
            @Override public void onConnectionClosed(){
                //Here you put code that will happen when the connection closes
            }
        };


        SensorManager sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);


        final SensorEventListener mEventListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            public void onSensorChanged(SensorEvent event) {
                // Handle the events for which we registered
                switch (event.sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        System.arraycopy(event.values, 0, mValuesAccel, 0, 3);
                        break;

                    case Sensor.TYPE_MAGNETIC_FIELD:
                        System.arraycopy(event.values, 0, mValuesMagnet, 0, 3);
                        break;

                    case Sensor.TYPE_PROXIMITY:
                        System.arraycopy(event.values, 0, mValuesProximity, 0, 1);
                        break;

                    case Sensor.TYPE_LIGHT:
                        System.arraycopy(event.values, 0, mValuesLight, 0, 1);
                        break;

                }

                SensorManager.getRotationMatrix(mRotationMatrix, null, mValuesAccel, mValuesMagnet);
                SensorManager.getOrientation(mRotationMatrix, mValuesOrientation);
                currentLetter = getLetter(mValuesOrientation[2],mValuesOrientation[1]);
                displayText = currentString + "\n" +  arrayToString(currentLetter);
                checkProximity();
                checkLight();
                changeFontSize(getLength(mValuesMagnet[0],mValuesMagnet[1],mValuesMagnet[2]));
                txt1.setText(displayText);
                updateString();
            }
        };

        // You have set the event listener up, now just need to register this with the
        // sensor manager along with the sensor wanted.
        setListners(sensorManager, mEventListener);

        txt1.setTypeface(Typeface.MONOSPACE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            appendLetter();
        } else if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            eraseLetter();
        } else if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            currentString += " ";
        }
        return true;
    }

    private void checkLight() {
        if (mValuesLight[0] > 77) {
            txt1.setBackgroundColor(Color.BLACK);
            txt1.setTextColor(Color.GREEN);
        } else if (mValuesLight[0] <= 77) {
            txt1.setBackgroundColor(Color.WHITE);
            txt1.setTextColor(Color.BLACK);
        }
    }

    private void checkProximity() {
        if (mValuesProximity[0] < 2.5 && !hasEnteredALetterIntoTheCurrentString) {
            appendLetter();
            hasEnteredALetterIntoTheCurrentString = true;
        } else if (mValuesProximity[0] > 2.5 && hasEnteredALetterIntoTheCurrentString) {
            hasEnteredALetterIntoTheCurrentString = false;
        }
    }

    private void changeFontSize(double incoming) {
        float textSize = txt1.getTextSize();
        if (incoming > 40) {
             textSize = Math.max(10,36 - (float) Math.round((incoming - 40) / 10));
        } else if (textSize > 0) {
            textSize = 36;
        }
        txt1.setTextSize(textSize);

    }

    private float getLength(float x, float y, float z) {
        return (float) Math.sqrt((x*x + y*y + z*z));
    }

    private void appendLetter() {
        currentString += getLetter(mValuesOrientation[2],mValuesOrientation[1]);
    }

    private void eraseLetter() {
        if (currentString.length() > 0) {
            currentString = currentString.substring(0,currentString.length()-1);
        }
    }

    private String getLetter(float sideways, float forward) {
        int y = Math.round((forward + 1)/2*6);
        int x;
        if (sideways < -0.6) {
            x = 0;
        } else if (sideways < -0.2) {
            x = 1;
        } else if (sideways < 0.2) {
            x = 2;
        } else if (sideways < 0.6) {
            x = 3;
        } else {
            x = 4;
        }
        if (y < 0) {
            y = 0;
        } else if (y > 5) {
            y = 5;
        }
        return letters[x][y];
    }

    private String arrayToString(String letter) {
        String text = "";
        for (int j = 5; j >= 0; j--) {
            for (int i = 0; i <= 4; i++) {
                if (letter.equals(letters[i][j])) {
                    text += "[" + letters[i][j] + "]";
                } else {
                    text += " " + letters[i][j] + " ";
                }
            }
            text+= "\n";
        }
        return text;
    }

    // Register the event listener and sensor type.
    public void setListners(SensorManager sensorManager, SensorEventListener mEventListener)
    {
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void updateString(){
        if (!currentString.equals(previousString)) {
            previousString = currentString;
            if(s!=null && s.isConnected()){
                s.sendMessage(currentString);
            }
        }
    }


    public void killapp(){
        s.disconnect();
        this.finish();
    }
}
