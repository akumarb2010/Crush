/*
 * @author : AnilKumar
 */

package rush.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Cluster {

	private static Map<Integer, SubCluster> subClusters = new HashMap<Integer, SubCluster>();

	public static final long maximumReplication = 3;

	public static void addSubCluster(SubCluster c) {
		assert subClusters.get(0).size() > maximumReplication;
		subClusters.put(c.getID(), c);
	}

	public static void removeSubCluster(SubCluster c) {
		subClusters.remove(c);
	}

	public static int size() {
		return subClusters.size();
	}

	public static SubCluster getSubCluster(int subClusterId) {
		return subClusters.get(subClusterId);
	}

	public static int getAllServersAvailable(int subClusterID) {
		int noOfServers = 0;
		Set<Entry<Integer, SubCluster>> entrySet = subClusters.entrySet();
		for (Entry<Integer, SubCluster> e : entrySet) {
			if (e.getKey() < subClusterID) {
				noOfServers += e.getValue().size();
			}
		}
		return noOfServers;
	}

	public static int weight(long subClusterId) {
		int totalweight = 0;
		Set<Entry<Integer, SubCluster>> entrySet = subClusters.entrySet();
		for (Entry<Integer, SubCluster> e : entrySet) {
			if (e.getKey() < subClusterId) {
				totalweight += e.getValue().weight();
			}
		}
		return totalweight;
	}

	public static long getNoOfServers() {
		long size = 0;
		for (SubCluster sc : subClusters.values()) {
			size += sc.size();
		}
		return size;
	}
}