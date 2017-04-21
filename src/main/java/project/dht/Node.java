package project.dht;

import project.GlobalConfiguration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    private static class FingerTable {
        List<FingerEntry> fingerEntries = new LinkedList<>();
    }

    private static class FingerEntry {
        private Identifier start = new Identifier();
        private Identifier succ = new Identifier();
    }

    public void join() {

    }

    private void initFingerTable() {

    }

    private Identifier closestPrecedingFinger(Identifier id) {
        for (int i = fingerEntryNum; i >= 1; i--) {
            Identifier entry = fingerTable.fingerEntries.get(i).succ;
            if (entry.compareTo(this.identifier) > 0 && entry.compareTo(id) <= 0)
                return entry;
        }
        return this.identifier;
    }

}
