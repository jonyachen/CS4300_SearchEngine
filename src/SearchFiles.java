import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SearchFiles {

	SearchFiles() {}
	
	// Returns pairs of <Document, atc.atc> values
		public static List<Pair<String, Double>> searchQueryIncludeTFIDF(String queryString, String database, int numResults, 
				CharArraySet stopwords, int version, HashMap<String, HashSet<String>>inverted_index){
			List<Pair<String, Double>> docs = new ArrayList<Pair<String, Double>>(); //Filenames of the files that matched the search query
			List<WeightedTFIDF> tf_idf = new ArrayList<WeightedTFIDF>();	// To return
			HashMap<String, Double> idfMap = WeightedTFIDF.createIDFMapping(database); // Hashed Re-use
			
			BufferedReader reader = null;
			TreeSet<Pair<String, Double>> treeSet = new TreeSet(new SearchFiles().new PairComparator());
			int size = 0;
			
			try {
				reader = new BufferedReader(new FileReader(database));
				String line = null;
				
				// 3a
					while((line = reader.readLine()) != null){
						WeightedTFIDF queryToDoc = new WeightedTFIDF(queryString, line, idfMap, stopwords);
						// NOTE: The main difference is that this returns the WHOLE DOCUMENT which is matched, instead of just the document name
						Pair<String, Double> newPair = new SearchFiles(). new Pair(line, queryToDoc.getCosineSimilarity_NormalizeFirst(inverted_index));
						treeSet.add(newPair);
					}				
				
			} catch (FileNotFoundException e) {
				System.out.println("FAILURE IN SEARCH FILES 1");
			} catch (IOException e) {
				System.out.println("FAILURE IN SEARCH FILES 2");
			}

			for(int i = 0;i<numResults;i++){
				Pair<String, Double> pair = treeSet.pollFirst();
				docs.add(pair);
			}
			
			return docs;
		}
		
		// Returns pairs of <Document, atc.atc> values
		public static List<String> searchQueryIncludeFullFile(String queryString, String database, int numResults, 
				CharArraySet stopwords, int version, HashMap<String, HashSet<String>>inverted_index){
			List<String> docs = new ArrayList<String>(); //Filenames of the files that matched the search query
			List<WeightedTFIDF> tf_idf = new ArrayList<WeightedTFIDF>();	// To return
			HashMap<String, Double> idfMap = WeightedTFIDF.createIDFMapping(database); // Hashed Re-use
			
			BufferedReader reader = null;
			TreeSet<Pair<String, Double>> treeSet = new TreeSet(new SearchFiles().new PairComparator());
			int size = 0;
			
			try {
				reader = new BufferedReader(new FileReader(database));
				String line = null;
				
					while((line = reader.readLine()) != null){
						WeightedTFIDF queryToDoc = new WeightedTFIDF(queryString, line, idfMap, stopwords);
						// NOTE: The main difference is that this returns the WHOLE DOCUMENT which is matched, instead of just the document name
						Pair<String, Double> newPair = new SearchFiles(). new Pair(line, queryToDoc.getCosineSimilarity_NormalizeFirst(inverted_index));
						treeSet.add(newPair);
					}				
				
			} catch (FileNotFoundException e) {
				System.out.println("FAILURE IN SEARCH FILES 1");
			} catch (IOException e) {
				System.out.println("FAILURE IN SEARCH FILES 2");
			}

			for(int i = 0;i<numResults;i++){
				Pair<String, Double> pair = treeSet.pollFirst();
				docs.add(pair.x);
			}
			
			return docs;
		}
		
	// Version tells us which version to use - 3a/3b/3c/3d
	public static List<String> searchQueryStudent(String queryString, String database, int numResults, 
			CharArraySet stopwords, int version, HashMap<String, HashSet<String>>inverted_index){
		List<String> docs = new ArrayList<String>(); //Filenames of the files that matched the search query
		List<WeightedTFIDF> tf_idf = new ArrayList<WeightedTFIDF>();	// To return
		HashMap<String, Double> idfMap = WeightedTFIDF.createIDFMapping(database); // Hashed Re-use
		
		BufferedReader reader = null;
		PriorityQueue<Pair<String, Double>> queue = new PriorityQueue(numResults, new SearchFiles().new PairComparator());
		TreeSet<Pair<String, Double>> treeSet = new TreeSet(new SearchFiles().new PairComparator());
		int size = 0;
		
		try {
			reader = new BufferedReader(new FileReader(database));
			String line = null;
			// 3a
			if(version == 1){
				while((line = reader.readLine()) != null){
					WeightedTFIDF queryToDoc = new WeightedTFIDF(queryString, line, idfMap, stopwords);
					Pair<String, Double> newPair = new SearchFiles(). new Pair(line, queryToDoc.getCosineSimilarity_NormalizeFirst(inverted_index));
					queue.add(newPair);
					treeSet.add(newPair);
				}
				// 3b
			}else if(version == 2){
				while((line = reader.readLine()) != null){
					WeightedTFIDF queryToDoc = new WeightedTFIDF(queryString, line, idfMap, stopwords);
					Pair<String, Double> newPair = new SearchFiles(). new Pair(line, queryToDoc.getCosineSimilarity_NoNormalize());
					queue.add(newPair);
				}
				// 3c
			}else if(version == 3){
				while((line = reader.readLine()) != null){
					WeightedTFIDF queryToDoc = new WeightedTFIDF(queryString, line, idfMap, stopwords);
					Pair<String, Double> newPair = new SearchFiles(). new Pair(line, queryToDoc.getWeighting_Similarity());
					queue.add(newPair);
				}
				// 3d - Our own answers
			}else if(version == 4){
				while((line = reader.readLine()) != null){
					WeightedTFIDF queryToDoc = new WeightedTFIDF(queryString, line, idfMap, stopwords);
					Pair<String, Double> newPair = new SearchFiles(). new Pair(line, 
							Math.max(queryToDoc.getWeighting_Similarity(), Math.max(queryToDoc.getCosineSimilarity_NoNormalize(), 
									queryToDoc.getCosineSimilarity_NormalizeFirst(inverted_index))));
					queue.add(newPair);
				}
				
				// Problem 4
			} else if(version == 5){
				while((line = reader.readLine()) != null){
					WeightedTFIDF queryToDoc = new WeightedTFIDF(queryString, line, idfMap, stopwords);
					Pair<String, Double> newPair = new SearchFiles(). new Pair(line, queryToDoc.getBM25());
					queue.add(newPair);
				}
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("FAILURE IN SEARCH FILES 1");
		} catch (IOException e) {
			System.out.println("FAILURE IN SEARCH FILES 2");
		}

		for(int i = 0;i<numResults;i++){
			Pair<String, Double> pair = treeSet.pollFirst();
			docs.add((pair.x.split(",")[0]).split(".txt")[0]);
		}
		
		return docs;
	}

	/** This function is only for test search. */
	public static List<String> searchQuery(String indexDir, String queryString, int numResults, CharArraySet stopwords) {
		String field = "contents";
		List<String> hitPaths = new ArrayList<String>();

		try {
			IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexDir)));
			IndexSearcher searcher = new IndexSearcher(reader);
//			Analyzer analyzer = new MyAnalyzer(Version.LUCENE_44, stopwords);
//
//			QueryParser parser = new QueryParser(Version.LUCENE_44, field, analyzer);
			Analyzer analyzer = new MyAnalyzer(stopwords);

			QueryParser parser = new QueryParser(field, analyzer);
			Query query;
			query = parser.parse(QueryParser.escape(queryString));

			TopDocs results = searcher.search(query, null, numResults);
			
			for (ScoreDoc hit : results.scoreDocs) {
				String path = searcher.doc(hit.doc).get("path");
				hitPaths.add(path.substring(0, path.length()-4)); // chop off the file extension (".txt")
			}
		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		} catch (ParseException e) {
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		}

		return hitPaths;
	}
	
	// Pair to represent (term, score) mapping
	public class Pair<String, Double>{
		public String x;
		public Double y;
		public Pair(String x, Double y){
			this.x = x;
			this.y = y;
		}
	}
	
	public class PairComparator implements Comparator<Pair<String, Double>> {

		@Override
		public int compare(Pair<java.lang.String, java.lang.Double> o1,
				Pair<java.lang.String, java.lang.Double> o2) {
			return (o2.y > o1.y) ? 1 : -1;
		}
		
	}
}
