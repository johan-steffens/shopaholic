package za.co.steff.shopaholicsdk.network.service;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.co.steff.shopaholicsdk.network.model.CitiesResponse;

public class MockShopaholicService implements ShopaholicService {

    private boolean isCallSuccessful;
    private CitiesResponse expectedResponse = new CitiesResponse();

    public MockShopaholicService(boolean isCallSuccessful) {
        this.isCallSuccessful = isCallSuccessful;
    }

    public void setExpectedResponse(CitiesResponse expectedResponse) {
        this.expectedResponse = expectedResponse;
    }

    @Override
    public Call<CitiesResponse> getAllCities() {
        return getAllCitiesCall;
    }

    private Call<CitiesResponse> getAllCitiesCall = new Call<CitiesResponse>() {
        @Override
        public void enqueue(Callback<CitiesResponse> callback) {
            if(isCallSuccessful) {
                callback.onResponse(this, Response.success(expectedResponse));
            } else {
                callback.onFailure(this, new Exception("Expected call to fail."));
            }
        }

        @Override
        public Response<CitiesResponse> execute() throws IOException { return null; }

        @Override
        public boolean isExecuted() { return false; }

        @Override
        public void cancel() {}

        @Override
        public boolean isCanceled() { return false; }

        @Override
        public Call<CitiesResponse> clone() { return null; }

        @Override
        public Request request() { return null; }
    };
}
