package za.co.steff.shopaholicsdk;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import za.co.steff.shopaholicsdk.network.APIServiceGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ShopaholicClientTest {

    private MockWebServer mockWebServer;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // Change the URL of our service to hit our mock service
        APIServiceGenerator.BASE_URL = "http://localhost:8888/";
    }

    @Test
    public void clientLoadsSuccessfully() throws InterruptedException {
        // Enqueue a successful response to be used by our once-off client
        mockWebServer.enqueue(new MockResponse()
                .setBody("{ \"cities\": [ {\n" +
                        "      \"id\": 1,\n" +
                        "      \"name\": \"Cape Town\",\n" +
                        "      \"malls\": [\n" +
                        "        {\n" +
                        "          \"id\": 1,\n" +
                        "          \"name\": \"Century City\",\n" +
                        "          \"shops\": [\n" +
                        "            {\n" +
                        "              \"id\": 1,\n" +
                        "              \"name\": \"Nespresso\"\n" +
                        "            }\n" +
                        "          ]\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
                .setResponseCode(200));

        // Create a countdown latch to await the asynchronous nature of our ShopaholicClient
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean success = new AtomicBoolean(false);

        // Build a once-off client to ensure client loads successfully
        new ShopaholicClient(new ShopaholicClient.ShopaholicClientEventListener() {
            @Override
            public void onClientLoaded() {
                success.set(true);
                latch.countDown();
            }

            @Override
            public void onClientLoadError(Throwable t) {
                latch.countDown();
            }
        });

        // Await our result timing out after 30 seconds
        latch.await(30, TimeUnit.SECONDS);

        // Check whether client loaded successfully
        assertTrue(success.get());
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

}
