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
import za.co.steff.shopaholicsdk.network.model.CitiesResponse;
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

    @Test
    public void clientReturnsExpectedListOfCities() throws Exception {
        // Await our client setup
        awaitClientSetup();

        // Assert that our client's list of cities matches our test data
        for(int i = 0; i < expectedResponse.getCities().size(); i++) {
            assertEquals(expectedResponse.getCities().get(i).getId(), client.getAllCities().get(i).getId());
            assertEquals(expectedResponse.getCities().get(i).getName(), client.getAllCities().get(i).getName());
        }
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

    @After
    public void tearDown() throws Exception {

    }

}
