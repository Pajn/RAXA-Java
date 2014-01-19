package se.raxa.server.services.http;

import org.restexpress.Format;
import org.restexpress.response.JsendResponseWrapper;
import org.restexpress.response.ResponseWrapper;
import org.restexpress.serialization.AbstractSerializationProvider;
import org.restexpress.serialization.json.JacksonJsonProcessor;

/**
 * @author Rasmus Eneman
 */
class SerializationProvider extends AbstractSerializationProvider {
    private static final ResponseWrapper JSEND_WRAPPER = new JsendResponseWrapper();

    public SerializationProvider() {
        super();
        add(new JacksonJsonProcessor(Format.WRAPPED_JSON), JSEND_WRAPPER);
        setDefaultFormat(Format.WRAPPED_JSON);
    }
}
