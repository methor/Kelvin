package project.gossip;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import project.JsonMessage;
import project.dht.Identifier;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mio on 2017/5/2.
 */
public class NodeInfo implements JsonMessage {

    public InetAddress address;
    public Identifier identifier;
    public Map<Identifier, Identifier> replicaRanges;

    @JsonIgnore
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public NodeInfo() {
        replicaRanges = new HashMap<>();
    }

    public NodeInfo(InetAddress address, Identifier identifier,
                    Map<Identifier, Identifier> replicaRanges) {
        this.address = address;
        this.identifier = identifier;
        this.replicaRanges = replicaRanges;
    }

    public void addReplicaRange(Identifier start, Identifier end) {
        replicaRanges.put(start, end);
    }

    @Override
    public String toString() {
        return "NodeInfo{" +
                "address=" + address +
                ", identifier=" + identifier +
                ", replicaRanges=" + replicaRanges +
                '}';
    }

    @Override
    public String serializeToJson() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static NodeInfo deserializeFromJson(String json) {
        try {
            return mapper.readValue(json, NodeInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
