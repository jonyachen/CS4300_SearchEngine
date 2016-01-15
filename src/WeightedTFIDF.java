import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.function.valuesource.NumDocsValueSource;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.Token;
import org.apache.lucene.search.Query;

/*
 * Represents TF-IDF of one query - document pair
 */
public class WeightedTFIDF {
	// Data that is needed to do calculations
	private String queryString;
	private String freqString;
	private String database;
	private HashMap<String, Double> idfMap;
	private Analyzer analyzer;
	
	// BM25 stuff
	private int doc_length;
	
	 // These are static PER DATABASE -- reset by calling createInvertCount() and createIDFMapping()
	private static Double avdl = 0.0;
	public static int total_docs = 0;
	private static HashMap<String, Integer> numDocPerTerm; // Maps Term -> occurrance in all documents
	
	// Data that is the result of calculations
	HashMap<String, Double> augTFDoc;
	HashMap<String, Integer> tfDoc;  // Term -> freq in THIS document
	HashMap<String, Integer> binTFQuery;

	public WeightedTFIDF(String queryString, String freqString,
			HashMap<String, Double> idfMap, CharArraySet stopWords) {
		this.queryString = queryString;
		this.freqString = freqString;
		this.idfMap = idfMap;
		this.analyzer = new MyAnalyzer(stopWords);
		calculateAugTFDoc();
		calculateBinaryTFQuery();
	}

	// Sets the AugTF value
	// 0.5 + (0.5 * freq / max(freq of any word in document))
	private void calculateAugTFDoc() {

		augTFDoc = new HashMap<String, Double>();
		HashMap<String, Integer> count = new HashMap<String, Integer>(); // Term to Count in this document
		String[] words = freqString.split(","); // the document mapping from the
												// database
		int max = -1;
		// Stores word -> freq pairs from the database read
		for (int i = 1; i < words.length; i++) {
			if (!words[i].contains("&&"))
				continue;

			int freq = Integer.parseInt(words[i].trim().split("&&")[1]);
			doc_length += freq;
			String word = words[i].trim().split("&&")[0];

			if (freq > max) {
				max = freq;
			}
			count.put(word, freq);
		}

		// Calculating augmented tf values
		for (String word : count.keySet()) {
			Double value = 0.5 + (0.5 * count.get(word)) / (1.0 * max);
			augTFDoc.put(word, value);
		}
		tfDoc = count;
	}

	// Sets the BinTF value for part 3c
	private void calculateBinaryTFQuery() {
		binTFQuery = new HashMap<String, Integer>();
		QueryParser parser = new QueryParser("contents", analyzer);
		Query query = null;
		HashSet<Term> terms = new HashSet<Term>();

		try {
			query = parser.parse(QueryParser.escape(queryString));
			query.extractTerms(terms);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Term t : terms) {
			String key = t.text();

			// Give a value of 1 to a term that has been seen before in the
			// augmented document search
			if (augTFDoc.containsKey(key)) {
				binTFQuery.put(key, 1);
			} else {
				binTFQuery.put(key, 0);
			}

		}
	}

	public Double getBM25(){
		double BM25 = 0.0;
		double b = 0.75;
		double k1 = 1.2;
		double k2 = 100;
		// No relevance information
		double r = 0;
		double R = 0;
		 
		// Parse the query
		QueryParser parser = new QueryParser("contents", analyzer);
		Query query = null;
		HashSet<Term> terms = new HashSet<Term>();

		try {
			query = parser.parse(QueryParser.escape(queryString));
			query.extractTerms(terms);
		} catch (ParseException e) {
			System.out.println("Issue with Normalize Cosine");
		}
		
		// Look in query terms
		for(Term t: terms){
			double n = (numDocPerTerm.get(t.text()) != null) ? numDocPerTerm.get(t.text()) : 0;
			double f = (tfDoc.get(t.text()) != null	 ? tfDoc.get(t.text()): 0);
			double gfi = 1.0 / terms.size();
			

			double K = k1 * ((1 - b) + b * (doc_length / avdl));
			BM25 += (Math.log10(((r + .5) / (R - r + .5))
					/ ((n - r + .5) / (total_docs - n - R + r + .5))))
					* ((k1 + 1) * f / (K + f)) * ((k2 + 1) * gfi / (k2 + gfi));
		}
		return BM25;
	}
	
