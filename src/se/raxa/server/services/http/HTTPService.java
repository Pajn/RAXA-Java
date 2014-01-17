package se.raxa.server.services.http;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.Format;
import org.restexpress.RestExpress;
import org.restexpress.serialization.DefaultSerializationProvider;
import org.restexpress.serialization.SerializationProvider;

/**
 * @author Rasmus Eneman
 */
public class HTTPService extends Thread {

    @Override
    public void run() {
        SerializationProvider serializationProvider = new DefaultSerializationProvider();
        serializationProvider.setDefaultFormat(Format.JSON);
        RestExpress.setSerializationProvider(serializationProvider);
        RestExpress server = new RestExpress()
                .setName("RAXA");

        defineRoutes(server);

        server.bind(9500);
        server.awaitShutdown();
    }

    private static void defineRoutes(RestExpress server) {
        DeviceController deviceController = new DeviceController();

        server.uri("/devices", deviceController)
                .method(HttpMethod.GET, HttpMethod.POST);

        server.uri("/devices/{deviceID}", deviceController)
                .method(HttpMethod.PUT, HttpMethod.DELETE)
                .action("readSingle", HttpMethod.GET);

        server.uri("/devices/{deviceID}/execute", deviceController)
                .action("execute", HttpMethod.POST);
    }
}
