package za.co.steff.shopaholicsdk;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import za.co.steff.shopaholicsdk.network.model.CitiesResponse;
import za.co.steff.shopaholicsdk.network.service.ShopaholicService;

public class MockShopaholicService implements ShopaholicService {

    private boolean successfulResponse;
    private CitiesResponse response = new CitiesResponse();

    public MockShopaholicService(boolean successfulResponse) {
        this.successfulResponse = successfulResponse;
    }

    public void setResponse(CitiesResponse response) {
        this.response = response;
    }

    @Override
    public Call<CitiesResponse> getAllCities() {
        return new Call<CitiesResponse>() {
            @Override
            public Response<CitiesResponse> execute() throws IOException {
                return null;
            }

            @Override
            public void enqueue(Callback<CitiesResponse> callback) {
                if(successfulResponse)
                    callback.onResponse(this, Response.success(response));
                else
                    callback.onFailure(this, new Exception());
            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<CitiesResponse> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };
    }
}
