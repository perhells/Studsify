package se.dewire.studs2016template;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private DewireContestConnection s = null;

    private static String GROUP_TAG = "Grp-"+Math.abs((new Random()).nextInt()%100000);     //Change this to your group name


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        s = new DewireContestConnection(GROUP_TAG){
            @Override public void onConnectionOpened(){
                //Here you put code that will happen when the connection opens

                updateString("Text i have written");
            }
            @Override public void onConnectionClosed(){
                //Here you put code that will happen when the connection closes
            }
        };
    }


    public void updateString(String str){
        if(s!=null && s.isConnected()){
            s.sendMessage(str);
        }
    }


    public void killapp(){
        s.disconnect();
        this.finish();
    }
}
