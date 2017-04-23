package project.dht;

import io.netty.channel.Channel;
import project.GlobalConfiguration;
import project.network.BlockingResponseHandler;
import project.network.Channels;

import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mio on 2017/4/20.
 */
public class Node {
    private Identifier identifier = new Identifier();
    private Identifier pred = new Identifier();
    private Identifier succ = new Identifier();
    private FingerTable fingerTable = new FingerTable();
    private List<Identifier> succList = new ArrayList<>();
    private final int fingerEntryNum = GlobalConfiguration.getInstance().getHashSize();
    private Map<Identifier, InetAddress> identifierMap = new ConcurrentHashMap<>();

    private static class FingerTable {
        List<FingerEntry> fingerEntries = new LinkedList<>();
    }

    private static class FingerEntry {
        private Identifier start = new Identifier();
        private Identifier succ = new Identifier();
    }

    public void join(Identifier nprime) {


    }

    private void initFingerTable() {

    }

    public Identifier findSuccessor(Identifier id) {
        Identifier nprime = findPredecessor(id);
        return successor(nprime);
    }

    private Identifier closestPrecedingFinger(Identifier n, Identifier id) {
        if (n.equals(this.identifier)) {
            for (int i = fingerEntryNum; i >= 1; i--) {
                Identifier entry = fingerTable.fingerEntries.get(i).succ;
                if (entry.compareTo(this.identifier) > 0 && entry.compareTo(id) <= 0)
                    return entry;
            }
            return this.identifier;
        }
        else {
            try {
                Channel channel = Channels.connect(identifierMap.get(n),
                        GlobalConfiguration.getInstance().getGossipPort()).sync().channel();
                channel.writeAndFlush(new RemoteRequest(RemoteRequest.Type.CLOSEST_PRECEDING_FINGER, n));
                Object response =
                        channel.attr(BlockingResponseHandler.QUEUE).get().poll(50, TimeUnit.MICROSECONDS);
                channel.close();
                RemoteIdentifierReply reply = RemoteIdentifierReply.deserializeFromJson
                        ((String)response);
                if (reply == null)
                    return null;
                else {
                    identifierMap.put(reply.identifier, reply.address);
                    return reply.identifier;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Identifier findPredecessor(Identifier id) {

        Identifier nprime = new Identifier(this.identifier);
        while (id.compareTo(nprime) <= 0 || id.compareTo(successor(nprime)) > 0)
            nprime = closestPrecedingFinger(nprime, id);
        return nprime;
    }

    private Identifier successor(Identifier n) {
        if (n.equals(this.identifier))
            return succ;
        try {
            Channel channel = Channels.connect(identifierMap.get(n),
                    GlobalConfiguration.getInstance().getGossipPort()).sync().channel();
            channel.writeAndFlush(new RemoteRequest(RemoteRequest.Type.SUCCESSOR, n));
            Object response =
                    channel.attr(BlockingResponseHandler.QUEUE).get().poll(50, TimeUnit.MICROSECONDS);
            channel.close();
            RemoteIdentifierReply reply = RemoteIdentifierReply.deserializeFromJson
                    ((String)response);
            if (reply == null)
                return null;
            else {
                identifierMap.put(reply.identifier, reply.address);
                return reply.identifier;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;

    }

}