	public Double getCosineSimilarity_NormalizeFirst(HashMap<String, HashSet<String>> inverted_index) {
		double result = 0.0;
		
		// Parse the queyr
		QueryParser parser = new QueryParser("contents", analyzer);
		Query query = null;
		HashSet<Term> terms = new HashSet<Term>();

		try {
			query = parser.parse(QueryParser.escape(queryString));
			query.extractTerms(terms);
		} catch (ParseException e) {
			System.out.println("Issue with Normalize Cosine");
		}

		// Get normalize terms for query as well as the numerator for the calculation
		Double query_normalize = 0.0;
		Double doc_normalize = 0.0;
		
		for (Term t : terms) {
			// The query term is in the document. Only these terms can
			// contribute to the overall cosine score
			if (augTFDoc.containsKey(t.text())) {
				double score =  augTFDoc.get(t.text()) * Math.pow(calculateIDF(inverted_index, t.text()),2);
				
				result += score;
				query_normalize += Math.pow(calculateIDF(inverted_index, t.text()), 2);
				doc_normalize += Math.pow(augTFDoc.get(t.text()) * calculateIDF(inverted_index, t.text()), 2);
			}
		}
		
		if(result == 0.0){
			return 0.0;
		}
		
		query_normalize = Math.sqrt(query_normalize);
		doc_normalize = doc_normalize();

		return (result)/(doc_normalize);
	}
	
	public int getTermFreqInQuery(String text){
		int count = 0;
		for(String i: queryString.split(" ")){
			if(i.trim().contains(text))
				count ++;
		}
		return count;
	}
	
	public Double getCosineSimilarity_NoNormalize() {
		Double result = 0.0;
		
		// Parse the queyr
		QueryParser parser = new QueryParser("contents", analyzer);
		Query query = null;
		HashSet<Term> terms = new HashSet<Term>();

		try {
			query = parser.parse(QueryParser.escape(queryString));
			query.extractTerms(terms);
		} catch (ParseException e) {
			System.out.println("Issue with Normalize Cosine");
		}

		// Get normalize terms for query as well as the numerator for the calculation
		Double query_normalize = 0.0;
		Double doc_normalize = doc_normalize();
		for (Term t : terms) {
			// The query term is in the document. Only these terms can
			// contribute to the overall cosine score
			if (augTFDoc.containsKey(t.text())) {
				Double tfidf = augTFDoc.get(t.text()) * idfMap.get(t.text());
				result += Math.pow(tfidf, 2);
				query_normalize += Math.pow(tfidf, 2);
			}
		}
		
		if(result == 0)
			return 0.0;
		query_normalize = Math.sqrt(query_normalize);

		return (result);
	}
	
	public Double getWeighting_Similarity() {
		Double result = 0.0;
		
		// Parse the query
		QueryParser parser = new QueryParser("contents", analyzer);
		Query query = null;
		HashSet<Term> terms = new HashSet<Term>();

		try {
			query = parser.parse(QueryParser.escape(queryString));
			query.extractTerms(terms);
		} catch (ParseException e) {
			System.out.println("Issue with Normalize Cosine");
		}

		// Get normalize terms for query as well as the numerator for the calculation
		for (Term t : terms) {
			// The query term is in the document. Only these terms can
			// contribute to the overall cosine score
			if (augTFDoc.containsKey(t.text())) {
				// Normal aug.tf * idf * (bintf * max idf)
				Double tfidf = augTFDoc.get(t.text()) * idfMap.get(t.text())* binTFQuery.get(t.text())*Double.max(idfMap.get(t.text()), 0);
				result += tfidf;
			}
		}
		
		if(result == 0)
			return 0.0;

		return (result);
	}
	
