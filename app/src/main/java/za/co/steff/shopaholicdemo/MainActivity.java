package za.co.steff.shopaholicdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.steff.shopaholicdemo.adapter.CitiesAdapter;
import za.co.steff.shopaholicdemo.adapter.MallsAdapter;
import za.co.steff.shopaholicdemo.adapter.ShopsAdapter;
import za.co.steff.shopaholicsdk.ShopaholicClient;
import za.co.steff.shopaholicsdk.common.dto.City;
import za.co.steff.shopaholicsdk.common.dto.Mall;
import za.co.steff.shopaholicsdk.common.dto.Shop;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @BindView(R.id.txtLoading)
    TextView txtLoading;
    @BindView(R.id.listShopaholic)
    ListView listShopaholic;
    TextView listTitle;

    private ShopaholicClient client;

    private CitiesAdapter citiesAdapter;
    private MallsAdapter mallsAdapter;
    private ShopsAdapter shopsAdapter;

    private City selectedCity;
    private Mall selectedMall;

    private boolean warningShown = false;

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

            // Show list of cities
            showTitle("Cities");
            showCities();
        }

        @Override
        public void onClientLoadError(Throwable t) {
            txtLoading.setText(String.format("Failed to load data:\n%s", t.getMessage()));
        }
    };

    private void showTitle(String title) {
        if(listTitle == null) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View header = inflater.inflate(R.layout.list_header_title, null);
            listTitle = header.findViewById(R.id.txtTitle);
            listShopaholic.addHeaderView(header);
        }
        listTitle.setText(title);
    }

    private void showCities() {
        warningShown = false;

        showTitle("Cities");

        citiesAdapter = new CitiesAdapter(MainActivity.this, client.getAllCities());
        citiesAdapter.setListener(citiesAdapterListener);
        listShopaholic.setAdapter(citiesAdapter);
        listShopaholic.setTag(citiesAdapter);
    }

    private CitiesAdapter.CitiesAdapterListener citiesAdapterListener = new CitiesAdapter.CitiesAdapterListener() {
        @Override
        public void onCitySelected(City city) {
            selectedCity = city;

            // Show list of malls for city
            showMalls(city);
        }
    };

    private void showMalls(City city) {
        warningShown = false;

        showTitle("Malls in " + city.getName());

        mallsAdapter = new MallsAdapter(MainActivity.this, client.getMallsForCity(city));
        mallsAdapter.setListener(mallsAdapterListener);
        listShopaholic.setAdapter(mallsAdapter);
        listShopaholic.setTag(mallsAdapter);
    }

    private MallsAdapter.MallsAdapterListener mallsAdapterListener = new MallsAdapter.MallsAdapterListener() {
        @Override
        public void onMallSelected(Mall mall) {
            selectedMall = mall;

            // Show list of shops for mall
            showShops(mall);
        }
    };

    private void showShops(Mall mall) {
        warningShown = false;

        showTitle("Shops in " + mall.getName());

        shopsAdapter = new ShopsAdapter(MainActivity.this, client.getShopsForMall(mall));
        shopsAdapter.setListener(shopsAdapterListener);
        listShopaholic.setAdapter(shopsAdapter);
        listShopaholic.setTag(shopsAdapter);
    }

    private ShopsAdapter.ShopsAdapterListener shopsAdapterListener = new ShopsAdapter.ShopsAdapterListener() {
        @Override
        public void onShopSelected(Shop shop) {
            warningShown = false;
        }
    };

    @Override
    public void onBackPressed() {
        // If back is pressed after the warning was shown, exit the app
        if(warningShown) {
            super.onBackPressed();
            return;
        }

        // Handle back-press based on which list we're showing
        if(listShopaholic.getTag() instanceof CitiesAdapter) {
            Toast.makeText(MainActivity.this, "Click back again to exit app.", Toast.LENGTH_LONG).show();
            warningShown = true;
        } else if(listShopaholic.getTag() instanceof MallsAdapter) {
            showCities();
        } else if(listShopaholic.getTag() instanceof ShopsAdapter) {
            showMalls(selectedCity);
        }
    }
}
