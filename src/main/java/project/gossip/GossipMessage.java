package project.gossip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mio on 2017/5/2.
 */
public class GossipMessage {

    private List<NodeInfo> nodeInfoList;
    private long timestamp;

    public GossipMessage() {
        nodeInfoList = new ArrayList<>();
        timestamp = System.currentTimeMillis();
    }

    public void addNodeInfo(NodeInfo info) {
        nodeInfoList.add(info);
    }

    @Override
    public String toString() {
        return "GossipMessage{" +
                "nodeInfoList=" + nodeInfoList +
                ", timestamp=" + timestamp +
                '}';
    }
}
