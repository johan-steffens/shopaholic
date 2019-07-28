package za.co.steff.shopaholicdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.steff.shopaholicsdk.ShopaholicClient;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @BindView(R.id.txtLoading)
    TextView txtLoading;
    @BindView(R.id.listShopaholic)
    ListView listShopaholic;

    private ShopaholicClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind the views
        ButterKnife.bind(this);

        // Create aan instance of our ShopaholicClient
        client = new ShopaholicClient(clientEventListener);
    }

    private ShopaholicClient.ShopaholicClientEventListener clientEventListener = new ShopaholicClient.ShopaholicClientEventListener() {
        @Override
        public void onClientLoaded() {
            txtLoading.setVisibility(View.GONE);
        }

        @Override
        public void onClientLoadError(Throwable t) {
            txtLoading.setText("Failed to load data:\n" + t.getMessage());
        }
    };

}
