package se.raxa.server.devices.groups;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import se.raxa.server.Database;
import se.raxa.server.devices.Device;
import se.raxa.server.devices.helpers.Member;
import se.raxa.server.exceptions.ClassCreationException;
import se.raxa.server.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Rasmus Eneman
 */
public interface Group<T extends Device> extends Device {

    /**
     * @return A collection of all members in the group
     */
    public default Iterable<Member<T>> getMembers() {
        Collection<Member<T>> members = new ArrayList<>();

        for (Object object : (Collection) getDBObj().get("members")) {
            try {
                members.add(new Member<>((DBObject) object));
            } catch (NotFoundException e) {
                DBObject query = new BasicDBObject("_id", ((DBObject) object).get("id"));
                Database.devices().update(query, new BasicDBObject("$pull", new BasicDBObject("members", object)));
                //TODO log?
            } catch (ClassCreationException e) {
                //TODO log?, remove?
            }
        }
        return members;
    }

    public default void addMember(Member<T> member) {
        DBObject query = new BasicDBObject("_id", member.getDevice().getId());
        Database.devices().update(query, new BasicDBObject("$push", new BasicDBObject("members", member.getDbObject())));
    }

    public default void addMember(T device) {
        addMember(new Member<T>(device));
    }
}
