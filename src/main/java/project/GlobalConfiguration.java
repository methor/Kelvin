package project;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mio on 2017/4/18.
 */
public class GlobalConfiguration {

    private String cluster;
    private int data_port;
    private int gossip_port;
    private int replica_port;
    private List<String> seeds;
    private List<InetAddress> seedsAddrs;
    private File data_file_dir;
    private int hash_size = 160;

    public String getClusterName() {
        return cluster;
    }

    public int getDataPort() {
        return data_port;
    }

    public int getGossipPort() {
        return gossip_port;
    }

    public int getReplicaPort() {
        return replica_port;
    }

    public List<InetAddress> getSeedsAddrs() {
        return new ArrayList<>(seedsAddrs);
    }

    public File getDataFileDir() {
        return new File(data_file_dir.getPath());
    }

    public int getHashSize() {return hash_size;}


    private GlobalConfiguration() {
        seeds = new ArrayList<>();
        seedsAddrs = new ArrayList<>();
    }

    private static class Holder {
        private static GlobalConfiguration INSTANCE;
    }

    public static GlobalConfiguration getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String toString()
    {
        return "GlobalConfiguration{" +
                "cluster='" + cluster + '\'' +
                ", data_port=" + data_port +
                ", gossip_port=" + gossip_port +
                ", replica_port=" + replica_port +
                ", seedsAddrs=" + seedsAddrs +
                ", data_file_dir=" + data_file_dir +
                ", hash_size=" + hash_size +
                '}';
    }

    public static void readConfig(String file) throws Exception {
        Yaml yaml = new Yaml();
        yaml.setBeanAccess(BeanAccess.FIELD);

        GlobalConfiguration conf = yaml.loadAs(new FileReader(file), GlobalConfiguration.class);
        Holder.INSTANCE = conf;
        System.out.println(conf);
        for (String seed : Holder.INSTANCE.seeds) {
            Holder.INSTANCE.seedsAddrs.add(InetAddress.getByName(seed));
        }


    }

}
