package za.co.steff.shopaholicdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import za.co.steff.shopaholicsdk.ShopaholicClient;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private ShopaholicClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new ShopaholicClient(clientEventListener);
    }

    private ShopaholicClient.ShopaholicClientEventListener clientEventListener = new ShopaholicClient.ShopaholicClientEventListener() {
        @Override
        public void onClientLoaded() {
            Log.d(TAG, "Client successfully loaded");
            Log.d(TAG, "First city :: " + client.getAllCities().get(0).toString());
        }

        @Override
        public void onClientLoadError(Throwable t) {
            Log.e(TAG, "Client load failed :: " + Log.getStackTraceString(t));
        }
    };

}
