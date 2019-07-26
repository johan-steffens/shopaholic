package za.co.steff.shopaholicsdk;

import android.util.Log;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.co.steff.shopaholicsdk.common.dto.City;
import za.co.steff.shopaholicsdk.common.dto.Mall;
import za.co.steff.shopaholicsdk.common.dto.Shop;
import za.co.steff.shopaholicsdk.network.model.CityResponse;
import za.co.steff.shopaholicsdk.common.exception.ClientLoadFailedException;
import za.co.steff.shopaholicsdk.network.APIServiceGenerator;
import za.co.steff.shopaholicsdk.network.model.CitiesResponse;
import za.co.steff.shopaholicsdk.network.model.MallResponse;
import za.co.steff.shopaholicsdk.network.model.ShopResponse;
import za.co.steff.shopaholicsdk.network.service.ShopaholicService;

public class ShopaholicClient {

    private static final String TAG = ShopaholicClient.class.getName();

    private ShopaholicClientEventListener listener;
    private ShopaholicService service;

    private CitiesResponse data;

    public ShopaholicClient(ShopaholicClientEventListener listener) {
        this.listener = listener;

        // Using the default Retrofit implementation ShopaholicService
        service = APIServiceGenerator.generateService(ShopaholicService.class);

        // Fetch initial batch of data
        fetchRemoteData();
    }

    public ShopaholicClient(ShopaholicClientEventListener listener, ShopaholicService service) {
        this.listener = listener;
        this.service = service;

        // Fetch initial batch of data
        fetchRemoteData();
    }

    private void fetchRemoteData() {
        service.getAllCities().enqueue(getAllCitiesCallback);
    }

    private Callback<CitiesResponse> getAllCitiesCallback = new Callback<CitiesResponse>() {
        @Override
        public void onResponse(Call<CitiesResponse> call, Response<CitiesResponse> response) {
            // Check to see that the response is successful and the body is present
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

    private void checkDataLoaded() {
        if(data == null)
            throw new IllegalStateException("The Shopaholic Client is in an unprepared state. Please ensure that data has been successfully loaded before executing any queries.");
    }

    /**
     * Forces data to be fetched from remote resource.
     *
     * Completion will call attached {@link ShopaholicClientEventListener} onClientLoaded() or
     * onClientLoadError() method depending on whether the data was successfully retrieved or not.
     */
    public void reloadData() {
        fetchRemoteData();
    }

    /**
     * @return a list of cities contained within the retrieved data set.
     */
    public List<City> getAllCities() {
        // Ensure we have data before proceeding
        checkDataLoaded();

        // Return a list of all cities
        return data.getCities().stream()
                .map(city -> new City(city))
                .collect(Collectors.toList());
    }

    /**
     * @param id the id of the city to be retrieved
     * @return a city based on provided id, or null if the city can't be found
     */
    public City getCity(long id) {
        // Ensure we have data before proceeding
        checkDataLoaded();

        // Try to find the city based on its id
        Optional<CityResponse> cityFound = data.getCities().stream()
                .filter(city -> city.getId() == id)
                .findFirst();

        // If the city was found, return it. Otherwise return null
        if(cityFound.isPresent()) {
            return new City(cityFound.get());
        } else {
            return null;
        }
    }

    /**
     *
     * @param city the city whose malls are to be retrieved
     * @return a list of malls associated to the given city
     */
    public List<Mall> getMallsForCity(City city) {
        return getMallsForCity(city.getId());
    }

    /**
     *
     * @param cityId the id of the city whose malls are to be retrieved
     * @return a list of malls associated to the given city
     */
    public List<Mall> getMallsForCity(long cityId) {
        // Ensure we have data before proceeding
        checkDataLoaded();

        // Try to find the city based on its id
        Optional<CityResponse> cityFound = data.getCities().stream()
                .filter(city -> city.getId() == cityId)
                .findFirst();

        // If the city was found, return a list of its malls. Otherwise return null
        if(cityFound.isPresent()) {
            return cityFound.get().getMalls().stream()
                    .map(mall -> new Mall(mall))
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    /**
     * @param city the city that the mall is found in
     * @param mallId the id of the mall to be retrieved
     * @return a mall based on provided city and mallId, or null if the mall can't be found
     */
    public Mall getMall(City city, long mallId) {
        return getMall(city.getId(), mallId);
    }

    /**
     * @param cityId the id of the city that the mall is found in
     * @param mallId the id of the mall to be retrieved
     * @return a mall based on provided cityId and mallId, or null if the mall can't be found
     */
    public Mall getMall(long cityId, long mallId) {
        // Ensure we have data before proceeding
        checkDataLoaded();

        // Try to find the city based on its id
        Optional<CityResponse> cityFound = data.getCities().stream()
                .filter(city -> city.getId() == cityId)
                .findFirst();

        // If the city is present, search for the mall
        if(cityFound.isPresent()) {
            // Try to find the mall based on its id
            Optional<MallResponse> mallFound = cityFound.get().getMalls().stream()
                    .filter(mall -> mall.getId() == mallId)
                    .findFirst();

            // If the mall was found, return it
            if(mallFound.isPresent()) {
                return new Mall(mallFound.get());
            }
        }

        // If the city or the mall is not found, return null
        return null;
    }

    /**
     * @param mall the mall that the shop is found in
     * @return a list of shops associated to the given mall
     */
    public List<Shop> getShopsForMall(Mall mall) {
        return getShopsForMall(mall.getId());
    }

    /**
     * @param mallId the id of the mall that the shop is found in
     * @return a list of shops associated to the given mall
     */
    public List<Shop> getShopsForMall(long mallId) {
        // Ensure we have data before proceeding
        checkDataLoaded();

        // Try to find the mall based on its id
        Optional<MallResponse> mallFound = data.getCities().stream()
                .flatMap(city -> city.getMalls().stream())
                .collect(Collectors.toList()).stream()
                .filter(mall -> mall.getId() == mallId)
                .findFirst();

        // If the mall is present, return its shops. Otherwise return null
        if(mallFound.isPresent()) {
            return mallFound.get().getShops().stream()
                    .map(shop -> new Shop(shop))
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    /**
     * @param mall the mall that the shop is found in
     * @param shopId the id of the shop to be retrieved
     * @return a shop based on provided mall and shopId, or null if the shop can't be found
     */
    public Shop getShop(Mall mall, long shopId) {
        return getShop(mall.getId(), shopId);
    }

    /**
     * @param mallId the id of the mall that the shop is found in
     * @param shopId the id of the shop to be retrieved
     * @return a shop based on provided mall and shopId, or null if the shop can't be found
     */
    public Shop getShop(long mallId, long shopId) {
        // Ensure we have data before proceeding
        checkDataLoaded();

        // Try to find the mall based on its id
        Optional<MallResponse> mallFound = data.getCities().stream()
                .flatMap(city -> city.getMalls().stream())
                .collect(Collectors.toList()).stream()
                .filter(mall -> mall.getId() == mallId)
                .findFirst();

        // If the mall is present, search for the shop
        if(mallFound.isPresent()) {
            // Try to find the shop based on its id
            Optional<ShopResponse> shopFound = mallFound.get().getShops().stream()
                    .filter(shop -> shop.getId() == shopId)
                    .findFirst();

            // If the mall was found, return it
            if(shopFound.isPresent()) {
                return new Shop(shopFound.get());
            }
        }

        // If the mall or the shop is not found, return null
        return null;
    }

    public interface ShopaholicClientEventListener {
        void onClientLoaded();
        void onClientLoadError(Throwable t);
    }

}
