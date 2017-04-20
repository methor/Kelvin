package project.dht;

/**
 * Created by Mio on 2017/4/20.
 */
public class RemoteRequest {
    public enum Type {
        FIND_SUCCESSOR, FIND_PREDECESSOR, CLOSEST_PRECEDING_FINGER, NOTIFY
    }

    Type type;
    Identifier identifier;

    public String serializeToJson() {
        return "";
    }

    public static RemoteRequest deserializeFromJson(String json) {
        return new RemoteRequest();
    }
}
