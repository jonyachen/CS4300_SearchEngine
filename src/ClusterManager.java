import java.util.*;

public class ClusterManager {
    List<Cluster> clusters;
    HashMap<String, HashSet<String>> inverted_index;
    HashMap<String, Double> doc_atc; // Maps from the document to the atc.atc score
    int total_docs;

    public ClusterManager(List<SearchFiles.Pair<String, Double>> input,
            HashMap<String, HashSet<String>> inverted_index){
        this.inverted_index = inverted_index;
        this.total_docs = WeightedTFIDF.total_docs;
        this.clusters = new ArrayList<Cluster>();
        this.doc_atc = new HashMap<String, Double>();

        for(int i=0;i<input.size();i++){
            String doc = input.get(i).x;

            Cluster newCluster = new Cluster(doc, inverted_index, total_docs);
            this.clusters.add(newCluster);

            doc_atc.put(doc, input.get(i).y);
        }
    }

    public List<SearchFiles.Pair<String, Double>> reorganizeFiles(){
        List<SearchFiles.Pair<String, Double>> pairs =
            new ArrayList<SearchFiles.Pair<String, Double>>();

        for(Cluster c:clusters){
            double score = c.max_score(doc_atc);

            for(String doc: c.docs){
                SearchFiles.Pair<String, Double> pair = new SearchFiles(). new Pair(doc, score);
                
                // Boost the score of the highest file just a little bit
                if(doc_atc.get(doc) == score){
                	pair.y += 0.0001;
                }
                pairs.add(pair);
            }
        }
    
        return pairs;
    }

    // Reduce the cluster size into this
    public void reduceClusterSize(int minSize){
        if(minSize >= clusters.size())
            return;

        // Reduce until it matches our size
        while(clusters.size() > minSize){

            double min = Double.MAX_VALUE;
            int c1_merge = -1;
            int c2_merge = -2;

            // Reduce cluster once
            for(int i=0;i<clusters.size();i++){
                for(int j=0;j<clusters.size();j++){

                    // If the clusters are not equal, see if the distance between
                    // those two clusters is the smallest available distance
                    if(i != j){
                        Cluster c1 = clusters.get(i);
                        Cluster c2 = clusters.get(j);
                        double score = c1.distance(c2);

                        // Update the minimum distance cluster
                        if(score < min){
                            c1_merge = i;
                            c2_merge = j;
                            min = score;
                        }
                    }
                }
            } // end big for loops

            if(c1_merge == -1 || c2_merge == -2){
            	mergeClusters(0,1);
            }else {
            	mergeClusters(c1_merge, c2_merge);
            }
        }
    }

    // Move all documents from cluster2 to cluster1
    public void mergeClusters(int loc1, int loc2){
        List<String> docs2 = clusters.get(loc2).docs;
        List<String> docs1 = clusters.get(loc1).docs;

        for(String doc: docs2){
            docs1.add(doc);
        }
        clusters.remove(loc2);
    }
}
