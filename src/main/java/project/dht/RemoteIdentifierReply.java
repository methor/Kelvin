package project.dht;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import project.JsonMessage;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by mio on 2017/4/23.
 */
public class RemoteIdentifierReply implements JsonMessage {
    public Identifier identifier;
    public InetAddress address;

    @JsonCreator
    public RemoteIdentifierReply(@JsonProperty("identifier") Identifier id,
                                 @JsonProperty("address") InetAddress address) {
        identifier = id;
        this.address = address;
    }

    @JsonIgnore
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public String serializeToJson() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RemoteIdentifierReply deserializeFromJson(String json) {
        try {
            return mapper.readValue(json, RemoteIdentifierReply.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "RemoteIdentifierReply{" +
                "identifier=" + identifier +
                ", address=" + address +
                '}';
    }
}
