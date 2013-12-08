package se.raxa.server.devices.groups;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import se.raxa.server.Database;
import se.raxa.server.devices.Executable;
import se.raxa.server.devices.helpers.AbstractDevice;
import se.raxa.server.devices.helpers.Member;
import se.raxa.server.exceptions.ExecutionException;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Rasmus Eneman
 */
public class Scenario extends AbstractDevice implements Group<Executable>, Executable {
    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();

    /**
     * @return An array of types, ordered by position in tree
     */
    @Override
    public String[] getType() {
        return new String[] {"Scenario", "Executable", "Group"};
    }

    /**
     * Executes the Scenario
     *
     * @param action Ignored
     */
    public void execute(Object action) throws ExecutionException {
        EXECUTOR.execute(() -> {
            for (Member member : getMembers()) {
                try {
                    ((Executable) member.getDevice()).execute(member.get("action"));
                } catch (ExecutionException e) {
                    //TODO log
                }
            }
        });
    }

    public void addMember(Executable device, Object action) {
        Member member = new Member<>(device);
        member.put("action", action);
        addMember(member);

        DBObject query = new BasicDBObject("_id", getId());
        Database.devices().update(query, new BasicDBObject("$push", new BasicDBObject("members", member.getDbObject())));
    }
}