	private Double doc_normalize(){
		Double doc_normalize = 0.0;
		// Get the normalized length value for the document
		for(String key: augTFDoc.keySet()){
			doc_normalize += Math.pow(augTFDoc.get(key)* idfMap.get(key), 2);
		}
		
		doc_normalize = Math.sqrt(doc_normalize);
		return doc_normalize;
	}

	// Returns a hashmap that maps term -> idf value
	// IDF = log(N/n), where N is total documents, n is occurrance

	public static HashMap<String, Double> createIDFMapping(String database) {
		HashMap<String, Integer> occurrance = new HashMap<String, Integer>();
		Double total = 0.0;
		HashMap<String, Double> toReturn = new HashMap<String, Double>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(database));
			String line = null;

			// Looks through all the documents' frequency mapping
			while ((line = reader.readLine()) != null) {
				total++;
				String[] words = line.trim().split(",");

				// Ignore first element
				// Looks through each term in a document
				for (int i = 1; i < words.length; i++) {
					String word = words[i].split("&&")[0]; // Gets the word
					int count = 1;

					if (occurrance.containsKey(word)) {
						count = occurrance.get(word) + 1;
					}
					occurrance.put(word, count);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("FAILING IN WEIGHTED TFIDF");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("FAILING IN WEIGHTED TFIDF");
		}

		// Convert from occurance to log(N/n)
		for (String key : occurrance.keySet()) {
			toReturn.put(key, Math.log10(total / occurrance.get(key)));
		}

		numDocPerTerm = occurrance;
		return toReturn;
	}
	
	public static HashMap<String, Integer> createInvertCount(String database) {
		HashMap<String, Integer> occurrance = new HashMap<String, Integer>();
		Double total_doc_length = 0.0;
		int sum = 0;	// Number of files in this database

		try {
			BufferedReader reader = new BufferedReader(new FileReader(database));
			String line = null;

			// Looks through all the documents' frequency mapping
			while ((line = reader.readLine()) != null) {
				sum++;
				String[] words = line.trim().split(",");

				// Ignore first element
				// Looks through each term in a document
				for (int i = 1; i < words.length; i++) {
					String word = words[i].split("&&")[0]; // Gets the word
					int count = 1;
					
					if(words[i].contains("&&")){
						total_doc_length += 
								Double.parseDouble(words[i].split("&&")[1]);
					}

					if (occurrance.containsKey(word)) {
						count = occurrance.get(word) + 1;
					}
					occurrance.put(word, count);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("FAILING IN WEIGHTED TFIDF");
		} catch (IOException e) {
			System.out.println("FAILING IN WEIGHTED TFIDF");
		}

		avdl = total_doc_length/sum;
		if(total_docs == 0)
			total_docs = sum;
		return occurrance;
	}

	// Here starts the new code for Project 2
	
	public static HashMap<String, HashSet<String>> createInvertedIndex(String database){
		HashMap<String, HashSet<String>> invert = new HashMap<String, HashSet<String>>();
		int total = 0;
		System.out.println(" Creating data base: " + database);
		try{
			BufferedReader reader = new BufferedReader(new FileReader(database));
			String line = null;
			
			while((line = reader.readLine()) != null){
				total ++;
				String[] words = line.trim().split(",");
				String doc = words[0];
				
				for(int i=1;i<words.length;i++){
					String word = words[i].split("&&")[0];
					if(!invert.containsKey(word)){
						HashSet<String> value = new HashSet<String>();
						value.add(doc);
						invert.put(word, value);
					}else{
						HashSet<String> value = invert.get(word);
						value.add(doc);
					}
				}
			}
			
			if(total_docs == 0){
				total_docs = total;
			}
			
		}catch (FileNotFoundException e) {
			System.out.println("FAILING IN WEIGHTED TFIDF Inverted Index");
		} catch (IOException e) {
			System.out.println("FAILING IN WEIGHTED TFIDF Inverted Index");
		}

		return invert;
	}
	
	public static double calculateIDF(HashMap<String, HashSet<String>> invertedIndex, String term){
		return Math.log10(((double)total_docs)/((double)invertedIndex.get(term).size()));
	}
}
