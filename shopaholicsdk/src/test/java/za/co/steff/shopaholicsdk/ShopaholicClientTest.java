package za.co.steff.shopaholicsdk;

import android.util.Log;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import za.co.steff.shopaholicsdk.common.dto.City;
import za.co.steff.shopaholicsdk.common.dto.Mall;
import za.co.steff.shopaholicsdk.network.model.CitiesResponse;
import za.co.steff.shopaholicsdk.network.model.CityResponse;
import za.co.steff.shopaholicsdk.network.model.MallResponse;
import za.co.steff.shopaholicsdk.network.service.MockShopaholicService;
import za.co.steff.shopaholicsdk.network.service.ShopaholicService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class ShopaholicClientTest {

    private MockShopaholicService shopaholicService;
    private ShopaholicClient client;

    private CitiesResponse expectedResponse;

    @Before
    public void setUp() {
        // Tell PowerMockito to mock all static calls of the Log class
        // Without this any static Log calls will throw an exception
        PowerMockito.mockStatic(Log.class);

        // Load our expected response from a json file
        InputStream successResponse = getClass().getClassLoader().getResourceAsStream("success-response.json");
        InputStreamReader streamReader = new InputStreamReader(successResponse);
        expectedResponse = new Gson().fromJson(streamReader, CitiesResponse.class);

        // Ensure that our test data loaded successfully
        assertNotNull(expectedResponse);
        assertNotEquals(expectedResponse.getCities().size(), 0); // Our test data should always have at least one city
        assertNotEquals(expectedResponse.getCities().get(0).getMalls().size(), 0); // Our test data should always have at least one mall
        assertNotEquals(expectedResponse.getCities().get(0).getMalls().get(0).getShops().size(), 0); // Our test data should always have at least one shop

        // Configure our mock ShopaholicService
        shopaholicService = new MockShopaholicService(true);
        shopaholicService.setExpectedResponse(expectedResponse);
    }

    /** As a developer, I would like to request a list of cities. **/
    @Test
    public void clientReturnsExpectedListOfCities() throws Exception {
        awaitClientSetup();

        // Assert that our client's list of cities matches our test data
        for(int i = 0; i < expectedResponse.getCities().size(); i++) {
            CityResponse ourCity = expectedResponse.getCities().get(i);
            City clientCity = client.getAllCities().get(i);

            assertEquals(ourCity.getId(), clientCity.getId());
            assertEquals(ourCity.getName(), clientCity.getName());
        }
    }

    /** As a developer, I would like to request a particular city. **/
    @Test
    public void clientReturnsExpectedCity() throws Exception {
        awaitClientSetup();

        // Take a city from our expected response and test whether its name matches the name of the city returned by our client
        CityResponse ourCity = expectedResponse.getCities().get(0);
        City clientCity = client.getCity(ourCity.getId());
        assertEquals(ourCity.getName(), clientCity.getName());

        // Testing negative case
        ourCity = new CityResponse();
        ourCity.setName("random-city-name");
        assertNotEquals(ourCity.getName(), clientCity.getName());
    }

    /** As a developer, I would like to request a list of malls in a city. **/
    @Test
    public void clientReturnsExpectedListOfMalls() throws Exception {
        awaitClientSetup();

        // Assert that our client's list of malls for the first city matches our test data
        for(int i = 0; i < expectedResponse.getCities().size(); i++) {
            CityResponse ourCity = expectedResponse.getCities().get(i);

            // Test the malls contained in our client's city matches our test data
            for(int j = 0; j < ourCity.getMalls().size(); j++) {
                MallResponse ourMall = ourCity.getMalls().get(j);
                Mall clientMall = client.getMallsForCity(ourCity.getId()).get(j);

                assertEquals(ourMall.getId(), clientMall.getId());
                assertEquals(ourMall.getName(), clientMall.getName());
            }
        }
    }

    /** As a developer, I would like to request a particular mall in a city. **/
    @Test
    public void clientReturnsExpectedMall() throws Exception {
        awaitClientSetup();

        // Take a mall from our expected response and test whether its name matches the name of the mall returned by our client
        CityResponse ourCity = expectedResponse.getCities().get(0);
        MallResponse ourMall = ourCity.getMalls().get(0);

        Mall clientMall = client.getMall(ourCity.getId(), ourMall.getId());
        assertEquals(ourMall.getName(), clientMall.getName());

        // Testing negative case
        ourMall = new MallResponse();
        ourMall.setName("random-mall-name");
        assertNotEquals(ourMall.getName(), clientMall.getName());
    }


    private void awaitClientSetup() throws Exception {
        // Create a countdown latch to await the asynchronous result of our ShopaholicClient's initialization
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean initializationResult = new AtomicBoolean(false);

        // Try to initialize our client, passing it our mock ShopaholicService
        client = new ShopaholicClient(new ShopaholicClient.ShopaholicClientEventListener() {
            @Override
            public void onClientLoaded() {
                // If it passes, release our latch with a successful result
                initializationResult.set(true);
                latch.countDown();
            }

            @Override
            public void onClientLoadError(Throwable t) {
                // If it fails, release our latch with a false result
                latch.countDown();
            }
        }, shopaholicService);
        latch.await();

        // If client load fails, throw an exception
        if(client == null || ! initializationResult.get()) {
            throw new IllegalStateException("Client initialization failed. Expected success.");
        }
    }

}
