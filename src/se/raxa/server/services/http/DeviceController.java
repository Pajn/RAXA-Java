package se.raxa.server.services.http;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.restexpress.Request;
import org.restexpress.Response;
import org.bson.types.ObjectId;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import se.raxa.server.Database;
import se.raxa.server.devices.Device;
import se.raxa.server.devices.Executable;
import se.raxa.server.devices.helpers.Devices;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.ExecutionException;
import se.raxa.server.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rasmus Eneman
 */
@SuppressWarnings("UnusedDeclaration") // As reflection is used this is worthless
public class DeviceController {

    public List<Map<String, Object>> read(Request request, Response response) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            String type = request.getHeader("type");
            for (Device device : Devices.getDevicesByType(type)) {
                list.add(device.describe());
            }
        } catch (Exception e) { //TODO Fix error handling
            response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            response.setException(e);
            e.printStackTrace();
        }
        return list;
    }

    public Map<String, Object> readSingle(Request request, Response response) {
        String id = request.getHeader("deviceID", "no id provided");
        try {
            return Devices.getDeviceById(new ObjectId(id)).describe();
        } catch (NotFoundException e) {
            response.setResponseStatus(HttpResponseStatus.NOT_FOUND);
            return null;
        } catch (Exception e) { //TODO Fix error handling
            response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            response.setException(e);
            e.printStackTrace();
            return null;
        }
    }

    public Object create(Request request, Response response) {
        String type = request.getHeader("type");
        String name = request.getHeader("name");
        Map<String, String> kwargs = new HashMap<>();

        for (String header : request.getHeaderNames()) {
            kwargs.put(header, request.getHeader(header));
        }

        try {
            Device device = Devices.createDeviceOfType(type, name, kwargs);
            device.save();

            response.setResponseCreated();
            return device.describe();
        } catch (IllegalArgumentException e) {
            response.setResponseStatus(HttpResponseStatus.BAD_REQUEST);
            return e.getMessage();
        } catch (ClassCreationException e) { //TODO Fix error handling
            response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            response.setException(e);
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public Map<String, Object> update(Request request, Response response) {
        String id = request.getHeader("deviceID", "no id provided");
        Map<String, String> kwargs = new HashMap<>();

        for (String header : request.getHeaderNames()) {
            kwargs.put(header, request.getHeader(header));
        }

        try {
            Device device =  Devices.getDeviceById(new ObjectId(id));
            device.onUpdate(kwargs);
            device.save();
            return device.describe();
        } catch (NotFoundException e) {
            response.setResponseStatus(HttpResponseStatus.NOT_FOUND);
            return null;
        } catch (ClassCreationException e) { //TODO Fix error handling
            response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            response.setException(e);
            e.printStackTrace();
            return null;
        }
    }

    public void delete(Request request, Response response) {
        String id = request.getHeader("deviceID", "no id provided");

        DBObject query = new BasicDBObject("_id", new ObjectId(id));
        Database.devices().remove(query);

        response.setResponseNoContent();
    }

    public void execute(Request request, Response response) {
        String id = request.getHeader("deviceID", "no id provided");
        String action = request.getHeader("action", "no action provided");
        try {
            Device device = Devices.getDeviceById(new ObjectId(id));
            if (device instanceof Executable) {
                ((Executable) device).execute(action);
            } else {
                throw new IllegalArgumentException("This device isn't executable");
            }
        } catch (NotFoundException e) {
            response.setResponseStatus(HttpResponseStatus.NOT_FOUND);
        } catch (ExecutionException | ClassCreationException e) { //TODO Fix error handling
            response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            response.setException(e);
            e.printStackTrace();
        }
    }
}
