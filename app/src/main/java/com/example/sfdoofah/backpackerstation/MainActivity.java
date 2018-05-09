package com.example.sfdoofah.backpackerstation;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.os.Handler;


public class MainActivity extends ActionBarActivity {
    Button online;
    Handler handler=new Handler();
    int tmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        online=(Button) findViewById(R.id.button);
        final WiFiAdmin wiFiAdmin = new WiFiAdmin(MainActivity.this);
        handler.postDelayed(runnable, 1000);
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wiFiAdmin.checkState() != 3) {
                    wiFiAdmin.openWiFi();
                }
                wiFiAdmin.addNetWork(wiFiAdmin.createWiFiInfo("N66u-5G", "csien66u", 3));
                tmp = wiFiAdmin.getNetWrokId();
            }
        });
//        btnOff.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                wiFiAdmin.removeWiFiInfo("N66u-5G");
//            }
//        });
    }
    public String longToIp(long ip) {
        return (ip & 0xFF) + "."
                + ((ip >> 8) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 24) & 0xFF);
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            WiFiAdmin wiFiAdmin = new WiFiAdmin(MainActivity.this);
//            wiFiName.setText(wiFiAdmin.getSSID());
//            wiFiStatus.setText(String.valueOf(wiFiAdmin.checkState() + "/" + wiFiAdmin.getNetWrokId()));
//            wiFiIp.setText(String.valueOf(longToIp(wiFiAdmin.getIpAddress())));
            handler.postDelayed(this, 1000);
        }
    };
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
