package za.co.steff.shopaholicsdk;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.co.steff.shopaholicsdk.common.dto.City;
import za.co.steff.shopaholicsdk.common.exception.ClientLoadFailedException;
import za.co.steff.shopaholicsdk.network.APIServiceGenerator;
import za.co.steff.shopaholicsdk.network.model.CitiesResponse;
import za.co.steff.shopaholicsdk.network.service.ShopaholicService;

public class ShopaholicClient {

    private static final String TAG = ShopaholicClient.class.getName();

    private ShopaholicClientEventListener listener;
    private ShopaholicService service;

    private CitiesResponse data;

    public ShopaholicClient(ShopaholicClientEventListener listener) {
        this.listener = listener;

        // Fetch initial batch of data
        fetchRemoteData();
    }

    private void fetchRemoteData() {
        service = APIServiceGenerator.generateService(ShopaholicService.class);
        service.getAllCities().enqueue(getAllCitiesCallback);
    }

    private Callback<CitiesResponse> getAllCitiesCallback = new Callback<CitiesResponse>() {
        @Override
        public void onResponse(Call<CitiesResponse> call, Response<CitiesResponse> response) {
            // If the response is not su
            if(response.isSuccessful() && response.body() != null) {
                Log.d(TAG, "Get all cities successfully completed.");
                data = response.body();

                // Notify listener that client has successfully loaded
                if(listener != null) {
                    listener.onClientLoaded();
                }
            } else {
                Log.e(TAG, "Get all cities failed.");

                // Notify listener that client failed to load
                if(listener != null) {
                    listener.onClientLoadError(new ClientLoadFailedException());
                }
            }
        }

        @Override
        public void onFailure(Call<CitiesResponse> call, Throwable t) {
            Log.e(TAG, "Response failed :: " + Log.getStackTraceString(t));

            // Notify listener that client failed to load
            if(listener != null) {
                listener.onClientLoadError(t);
            }
        }
    };

    public void reloadData() {
        fetchRemoteData();
    }

    public interface ShopaholicClientEventListener {

        void onClientLoaded();
        void onClientLoadError(Throwable t);

    }

}
