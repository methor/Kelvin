package project.dht;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Optional;

import java.io.IOException;

/**
 * Created by Mio on 2017/4/20.
 */
public class RemoteRequest {
    public enum Type {
        FIND_SUCCESSOR, FIND_PREDECESSOR, CLOSEST_PRECEDING_FINGER, NOTIFY
    }

    public Type type;
    public Identifier identifier;

    @JsonCreator
    public RemoteRequest(@JsonProperty("type") Type t, @JsonProperty("identifier") Identifier id) {
        type = t;
        identifier = id;
    }

    @JsonIgnore
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public Optional<String> serializeToJson() {
        try {
            return Optional.fromNullable(mapper.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Optional.absent();
    }

    public static Optional<RemoteRequest> deserializeFromJson(String json) {
        try {
            return Optional.fromNullable(mapper.readValue(json, RemoteRequest.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.absent();
    }

    @Override
    public String toString() {
        return "RemoteRequest{" +
                "type=" + type +
                ", identifier=" + identifier +
                '}';
    }
}
