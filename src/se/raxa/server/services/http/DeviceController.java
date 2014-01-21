package se.raxa.server.services.http;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.restexpress.Request;
import org.restexpress.Response;
import se.raxa.server.Database;
import se.raxa.server.devices.Device;
import se.raxa.server.devices.Executable;
import se.raxa.server.devices.helpers.Devices;
import se.raxa.server.exceptions.BadPluginException;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.ExecutionException;
import se.raxa.server.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Rasmus Eneman
 */
@SuppressWarnings("UnusedDeclaration") // As reflection is used this is worthless
class DeviceController {
    private static final Logger LOGGER = Logger.getLogger(DeviceController.class.getName());

    public List<Map<String, Object>> read(Request request, Response response) {
        List<Map<String, Object>> list = new ArrayList<>();
        String type = request.getHeader("type");
        try {
            for (Device device : Devices.getDevicesByType(type)) {
                list.add(device.read());
            }
        } catch (Exception e) {
            response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            response.setException(e);
            LOGGER.log(Level.WARNING, String.format("Error getting devices by type \"%s\"", type), e);
        }
        return list;
    }

    public Map<String, Object> readSingle(Request request, Response response) {
        String id = request.getHeader("deviceID", "no id provided");
        try {
            return Devices.getDeviceById(new ObjectId(id)).read();
        } catch (NotFoundException e) {
            response.setResponseStatus(HttpResponseStatus.NOT_FOUND);
        } catch (ClassCreationException | BadPluginException e) {
            response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            response.setException(e);
            LOGGER.log(Level.WARNING, String.format("Error getting device by id \"%s\"", id), e);
        }
        return null;
    }

    public Object create(Request request, Response response) {
        String type = request.getHeader("type", "no type provided");
        Map<String, String> propertyValues = new HashMap<>();

        for (String header : request.getHeaderNames()) {
            propertyValues.put(header, request.getHeader(header));
        }

        try {
            Device device = Devices.createDeviceOfType(type, propertyValues);
            device.save();

            response.setResponseCreated();
            return device.read();
        } catch (IllegalArgumentException e) {
            response.setResponseStatus(HttpResponseStatus.BAD_REQUEST);
            return e.getMessage();
        } catch (ClassCreationException | BadPluginException e) {
            response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            response.setException(e);
            LOGGER.log(Level.WARNING, String.format("Error creating device by type \"%s\"", type), e);
            return e.getMessage();
        }
    }

    public Map<String, Object> update(Request request, Response response) {
        String id = request.getHeader("deviceID", "no id provided");
        Map<String, String> propertyValues = new HashMap<>();

        for (String header : request.getHeaderNames()) {
            propertyValues.put(header, request.getHeader(header));
        }

        try {
            Device device =  Devices.getDeviceById(new ObjectId(id));
            device.update(propertyValues);
            device.save();
            return device.read();
        } catch (NotFoundException e) {
            response.setResponseStatus(HttpResponseStatus.NOT_FOUND);
            return null;
        } catch (ClassCreationException | BadPluginException e) {
            response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            response.setException(e);
            LOGGER.log(Level.WARNING, String.format("Error getting device by id \"%s\"", id), e);
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
        } catch (ExecutionException e) {
            response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            response.setException(e);
            LOGGER.log(Level.WARNING, String.format("Error executing action \"%s\" on device by id \"%s\"", action, id), e);
        } catch (ClassCreationException e) {
            response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            response.setException(e);
            LOGGER.log(Level.WARNING, String.format("Error getting device by id \"%s\"", id), e);
        }
    }
}
