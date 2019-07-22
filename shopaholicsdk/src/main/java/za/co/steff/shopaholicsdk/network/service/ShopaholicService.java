package za.co.steff.shopaholicsdk.network.service;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import za.co.steff.shopaholicsdk.network.model.CitiesResponse;

public interface ShopaholicService {

    @GET("5b7e8bc03000005c0084c210")
    Call<CitiesResponse> getAllCities();

}
