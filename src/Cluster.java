import java.util.*;

public class Cluster {
	List<String> docs; // The documents in this cluster
	HashMap<String, HashSet<String>> inverted_index;
	int total_docs;
    private static int id_counter = 0;
    int id;

	public Cluster(String doc, HashMap<String, HashSet<String>> inverted_index, int total_docs){
        List<String> list = new ArrayList<String>();
        list.add(doc);
        this.docs = list;
        this.inverted_index = inverted_index;
        this.total_docs = total_docs;
        this.id = id_counter;
        id_counter ++;
	}

    // Returns the maximum atc score in this cluster
    public double max_score(HashMap<String, Double> doc_atc){
        double max = -1;
            for(String doc: docs){
                if(doc_atc.get(doc)>max){
                    max = doc_atc.get(doc);
                }
            }
        return max;
    }

    // Cluster distance between two clusters
    // Distance = the maximum distance between two documents in the cluster
    public double distance(Cluster other){
        double max = -1;
        // Look through this cluster's documents
        for(int i=0;i<docs.size();i++){

            // Look through the other cluster's documents
            for(int j=0;j<other.docs.size();j++){

                // If the documents are different, calculate distance
                if (!docs.get(i).equals(other.docs.get(j))){

                    DocumentPair pair = new DocumentPair(
                            docs.get(i), other.docs.get(j), inverted_index,
                            total_docs);

                    double score = pair.getDistance();
                    if (score > max){
                        max = score;
                    }
                }
            }
        }
        return max;
    }
}
