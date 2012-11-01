As of now We have implemented RUSHP, RUSHT and also implemented RUSHR partially.

Now the main goals are Tuning the algorithms(like tuning the hash function which is the heart of the algo for uniform distribution).

About RUSHP: The following points are implemented 1) Implemented RUSHP algorithm, to find the appropriate sub-cluster and DiskServer for the given object(Object ID, replication).

Still needs tuning: 1) Hashing need to be tuned.

About RUSHT: The following points are implemented 1) RUSH tree contains all the sub-clusters as leaf nodes and remaining all intermediate nodes are dummy. 2) This tree grows dynamically for each sub-cluster addition and always remains as "almost complete binary tree". 3) Index of each node will be always same. 4) Implemented RUSHT algorithm, to find the appropriate sub-cluster and DiskServer for the given object(Object ID, replication). (Note: The beauty of rust tree is, at each insertion of sub-cluster node should go to leaf and middle of the path, It should keep all the dummy nodes. And every time it should be balanced, root always keeps changing.)

Still needs tuning: 1) Replication is not taking care 2) Hashing need to be tuned.

About RUSHR: 1) Hyper Geometric Distribution need to be analyzed.

References: http://www.ssrc.ucsc.edu/Papers/weil-sc06.pdf http://saluc.engr.uconn.edu/refs/algorithms/hashalg/gauravaram04crush.pdf