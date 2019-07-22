package za.co.steff.shopaholicsdk.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIServiceGenerator {

    public static <T> T generateService(Class<T> serviceClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.mocky.io/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(serviceClass);
    }

}
