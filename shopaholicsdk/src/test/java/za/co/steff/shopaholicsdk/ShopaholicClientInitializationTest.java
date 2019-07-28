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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import za.co.steff.shopaholicsdk.network.service.MockShopaholicService;
import za.co.steff.shopaholicsdk.network.service.ShopaholicService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class ShopaholicClientInitializationTest {

    private ShopaholicService shopaholicService;

    @Before
    public void setUp() {
        // Tell PowerMockito to mock all static calls of the Log class
        // Without this any static Log calls will throw an exception
        PowerMockito.mockStatic(Log.class);
    }

    @Test
    public void clientLoadSucceeds() throws Exception {
        // Create a mock ShopaholicService that returns success and an empty body
        shopaholicService = new MockShopaholicService(true);

        // Create a countdown latch to await the asynchronous result of our ShopaholicClient's initialization
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean initializationResult = new AtomicBoolean(false);

        // Try to initialize our client, passing it our mock ShopaholicService
        new ShopaholicClient(new ShopaholicClient.ShopaholicClientEventListener() {
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

        // When our latch releases, assert that our client loaded without issues
        assertTrue(initializationResult.get());
    }

    @Test
    public void clientLoadFailsGracefully() throws Exception {
        // Replace our existing ShopaholicService with one that expects an initialization error
        shopaholicService = new MockShopaholicService(false);

        // Create a countdown latch to await the asynchronous result of our ShopaholicClient's initialization
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean initializationResult = new AtomicBoolean(true);

        // Try to initialize our client, passing it our mock ShopaholicService
        new ShopaholicClient(new ShopaholicClient.ShopaholicClientEventListener() {
            @Override
            public void onClientLoaded() {
                // If it passes, release our latch with a successful result
                latch.countDown();
            }

            @Override
            public void onClientLoadError(Throwable t) {
                // If it fails, release our latch with a false result
                initializationResult.set(false);
                latch.countDown();
            }
        }, shopaholicService);
        latch.await();

        // When our latch releases, assert that our client loaded without issues
        assertFalse(initializationResult.get());
    }

}
