import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;


public class DocumentPair {

	String doc1;
	String doc2;
    int total_docs;
	HashMap<String, HashSet<String>> inverted_index;
    HashMap<String, Double> tf1; // augmented TF1 values for doc 1
    HashMap<String, Double> tf2; // augmented TF2 values for doc 2

	public DocumentPair(String doc1, String doc2,
            HashMap<String, HashSet<String>> inverted_index, int total_docs){
		this.doc1 = doc1;
		this.doc2 = doc2;
		this.inverted_index = inverted_index;
        this.total_docs = total_docs;
        setUpTF1();
        setUpTF2();
	}

    private void setUpTF1(){
        HashMap<String, Double> res = new HashMap<String, Double>();
        HashMap<String, Integer> count = new HashMap<String, Integer>();
        String[] words = doc1.split(",");
        int max = -1;

        for(int i =1; i<words.length; i++){
            if(!words[i].contains("&&")){
                continue;
            }

            int freq = Integer.parseInt(words[i].trim().split("&&")[1]);
            String word = words[i].trim().split("&&")[0];

            if(freq > max){
                max = freq;
            }
            count.put(word, freq);
        }

        // augmented tf values
        for (String word : count.keySet()){
            double value = 0.5 + (0.5* count.get(word))/ (1.0*max);
            res.put(word, value);
        }
        tf1 = res;
    }
    private void setUpTF2(){
        HashMap<String, Double> res = new HashMap<String, Double>();
        HashMap<String, Integer> count = new HashMap<String, Integer>();
        String[] words = doc2.split(",");
        int max = -1;

        for(int i =1; i<words.length; i++){
            if(!words[i].contains("&&")){
                continue;
            }

            int freq = Integer.parseInt(words[i].trim().split("&&")[1]);
            String word = words[i].trim().split("&&")[0];

            if(freq > max){
                max = freq;
            }
            count.put(word, freq);
        }

        // augmented tf values
        for (String word : count.keySet()){
            double value = 0.5 + (0.5* count.get(word))/ (1.0*max);
            res.put(word, value);
        }
        tf2 = res;
    }

    public double calculateIDF(HashMap<String, HashSet<String>> inverted_index,
            String term){
        return Math.log10(((double)total_docs)/((double)inverted_index.get(term).
                    size()));
    }

    private double doc_normalizeDoc1(){
        double doc_normalize = 0.0;
        for(String key : tf1.keySet()){
            doc_normalize += Math.pow(tf1.get(key) *
                    calculateIDF(inverted_index, key), 2);
        }
        doc_normalize = Math.sqrt(doc_normalize);
        return doc_normalize;
    }

    private double doc_normalizeDoc2(){
        double doc_normalize = 0.0;
        for(String key : tf2.keySet()){
            doc_normalize += Math.pow(tf2.get(key) *
                    calculateIDF(inverted_index, key), 2);
        }
        doc_normalize = Math.sqrt(doc_normalize);
        return doc_normalize;
    }

    public double getDistance(){
        double cosine = getCosineSimilarity();

        if(cosine == 0){
            //System.out.println("Facing zero similarity between docs " +
                    //doc1 + " \n " + doc2 +"\n");
            return Double.MAX_VALUE;
        }
        return (1.0)/cosine;
    }

	public double getCosineSimilarity(){
		double result = 0;
		List<String> terms1 = new ArrayList<String>();
		List<String> terms2 = new ArrayList<String>();

        // Get the words in doc 1
        for(String text: doc1.split(",")){
            if(text.contains("&&")){
                String word = text.split("&&")[0];
                terms1.add(word);
            }
        }

        // Get the words in doc 2
        for(String text: doc2.split(",")){
            if(text.contains("&&")){
                String word = text.split("&&")[0];
                terms2.add(word);
            }
        }

        // Calculate similarity
        for(String term : terms1){
            if(terms2.contains(term)){
                double score = tf1.get(term)*tf2.get(term)*
                    Math.pow(calculateIDF(inverted_index, term),2);
                result += score;
            }
        }

        if(result == 0)
            return 0.0;

		return result/doc_normalizeDoc1()/doc_normalizeDoc2();
	}

}
