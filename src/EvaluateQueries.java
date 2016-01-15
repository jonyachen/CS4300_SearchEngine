import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.io.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
// import lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

public class EvaluateQueries {
	public static void main(String[] args) {
		String cacmDocsDir = "data/cacm"; // directory containing CACM documents
		String medDocsDir = "data/med"; // directory containing MED documents

		String cacmIndexDir = "data/index/cacm"; // the directory where index is written into
		String medIndexDir = "data/index/med"; // the directory where index is written into

		String cacmQueryFile = "data/cacm_processed.query";    // CACM query file
		String cacmAnswerFile = "data/cacm_processed.rel";   // CACM relevance judgements file

		String medQueryFile = "data/med_processed.query";    // MED query file
		String medAnswerFile = "data/med_processed.rel";   // MED relevance judgements file

		int cacmNumResults = 100;
		int medNumResults = 100;

		//TODO: BUILD NEW CHARARRAYSET HERE
		BufferedReader reader = null;
		HashSet<String> stopWordSet = new HashSet<String>();
		try {
			reader = new BufferedReader(new FileReader("data/stopwords/stopwords_indri.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;

		try {
			line = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while(line != null){
			stopWordSet.add(line);
			try {
				line = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	    // CharArraySet stopwords = new CharArraySet(Version.LUCENE_44,0,false);
		//No stopwords
	    CharArraySet stopwords = new CharArraySet(0, false);
	    CharArraySet specialStopWords = new CharArraySet(stopWordSet, false);


	    IndexFiles.buildStudentIndex(cacmIndexDir, cacmDocsDir, "cacm", specialStopWords);
	    IndexFiles.buildStudentIndex(medIndexDir, medDocsDir, "med", specialStopWords);

		/*//Prints out the avg precision AT 100 documents over all queries
		System.out.println(evaluate(cacmIndexDir, cacmDocsDir, cacmQueryFile,
				cacmAnswerFile, cacmNumResults, stopwords));

		System.out.println("\n");

		//Prints out the avg precision AT 100 documents over all queries
		System.out.println(evaluate(medIndexDir, medDocsDir, medQueryFile,
				medAnswerFile, medNumResults, stopwords));

		System.out.println("\n");*/

		//HashMap<String, Double> mapping = WeightedTFIDF.createIDFMapping("data/index/cacm/cacm.csv");
		//Begin output for Project 1


		//Part 1 output

		/*System.out.println("Part 1 - MAP measure for CACM documents: " + MAP(cacmIndexDir, cacmDocsDir, cacmQueryFile,
				cacmAnswerFile, cacmNumResults, stopwords));
		System.out.println("\n");

		//Part 1 output
		System.out.println("Part 1 - MAP measure for Medlars documents: " + MAP(medIndexDir, medDocsDir, medQueryFile,
				medAnswerFile, medNumResults, stopwords));
		System.out.println("\n");*/

		//Part 3a output
		/*System.out.println("MAP measure for CACM docs - Part 3a implementation: "+ MAP_MiniSearch_3a(cacmIndexDir, cacmDocsDir, cacmQueryFile,
				cacmAnswerFile, "data/index/cacm/cacm.csv", cacmNumResults, specialStopWords));
		System.out.println("\n");*/



		//Part 3a output
		/*System.out.println("MAP measure for MED docs - Part 3a implementation: "+ MAP_MiniSearch_3a(medIndexDir, medDocsDir, medQueryFile,
				medAnswerFile, "data/index/med/med.csv", medNumResults, specialStopWords));
		System.out.println("\n");*/


		//Part 3b output
		/*System.out.println("MAP measure for CACM docs - Part 3b implementation: "+ MAP_MiniSearch_3b(cacmIndexDir, cacmDocsDir, cacmQueryFile,
				cacmAnswerFile, "data/index/cacm/cacm.csv", cacmNumResults, specialStopWords));
		System.out.println("\n");
		/*
		//Part 3b output
		System.out.println("MAP measure for MED docs - Part 3b implementation: "+ MAP_MiniSearch_3b(medIndexDir, medDocsDir, medQueryFile,
				medAnswerFile, "data/index/med/med.csv", medNumResults, specialStopWords));
		System.out.println("\n");

		//Part 3c output
		System.out.println("MAP measure for CACM docs - Part 3c implementation: "+ MAP_MiniSearch_3c(cacmIndexDir, cacmDocsDir, cacmQueryFile,
				cacmAnswerFile, "data/index/cacm/cacm.csv", cacmNumResults, specialStopWords));
		System.out.println("\n");

		//Part 3c output
		System.out.println("MAP measure for MED docs - Part 3c implementation: "+ MAP_MiniSearch_3c(medIndexDir, medDocsDir, medQueryFile,
				medAnswerFile, "data/index/med/med.csv", medNumResults, specialStopWords));
		System.out.println("\n");

		//Part 3d output
		System.out.println("MAP measure for CACM docs - Part 3d implementation: "+ MAP_MiniSearch_3d(cacmIndexDir, cacmDocsDir, cacmQueryFile,
				cacmAnswerFile, "data/index/cacm/cacm.csv", cacmNumResults, specialStopWords));
		System.out.println("\n");

		//Part 3d output
		System.out.println("MAP measure for MED docs - Part 3d implementation: "+ MAP_MiniSearch_3d(medIndexDir, medDocsDir, medQueryFile,
				medAnswerFile, "data/index/med/med.csv", medNumResults, specialStopWords));
		System.out.println("\n");

		// THE FOLLOWING (PROBLEM 4) MUST BE CALLED IN THE FOLLOWING ORDER!!
		WeightedTFIDF.createInvertCount("data/index/cacm/cacm.csv");

		//Part 4 output
		System.out.println("MAP measure for CACM docs - Part 4 implementation: "+ MAP_MiniSearch_4(cacmIndexDir, cacmDocsDir, cacmQueryFile,
				cacmAnswerFile, "data/index/cacm/cacm.csv", cacmNumResults, specialStopWords));
		System.out.println("\n");

		WeightedTFIDF.createInvertCount("data/index/med/med.csv");

		//Part 4 output
		System.out.println("MAP measure for MED docs - Part 4 implementation: "+ MAP_MiniSearch_4(medIndexDir, medDocsDir, medQueryFile,
				medAnswerFile, "data/index/med/med.csv", medNumResults, specialStopWords));
		System.out.println("\n");
		*/
		
		//End output for Project 1

		//Begin output for Project 2
		//Part 1a output
		int cacmNumResults1a = 7;
		int medNumResults1a = 7;

		//Part 1a output
		System.out.println("MAP measure for CACM docs - Part 1a implementation: "+ MAP_MiniSearch2_1(cacmIndexDir, cacmDocsDir, cacmQueryFile,
		cacmAnswerFile, "data/index/cacm/cacm.csv", cacmNumResults1a, specialStopWords, 4, 8, 0, 5));
		System.out.println("\n");

		//Part 1a output
		System.out.println("MAP measure for MED docs - Part 1a implementation: "+ MAP_MiniSearch2_1(medIndexDir, medDocsDir, medQueryFile,
		medAnswerFile, "data/index/med/med.csv", medNumResults1a, specialStopWords, 4, 8, 0, 5));
		System.out.println("\n");
		
		//Part 1b output
		/*
		System.out.println("MAP measure for CACM docs - Part 1b implementation: "+ MAP_MiniSearch2_1(cacmIndexDir, cacmDocsDir, cacmQueryFile,
		cacmAnswerFile, "data/index/cacm/cacm.csv", cacmNumResults1a, specialStopWords, 4, 16, 0, 10));
		System.out.println("\n");

		//Part 1b output
		System.out.println("MAP measure for MED docs - Part 1b implementation: "+ MAP_MiniSearch2_1(medIndexDir, medDocsDir, medQueryFile,
		medAnswerFile, "data/index/med/med.csv", medNumResults1a, specialStopWords, 4, 16, 0, 10));
		*/

		//Part 2 output
		/*System.out.println("MAP measure for CACM docs - Part 2 implementation: "+ Cluster30(cacmIndexDir, cacmDocsDir, cacmQueryFile,
				cacmAnswerFile, "data/index/cacm/cacm.csv", cacmNumResults, specialStopWords));
		System.out.println("\n");
		
		//Part 2 output
		System.out.println("MAP measure for MED docs - Part 2 implementation: "+ Cluster30(cacmIndexDir, cacmDocsDir, cacmQueryFile,
				cacmAnswerFile, "data/index/cacm/cacm.csv", medNumResults, specialStopWords));
		System.out.println("\n");*/
		
		//Part 3a output
		/*
		System.out.println("MAP measure for CACM docs - Part 3a implementation: "+ MAP_MiniSearch2_3(cacmIndexDir, cacmDocsDir, cacmQueryFile,
				cacmAnswerFile, "data/index/cacm/cacm.csv", cacmNumResults1a, specialStopWords, 4, 8, 4, 5));
		System.out.println("\n");

		//Part 3a output
		System.out.println("MAP measure for MED docs - Part 3a implementation: "+ MAP_MiniSearch2_3(medIndexDir, medDocsDir, medQueryFile,
				medAnswerFile, "data/index/med/med.csv", medNumResults1a, specialStopWords, 4, 8, 4, 5));
		*/
				
		//Part 3b output
		/*
		System.out.println("MAP measure for CACM docs - Part 3b implementation: "+ MAP_MiniSearch2_3(cacmIndexDir, cacmDocsDir, cacmQueryFile,
				cacmAnswerFile, "data/index/cacm/cacm.csv", cacmNumResults1a, specialStopWords, 4, 16, 0, 5));
		System.out.println("\n");

		//Part 3b output
		System.out.println("MAP measure for MED docs - Part 3b implementation: "+ MAP_MiniSearch2_3(medIndexDir, medDocsDir, medQueryFile,
				medAnswerFile, "data/index/med/med.csv", medNumResults1a, specialStopWords, 4, 16, 0, 5));
		*/
	}

	private static Map<Integer, String> loadQueries(String filename) {
		HashMap<Integer, String> queryIdMap = new HashMap<Integer, String>();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(
					new File(filename)));
		} catch (FileNotFoundException e) {
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		}

		String line;
		try {
			while ((line = in.readLine()) != null) {
				int pos = line.indexOf(',');
				queryIdMap.put(Integer.parseInt(line.substring(0, pos)), line
						.substring(pos + 1));
			}
		} catch(IOException e) {
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		} finally {
			try {
				in.close();
			} catch(IOException e) {
				System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
			}
		}
		return queryIdMap;
	}

	private static Map<Integer, HashSet<String>> loadAnswers(String filename) {
		HashMap<Integer, HashSet<String>> queryAnswerMap = new HashMap<Integer, HashSet<String>>();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(
					new File(filename)));

			String line;
			while ((line = in.readLine()) != null) {
				String[] parts = line.split(" ");
				HashSet<String> answers = new HashSet<String>();
				for (int i = 1; i < parts.length; i++) {
					answers.add(parts[i]);
				}
				queryAnswerMap.put(Integer.parseInt(parts[0]), answers);
			}
		} catch(IOException e) {
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		} finally {
			try {
				in.close();
			} catch(IOException e) {
				System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
			}
		}
		return queryAnswerMap;
	}

	private static double precision(HashSet<String> answers,
			List<String> results) {
		double matches = 0;
		for (String result : results) {
			if (answers.contains(result))
				matches++;
		}
		//returns # of retrieved relevent docs/# of retrieved docs (precision AT particular doc position)
		return matches / results.size();
	}

	private static double evaluate(String indexDir, String docsDir,
			String queryFile, String answerFile, int numResults,
			CharArraySet stopwords) {

		// Build Index
		IndexFiles.buildIndex(indexDir, docsDir, stopwords);

		// load queries and answer
		Map<Integer, String> queries = loadQueries(queryFile);
		//The actual correct relevant docs that should be retrieved
		Map<Integer, HashSet<String>> queryAnswers = loadAnswers(answerFile);

		// Search and evaluate
		double sum = 0;
		for (Integer i : queries.keySet()) {
			if (i == 1) {
				List<String> results = SearchFiles.searchQuery(indexDir, queries
						.get(i), numResults, stopwords);
				//Adds up all precisions for retrieved docs
				sum += precision(queryAnswers.get(i), results);
				System.out.printf("\nTopic %d  ", i);
				System.out.print (results);
				System.out.println();
			}
		}

		//Gets avg precision over all queries
		return sum / queries.size();
	}

	private static double MAP_MiniSearch_4(String indexDir, String docsDir,
			String queryFile, String answerFile, String database, int numResults,
			CharArraySet stopwords) {

		// Build Index
		IndexFiles.buildIndex(indexDir, docsDir, stopwords);

		// load queries and answer
		Map<Integer, String> queries = loadQueries(queryFile);
		//The actual correct relevant docs that should be retrieved
		Map<Integer, HashSet<String>> queryAnswers = loadAnswers(answerFile);

		// Search and evaluate
		double sum = 0;
		double avgPrecSum = 0;
		HashMap<String, HashSet<String>> inverted_index = WeightedTFIDF.createInvertedIndex(database);

		//Goes through each given query
		for (Integer i : queries.keySet()) {
				sum = 0;
				double answerSize = queryAnswers.get(i).size();
				//Get the relevant docs for the specific query
				HashSet<String> answer = queryAnswers.get(i);
				//System.out.println();
				//System.out.println("Relevent list: " + queryAnswers.get(i));
				List<String> results = SearchFiles.searchQueryStudent(queries.get(i), database,
						numResults, stopwords, 5, inverted_index);

				//System.out.println("Retrieved list: " + results);

				//Goes through each retrieved doc
				for(int j = 0; j < results.size(); j++)
				{
					//Creates a list of results up to the specific retrieved doc
					List<String> temp = results.subList(0, j+1);
					//Creates a string for the specific retrieved doc
					String doc = results.get(j);

					//Checks to see if the specific retrieved doc is relevant
					if(answer.contains(doc))
					{
						sum += precision(answer, temp);
						//System.out.println("Relevent & retrieved list: " + doc);
					}
				}
				System.out.printf("\nTopic %d  ", i);
				//average precision = number of relevant relevant docs/# relevant docs
				double avgPrecision = sum/answerSize;
				avgPrecSum += avgPrecision;
				System.out.print("MAP = " + avgPrecision);
		}
		System.out.println();

		//Gets avg precision over all queries
		return avgPrecSum / queries.size();
	}

	private static double MAP_MiniSearch_3d(String indexDir, String docsDir,
			String queryFile, String answerFile, String database, int numResults,
			CharArraySet stopwords) {

		// Build Index
		IndexFiles.buildIndex(indexDir, docsDir, stopwords);

		// load queries and answer
		Map<Integer, String> queries = loadQueries(queryFile);
		//The actual correct relevant docs that should be retrieved
		Map<Integer, HashSet<String>> queryAnswers = loadAnswers(answerFile);

		// Search and evaluate
		double sum = 0;
		double avgPrecSum = 0;
		HashMap<String, HashSet<String>> inverted_index = WeightedTFIDF.createInvertedIndex(database);

		//Goes through each given query
		for (Integer i : queries.keySet()) {
				sum = 0;
				double answerSize = queryAnswers.get(i).size();
				//Get the relevant docs for the specific query
				HashSet<String> answer = queryAnswers.get(i);
				//System.out.println();
				//System.out.println("Relevent list: " + queryAnswers.get(i));
				List<String> results = SearchFiles.searchQueryStudent(queries.get(i), database,
						numResults, stopwords, 4, inverted_index);

				//System.out.println("Retrieved list: " + results);

				//Goes through each retrieved doc
				for(int j = 0; j < results.size(); j++)
				{
					//Creates a list of results up to the specific retrieved doc
					List<String> temp = results.subList(0, j+1);
					//Creates a string for the specific retrieved doc
					String doc = results.get(j);

					//Checks to see if the specific retrieved doc is relevant
					if(answer.contains(doc))
					{
						sum += precision(answer, temp);
						//System.out.println("Relevent & retrieved list: " + doc);
					}
				}
				System.out.printf("\nTopic %d  ", i);
				//average precision = number of relevant relevant docs/# relevant docs
				double avgPrecision = sum/answerSize;
				avgPrecSum += avgPrecision;
				System.out.print("MAP = " + avgPrecision);
		}
		System.out.println();

		//Gets avg precision over all queries
		return avgPrecSum / queries.size();
	}

	private static double MAP_MiniSearch_3c(String indexDir, String docsDir,
			String queryFile, String answerFile, String database, int numResults,
			CharArraySet stopwords) {

		// Build Index
		IndexFiles.buildIndex(indexDir, docsDir, stopwords);

		// load queries and answer
		Map<Integer, String> queries = loadQueries(queryFile);
		//The actual correct relevant docs that should be retrieved
		Map<Integer, HashSet<String>> queryAnswers = loadAnswers(answerFile);

		// Search and evaluate
		double sum = 0;
		double avgPrecSum = 0;
		HashMap<String, HashSet<String>> inverted_index = WeightedTFIDF.createInvertedIndex(database);

		//Goes through each given query
		for (Integer i : queries.keySet()) {
				sum = 0;
				double answerSize = queryAnswers.get(i).size();
				//Get the relevant docs for the specific query
				HashSet<String> answer = queryAnswers.get(i);
				//System.out.println();
				//System.out.println("Relevent list: " + queryAnswers.get(i));
				List<String> results = SearchFiles.searchQueryStudent(queries.get(i), database,
						numResults, stopwords, 3, inverted_index);

				//System.out.println("Retrieved list: " + results);

				//Goes through each retrieved doc
				for(int j = 0; j < results.size(); j++)
				{
					//Creates a list of results up to the specific retrieved doc
					List<String> temp = results.subList(0, j+1);
					//Creates a string for the specific retrieved doc
					String doc = results.get(j);

					//Checks to see if the specific retrieved doc is relevant
					if(answer.contains(doc))
					{
						sum += precision(answer, temp);
						//System.out.println("Relevent & retrieved list: " + doc);
					}
				}
				System.out.printf("\nTopic %d  ", i);
				//average precision = number of relevant relevant docs/# relevant docs
				double avgPrecision = sum/answerSize;
				avgPrecSum += avgPrecision;
				System.out.print("MAP = " + avgPrecision);
		}
		System.out.println();

		//Gets avg precision over all queries
		return avgPrecSum / queries.size();
	}

	private static double MAP_MiniSearch_3b(String indexDir, String docsDir,
			String queryFile, String answerFile, String database, int numResults,
			CharArraySet stopwords) {

		// Build Index
		IndexFiles.buildIndex(indexDir, docsDir, stopwords);

		// load queries and answer
		Map<Integer, String> queries = loadQueries(queryFile);
		//The actual correct relevant docs that should be retrieved
		Map<Integer, HashSet<String>> queryAnswers = loadAnswers(answerFile);

		// Search and evaluate
		double sum = 0;
		double avgPrecSum = 0;
		HashMap<String, HashSet<String>> inverted_index = WeightedTFIDF.createInvertedIndex(database);

		//Goes through each given query
		for (Integer i : queries.keySet()) {
				sum = 0;
				double answerSize = queryAnswers.get(i).size();
				//Get the relevant docs for the specific query
				HashSet<String> answer = queryAnswers.get(i);
				//System.out.println();
				//System.out.println("Relevent list: " + queryAnswers.get(i));
				List<String> results = SearchFiles.searchQueryStudent(queries.get(i), database,
						numResults, stopwords, 2, inverted_index);

				//System.out.println("Retrieved list: " + results);

				//Goes through each retrieved doc
				for(int j = 0; j < results.size(); j++)
				{
					//Creates a list of results up to the specific retrieved doc
					List<String> temp = results.subList(0, j+1);
					//Creates a string for the specific retrieved doc
					String doc = results.get(j);

					//Checks to see if the specific retrieved doc is relevant
					if(answer.contains(doc))
					{
						sum += precision(answer, temp);
						//System.out.println("Relevent & retrieved list: " + doc);
					}
				}
				System.out.printf("\nTopic %d  ", i);
				//average precision = number of relevant relevant docs/# relevant docs
				double avgPrecision = sum/answerSize;
				avgPrecSum += avgPrecision;
				System.out.print("MAP = " + avgPrecision);
		}
		System.out.println();

		//Gets avg precision over all queries
		return avgPrecSum / queries.size();
	}

	private static double MAP_MiniSearch_3a(String indexDir, String docsDir,
			String queryFile, String answerFile, String database, int numResults,
			CharArraySet stopwords) {

		// Build Index
		IndexFiles.buildIndex(indexDir, docsDir, stopwords);

		// load queries and answer
		Map<Integer, String> queries = loadQueries(queryFile);
		//The actual correct relevant docs that should be retrieved
		Map<Integer, HashSet<String>> queryAnswers = loadAnswers(answerFile);

		// Search and evaluate
		double sum = 0;
		double avgPrecSum = 0;
		HashMap<String, HashSet<String>> inverted_index = WeightedTFIDF.createInvertedIndex(database);
		//Goes through each given query
		for (Integer i : queries.keySet()) {
				sum = 0;
				double answerSize = queryAnswers.get(i).size();
				//Get the relevant docs for the specific query
				HashSet<String> answer = queryAnswers.get(i);
				//System.out.println();
				//System.out.println("Relevant list: " + queryAnswers.get(i));
				//System.out.println("Relevent list: " + queryAnswers.get(i));
				List<String> results = SearchFiles.searchQueryStudent(queries.get(i), database,
						numResults, stopwords, 1, inverted_index);

				//System.out.println("Retrieved list: " + results);

				//Goes through each retrieved doc
				for(int j = 0; j < results.size(); j++)
				{
					//Creates a list of results up to the specific retrieved doc
					List<String> temp = results.subList(0, j+1);
					//Creates a string for the specific retrieved doc
					String doc = results.get(j);

					//Checks to see if the specific retrieved doc is relevant
					if(answer.contains(doc))
					{
						sum += precision(answer, temp);
						//System.out.println("Relevant & retrieved list: " + doc);
					}
				}
				System.out.printf("\nTopic %d  ", i);
				//average precision = number of relevant relevant docs/# relevant docs
				double avgPrecision = sum/answerSize;
				avgPrecSum += avgPrecision;
				System.out.print("MAP = " + avgPrecision);
		}
		System.out.println();

		//Gets avg precision over all queries
		return avgPrecSum / queries.size();
	}

	private static double MAP_MiniSearch2_1(String indexDir, String docsDir,
			String queryFile, String answerFile, String database, int numResults,
			CharArraySet stopwords, int A, int B, int C, int K) {

			// Build Index
			IndexFiles.buildIndex(indexDir, docsDir, stopwords);

			// load queries and answer
			Map<Integer, String> queries = loadQueries(queryFile);
			//The actual correct relevant docs that should be retrieved
			Map<Integer, HashSet<String>> queryAnswers = loadAnswers(answerFile);

			// Search and evaluate
			double sum = 0;
			double avgPrecSum = 0;
			HashMap<String, HashSet<String>> inverted_index = WeightedTFIDF.createInvertedIndex(database);
			//Goes through each given query
			for (Integer i : queries.keySet()) {
			sum = 0;
			double answerSize = queryAnswers.get(i).size();
			//Get the relevant docs for the specific query
			HashSet<String> answer = queryAnswers.get(i);
			//System.out.println();
			//System.out.println("Relevant list: " + queryAnswers.get(i));
			List<String> results = SearchFiles.searchQueryStudent(queries.get(i), database,
					numResults, stopwords, 1, inverted_index);
			
			/*
			System.out.println();
			System.out.println("Retrieved list: " + results);
			*/
			
			//Gets the new expanded query - new code implementation below
			
			//Creates our matrix of tf*idf weights
			ArrayList<ArrayList<Double>> termByDoc = new ArrayList<ArrayList<Double>>();
			
			//Giant listOfTerms - contains all terms in both query and doc
			ArrayList<String> listOfTerms = new ArrayList<String>();
			
			//Get list of all terms in query termsInQuery
			Analyzer analyzer = new MyAnalyzer(stopwords);
			QueryParser parser = new QueryParser("contents", analyzer);
			Query query = null;
			HashSet<Term> terms = new HashSet<Term>();

			try {
				query = parser.parse(QueryParser.escape(queries.get(i)));
				query.extractTerms(terms);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			ArrayList<String> termsInQuery = new ArrayList<String>();
			for(Term t: terms) {
				String key = t.text();
				termsInQuery.add(key);
			}
			listOfTerms.addAll(termsInQuery);
			
			//Debug print - to see terms in the query
			/*
			System.out.println("TermsInQuery here");
			for(int x = 0; x < termsInQuery.size(); x++)
			{
			        System.out.print(termsInQuery.get(x));
			        System.out.print(" ");
			}
			*/
			
			//Get list of all terms in each doc termsInDoc (for each doc, get list of terms for that doc)
			List<String> fullFile = SearchFiles.searchQueryIncludeFullFile(queries.get(i), database,
					numResults, stopwords, 1, inverted_index);
			HashMap<String, Double> idfMap = WeightedTFIDF.createIDFMapping(database);
			for(String docu: fullFile){	
				//System.out.println(docu);
				WeightedTFIDF queryToDoc = new WeightedTFIDF(queries.get(i), docu, idfMap, stopwords);
				ArrayList<String> keys = new ArrayList<String>(queryToDoc.tfDoc.keySet());
				ArrayList<String> termsInDoc = new ArrayList<String>();
				for (int j = 0; j < keys.size(); j++) {
				    termsInDoc.add(keys.get(j));
				}
				listOfTerms.addAll(termsInDoc);
				
				//Debug print - to see terms in each document
				/*
				System.out.println();
				System.out.println("TermsInDoc here");
				for(int x = 0; x < termsInDoc.size(); x++)
				{
				        System.out.print(termsInDoc.get(x));
				        System.out.print(" ");
				}	
				*/			
			}
			
			//Only perform steps below once all terms are added into listOfTerms		
			//Gets rid of duplicates in listOfTerms
			HashSet<String> hs = new HashSet<String>();
			hs.addAll(listOfTerms);
			listOfTerms.clear();
			listOfTerms.addAll(hs);
			
			//Debug print - to see all terms in query and all 7 relevant documents
			/*
			System.out.println();
			System.out.println("listOfTerms here");
			for(int x = 0; x < listOfTerms.size(); x++)
			{
			        System.out.print(listOfTerms.get(x));
			        System.out.print(" ");
			}
			*/
			
			//Creates a new row of weights for each document
			for(String docu: fullFile){	
				WeightedTFIDF queryToDoc = new WeightedTFIDF(queries.get(i), docu, idfMap, stopwords);
				ArrayList<Double> docRow = new ArrayList<Double>();
				ArrayList<String> keys = new ArrayList<String>(queryToDoc.tfDoc.keySet());
				ArrayList<String> termsInDoc = new ArrayList<String>();
				for (int j = 0; j < keys.size(); j++) {
				    termsInDoc.add(keys.get(j));
				}
				//Input tf*idf weight - pass in doc id s and get weight for all terms in s
				for(String term: listOfTerms)
				{
					if(termsInDoc.contains(term)){	
						double tf = queryToDoc.tfDoc.get(term); 
						double idf = WeightedTFIDF.calculateIDF(inverted_index, term);
						docRow.add(tf*idf);
					}
					else{
						docRow.add(0.0);	
					}
				}
				
				//Debug print line - See each row that is added to tf*idf matrix per document
				/*
				System.out.println();
				System.out.println("tf*idf row is: ");
				for(int x = 0; x < docRow.size(); x++)
				{
				        System.out.print(docRow.get(x));
				        System.out.print(" ");
				}
				*/
				termByDoc.add(docRow);	
			}
			
			//Creates a row of weights for terms in the query
			ArrayList<Double> queryRow = new ArrayList<Double>();
			for(String term: listOfTerms)
			{
				if(!inverted_index.containsKey(term)){
					double tf = 1.0;
					double idf = 0;
					queryRow.add(tf*idf);
				}
				else if(termsInQuery.contains(term)){
					double tf = 1.0; //assume 1 because tf in query 
					double idf = WeightedTFIDF.calculateIDF(inverted_index, term);
					queryRow.add(tf*idf);
				}
				else{
					queryRow.add(0.0);	
				}
			}
			//Debug print line - See the row that is added to tf*idf matrix for the query
			/*
			System.out.println();
			System.out.println("tf*idf query row is: ");
			for(int x = 0; x < queryRow.size(); x++)
			{
			        System.out.print(queryRow.get(x));
			        System.out.print(" ");
			}
			*/
			termByDoc.add(queryRow);
			
			//Calculate Rocchio weights
			//Creates a row of weights for terms in the query
			ArrayList<Double> rocchioWeightRow = new ArrayList<Double>();
			for(int col = 0; col < listOfTerms.size(); col++)
			{
				double termWeight = 0;
				double sumOfRelWeights = 0;
				for(int row = 0; row < termByDoc.size()-1; row++)
				{
					sumOfRelWeights += termByDoc.get(row).get(col);
				}
				//Rocchio weight = A*q + B*(1/rel)*sumOf(dOfRel) + C*(1/nonrel)*sumOf(dOfNonRel)
				termWeight = A * termByDoc.get(termByDoc.size()-1).get(col) + B * (1.0/numResults) * sumOfRelWeights;
				rocchioWeightRow.add(termWeight);
			}		
			
			//Debug print line - See the row that is added which includes Rocchio weights
			/*
			System.out.println();
			System.out.println("Rocchio weight row is: ");
			for(int x = 0; x < rocchioWeightRow.size(); x++)
			{
			        System.out.print(rocchioWeightRow.get(x));
			        System.out.print(" ");
			}
			*/
			
			//Selects terms with the highest weights to expand the query
		    ArrayList<String> newTermsInQuery = termsInQuery;
		    int limitSize = termsInQuery.size() + K;
		    ArrayList<Double> rocchioWeightRowCopy = rocchioWeightRow;
		    while(newTermsInQuery.size() < limitSize)
		    {
				//Finds term with the highest weight
				Double maxValue = Collections.max(rocchioWeightRowCopy);
				int maxIndex = rocchioWeightRowCopy.indexOf(maxValue);
				String termToAdd = listOfTerms.get(maxIndex);
				
				//Debug statements
				/*
				System.out.println();
			    System.out.println("Maximum weight is: " + maxValue);
			    System.out.println("Index of maximum weight is: " + maxIndex);
			    System.out.println("Term with maximum weight is: " + termToAdd);
			    */
				
			    //Check to see if the term is already in the query
			    if(!newTermsInQuery.contains(termToAdd)){
			    	newTermsInQuery.add(termToAdd);
			    }
			    else{
			    	rocchioWeightRowCopy.set(maxIndex, 0.0);
			    }
		    }

			//Debug print line - See the new expanded query
		    /*
			System.out.println();
			System.out.println("New expanded query is : ");
			for(int z = 0; z < newTermsInQuery.size(); z++)
			{
			        System.out.print(newTermsInQuery.get(z));
			        System.out.print(" ");
			}
			*/
		    
		    //With the new query, rank all documents again to get the top 100 documents
			String newQueryString = new String("");
			for(String newQueryTerm : newTermsInQuery){
				newQueryString += " " + newQueryTerm;
			}
			/*
			System.out.println();
			System.out.println("New Query String: " + newQueryString);
			*/

			List<String> newResults = SearchFiles.searchQueryStudent(newQueryString, database,
					100, stopwords, 1, inverted_index);
			
/*		    for(String newQueryTerm: newTermsInQuery){
		    	for(int w = 0; w < listOfTerms.size(); w++){
		    		if((listOfTerms.get(w).equals(newQueryTerm))){
		    			//rocchioWeightRow.set(w, 9999.0);
		    		}
		    	}	    		
		    }
		    
		    for(String doc: fullFile){
		    	
		    }
*/		    
			
			//Goes through each retrieved doc
			for(int j = 0; j < newResults.size(); j++)
			{
			//Creates a list of results up to the specific retrieved doc
			List<String> temp = newResults.subList(0, j+1);
			//Creates a string for the specific retrieved doc
			String doc = newResults.get(j);

			//Checks to see if the specific retrieved doc is relevant
			if(answer.contains(doc))
			{
			sum += precision(answer, temp);
			//System.out.println("Relevant & retrieved list: " + doc);
			}
			}
			System.out.printf("\nTopic %d  ", i);
			//average precision = number of relevant relevant docs/# relevant docs
			double avgPrecision = sum/answerSize;
			avgPrecSum += avgPrecision;
			System.out.print("MAP = " + avgPrecision);
			}
			System.out.println();

			//Gets avg precision over all queries
			return avgPrecSum / queries.size();
			}
	
	private static double MAP_MiniSearch2_3(String indexDir, String docsDir,
			String queryFile, String answerFile, String database, int numResults,
			CharArraySet stopwords, int A, int B, int C, int K) {

			// Build Index
			IndexFiles.buildIndex(indexDir, docsDir, stopwords);

			// load queries and answer
			Map<Integer, String> queries = loadQueries(queryFile);
			//The actual correct relevant docs that should be retrieved
			Map<Integer, HashSet<String>> queryAnswers = loadAnswers(answerFile);

			// Search and evaluate
			double sum = 0;
			double avgPrecSum = 0;
			HashMap<String, HashSet<String>> inverted_index = WeightedTFIDF.createInvertedIndex(database);
			//Goes through each given query
			for (Integer i : queries.keySet()) {
			if(i != 23 && i != 31 && i!=33 && i!=39 && i != 50){
			sum = 0;
			double answerSize = queryAnswers.get(i).size();
			//Get the relevant docs for the specific query
			HashSet<String> answer = queryAnswers.get(i);
			//System.out.println();
			//System.out.println("Relevant list: " + queryAnswers.get(i));
			List<String> results = SearchFiles.searchQueryStudent(queries.get(i), database,
					numResults, stopwords, 1, inverted_index);
			
			/*
			System.out.println();
			System.out.println("Retrieved list: " + results);
			*/
			
			//Gets the new expanded query - new code implementation below
			
			//Creates our matrix of tf*idf weights
			ArrayList<ArrayList<Double>> termByDoc = new ArrayList<ArrayList<Double>>();
			
			//Giant listOfTerms - contains all terms in both query and doc
			ArrayList<String> listOfTerms = new ArrayList<String>();
			
			//Get list of all terms in query termsInQuery
			Analyzer analyzer = new MyAnalyzer(stopwords);
			QueryParser parser = new QueryParser("contents", analyzer);
			Query query = null;
			HashSet<Term> terms = new HashSet<Term>();

			try {
				query = parser.parse(QueryParser.escape(queries.get(i)));
				query.extractTerms(terms);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			ArrayList<String> termsInQuery = new ArrayList<String>();
			for(Term t: terms) {
				String key = t.text();
				termsInQuery.add(key);
			}
			listOfTerms.addAll(termsInQuery);
			
			//Debug print - to see terms in the query
			/*
			System.out.println("TermsInQuery here");
			for(int x = 0; x < termsInQuery.size(); x++)
			{
			        System.out.print(termsInQuery.get(x));
			        System.out.print(" ");
			}
			*/
			
			//Get list of all terms in each doc termsInDoc (for each of two docs, get list of terms for that doc)
			List<String> fullFile = SearchFiles.searchQueryIncludeFullFile(queries.get(i), database,
					numResults, stopwords, 1, inverted_index);
			HashMap<String, Double> idfMap = WeightedTFIDF.createIDFMapping(database);
			
			//find the top-ranked relevant document (among the 7) and the top-ranked non-relevant document by using the relevance info file
			int topRelDocIndex = -1;
			int topNonRelDocIndex = -1;	
			boolean relDone = false;
			boolean nonRelDone = false;
			for(int a = 0; a < results.size(); a++){
				String curDoc = results.get(a);
				if(answer.contains(curDoc) && !relDone){
						topRelDocIndex = a;
						relDone = true;
				}
				else if(!answer.contains(curDoc) && !nonRelDone){
						topNonRelDocIndex = a;
						nonRelDone = true;
				}
				else{
				}
			}
			//To check: Proper indexes
			/*
			System.out.println();
			System.out.println(results);
			System.out.println("IndexRel = " + topRelDocIndex);
			System.out.println("IndexNonRel =  " + topNonRelDocIndex);
			*/
			
			//Modify fullFile to only contain the two documents of top relevant and nonrelevant
			List<String> newFullFile = new ArrayList<String>();
			if(relDone){
				newFullFile.add(fullFile.get(topRelDocIndex));
			}
			else if(!relDone){
				B = 0;
			}
			
			if(nonRelDone){
				newFullFile.add(fullFile.get(topNonRelDocIndex));
			}
			else if(!nonRelDone){
				C = 0;
			}
						
			for(String docu: newFullFile){	
				//System.out.println(docu);
				WeightedTFIDF queryToDoc = new WeightedTFIDF(queries.get(i), docu, idfMap, stopwords);
				ArrayList<String> keys = new ArrayList<String>(queryToDoc.tfDoc.keySet());
				ArrayList<String> termsInDoc = new ArrayList<String>();
				for (int j = 0; j < keys.size(); j++) {
				    termsInDoc.add(keys.get(j));
				}
				listOfTerms.addAll(termsInDoc);
				
				//Debug print - to see terms in each document
				/*
				System.out.println();
				System.out.println("TermsInDoc here");
				for(int x = 0; x < termsInDoc.size(); x++)
				{
				        System.out.print(termsInDoc.get(x));
				        System.out.print(" ");
				}	
				*/			
			}
			
			//Only perform steps below once all terms are added into listOfTerms		
			//Gets rid of duplicates in listOfTerms
			HashSet<String> hs = new HashSet<String>();
			hs.addAll(listOfTerms);
			listOfTerms.clear();
			listOfTerms.addAll(hs);
			
			//Debug print - to see all terms in query and all 7 relevant documents
			/*
			System.out.println();
			System.out.println("listOfTerms here");
			for(int x = 0; x < listOfTerms.size(); x++)
			{
			        System.out.print(listOfTerms.get(x));
			        System.out.print(" ");
			}
			*/
			
			//Creates a new row of weights for each document
			for(String docu: newFullFile){	
				WeightedTFIDF queryToDoc = new WeightedTFIDF(queries.get(i), docu, idfMap, stopwords);
				ArrayList<Double> docRow = new ArrayList<Double>();
				ArrayList<String> keys = new ArrayList<String>(queryToDoc.tfDoc.keySet());
				ArrayList<String> termsInDoc = new ArrayList<String>();
				for (int j = 0; j < keys.size(); j++) {
				    termsInDoc.add(keys.get(j));
				}
				//Input tf*idf weight - pass in doc id s and get weight for all terms in s
				for(String term: listOfTerms)
				{
					if(termsInDoc.contains(term)){	
						double tf = queryToDoc.tfDoc.get(term); 
						double idf = WeightedTFIDF.calculateIDF(inverted_index, term);
						docRow.add(tf*idf);
					}
					else{
						docRow.add(0.0);	
					}
				}
				
				//Debug print line - See each row that is added to tf*idf matrix per document
				/*
				System.out.println();
				System.out.println("tf*idf row is: ");
				for(int x = 0; x < docRow.size(); x++)
				{
				        System.out.print(docRow.get(x));
				        System.out.print(" ");
				}
				*/
				termByDoc.add(docRow);	
			}
			
			//Creates a row of weights for terms in the query
			ArrayList<Double> queryRow = new ArrayList<Double>();
			for(String term: listOfTerms)
			{
				if(!inverted_index.containsKey(term)){
					double tf = 1.0;
					double idf = 0;
					queryRow.add(tf*idf);
				}
				else if(termsInQuery.contains(term)){
					double tf = 1.0; //assume 1 because tf in query 
					double idf = WeightedTFIDF.calculateIDF(inverted_index, term);
					queryRow.add(tf*idf);
				}
				else{
					queryRow.add(0.0);	
				}
			}
			//Debug print line - See the row that is added to tf*idf matrix for the query
			/*
			System.out.println();
			System.out.println("tf*idf query row is: ");
			for(int x = 0; x < queryRow.size(); x++)
			{
			        System.out.print(queryRow.get(x));
			        System.out.print(" ");
			}
			*/
			termByDoc.add(queryRow);
			
			//Calculate Rocchio weights
			//Creates a row of weights for terms in the query
			ArrayList<Double> rocchioWeightRow = new ArrayList<Double>();
			for(int col = 0; col < listOfTerms.size(); col++)
			{
				double termWeight = 0;
				double sumOfRelWeights = 0;
				double sumOfNonRelWeights = 0;
				for(int row = 0; row < termByDoc.size()-1; row++)
				{
					if(row == 0)
						sumOfRelWeights += termByDoc.get(row).get(col);
					else if(row ==1)
						sumOfNonRelWeights += termByDoc.get(row).get(col);
				}
				//Rocchio weight = A*q + B*(1/rel)*sumOf(dOfRel) + C*(1/nonrel)*sumOf(dOfNonRel)
				termWeight = A * termByDoc.get(termByDoc.size()-1).get(col) + B * (1.0/1.0) * sumOfRelWeights + C * (1.0/1.0) * sumOfNonRelWeights;
				rocchioWeightRow.add(termWeight);
			}
			
			//Debug print line - See the row that is added which includes Rocchio weights
			/*
			System.out.println();
			System.out.println("Rocchio weight row is: ");
			for(int x = 0; x < rocchioWeightRow.size(); x++)
			{
			        System.out.print(rocchioWeightRow.get(x));
			        System.out.print(" ");
			}
			*/
			
			//Selects terms with the highest weights to expand the query
		    ArrayList<String> newTermsInQuery = termsInQuery;
		    int limitSize = termsInQuery.size() + K;
		    ArrayList<Double> rocchioWeightRowCopy = rocchioWeightRow;
		    while(newTermsInQuery.size() < limitSize)
		    {
				//Finds term with the highest weight
				Double maxValue = Collections.max(rocchioWeightRowCopy);
				int maxIndex = rocchioWeightRowCopy.indexOf(maxValue);
				String termToAdd = listOfTerms.get(maxIndex);
				
				//Debug statements
				/*
				System.out.println();
			    System.out.println("Maximum weight is: " + maxValue);
			    System.out.println("Index of maximum weight is: " + maxIndex);
			    System.out.println("Term with maximum weight is: " + termToAdd);
			    */
				
			    //Check to see if the term is already in the query
			    if(!newTermsInQuery.contains(termToAdd)){
			    	newTermsInQuery.add(termToAdd);
			    }
			    else{
			    	rocchioWeightRowCopy.set(maxIndex, 0.0);
			    }
		    }

			//Debug print line - See the new expanded query
		    /*
			System.out.println();
			System.out.println("New expanded query is : ");
			for(int z = 0; z < newTermsInQuery.size(); z++)
			{
			        System.out.print(newTermsInQuery.get(z));
			        System.out.print(" ");
			}
			*/
		    
		    //With the new query, rank all documents again to get the top 100 documents
			String newQueryString = new String("");
			for(String newQueryTerm : newTermsInQuery){
				newQueryString += " " + newQueryTerm;
			}
			/*
			System.out.println();
			System.out.println("New Query String: " + newQueryString);
			*/

			List<String> newResults = SearchFiles.searchQueryStudent(newQueryString, database,
					100, stopwords, 1, inverted_index);
			
/*		    for(String newQueryTerm: newTermsInQuery){
		    	for(int w = 0; w < listOfTerms.size(); w++){
		    		if((listOfTerms.get(w).equals(newQueryTerm))){
		    			//rocchioWeightRow.set(w, 9999.0);
		    		}
		    	}	    		
		    }
		    
		    for(String doc: fullFile){
		    	
		    }
*/		    
			
			//Goes through each retrieved doc
			for(int j = 0; j < newResults.size(); j++)
			{
			//Creates a list of results up to the specific retrieved doc
			List<String> temp = newResults.subList(0, j+1);
			//Creates a string for the specific retrieved doc
			String doc = newResults.get(j);

			//Checks to see if the specific retrieved doc is relevant
			if(answer.contains(doc))
			{
			sum += precision(answer, temp);
			//System.out.println("Relevant & retrieved list: " + doc);
			}
			}
			System.out.printf("\nTopic %d  ", i);
			//average precision = number of relevant relevant docs/# relevant docs
			double avgPrecision = sum/answerSize;
			avgPrecSum += avgPrecision;
			System.out.print("MAP = " + avgPrecision);
			}
			}//new
			System.out.println();

			//Gets avg precision over all queries
			return avgPrecSum / queries.size();
			}

	
	private static double Cluster30(String indexDir, String docsDir,
			String queryFile, String answerFile, String database, int numResults,
			CharArraySet stopwords) {

		// Build Index
		IndexFiles.buildIndex(indexDir, docsDir, stopwords);

		// load queries and answer
		Map<Integer, String> queries = loadQueries(queryFile);
		//The actual correct relevant docs that should be retrieved
		Map<Integer, HashSet<String>> queryAnswers = loadAnswers(answerFile);

		// Search and evaluate
		double sum = 0;
		double avgPrecSum = 0;
		HashMap<String, HashSet<String>> inverted_index = WeightedTFIDF.createInvertedIndex(database);
		//Goes through each given query
		for (Integer i : queries.keySet()) {
				sum = 0;
				double answerSize = queryAnswers.get(i).size();
				//Get the relevant docs for the specific query
				HashSet<String> answer = queryAnswers.get(i);
				//System.out.println();
				//System.out.println("Relevent list: " + queryAnswers.get(i));
				List<SearchFiles.Pair<String, Double>> results = SearchFiles.searchQueryIncludeTFIDF(queries.get(i), database,
						30, stopwords, 1, inverted_index);
                ClusterManager manager = new ClusterManager(results, inverted_index);
                System.out.println("Previous Cluster Size: "+ manager.clusters.size());
                manager.reduceClusterSize(20);
                System.out.println("Cluster size: " + manager.clusters.size());
		}
		System.out.println();

		//Gets avg precision over all queries
		return avgPrecSum / queries.size();
	}

	private static double MAP(String indexDir, String docsDir,
			String queryFile, String answerFile, int numResults,
			CharArraySet stopwords) {

		// Build Index
		IndexFiles.buildIndex(indexDir, docsDir, stopwords);

		// load queries and answer
		Map<Integer, String> queries = loadQueries(queryFile);

		//The actual correct relevant docs that should be retrieved
		Map<Integer, HashSet<String>> queryAnswers = loadAnswers(answerFile);

		// Search and evaluate
		double sum = 0;
		double avgPrecSum = 0;

		//Goes through each given query
		for (Integer i : queries.keySet()) {
				sum = 0;
				double answerSize = queryAnswers.get(i).size();
				//Get the relevant docs for the specific query
				HashSet<String> answer = queryAnswers.get(i);
				//System.out.println();
				//System.out.println("Relevent list: " + queryAnswers.get(i));
				List<String> results = SearchFiles.searchQuery(indexDir, queries
						.get(i), numResults, stopwords);
				//System.out.println("Retrieved list: " + results);

				//Goes through each retrieved doc
				for(int j = 0; j < results.size(); j++)
				{
					//Creates a list of results up to the specific retrieved doc
					List<String> temp = results.subList(0, j+1);

					//Creates a string for the specific retrieved doc
					String doc = results.get(j);

					//Checks to see if the specific retrieved doc is relevant
					if(answer.contains(doc))
					{
						sum += precision(answer, temp);
						//System.out.println("Relevent & retrieved list: " + doc);
					}
				}
				System.out.printf("\nTopic %d  ", i);
				//average precision = number of relevant relevant docs/# relevant docs
				double avgPrecision = sum/answerSize;
				avgPrecSum += avgPrecision;
				System.out.print("MAP = " + avgPrecision);
		}
		System.out.println();

		//Gets avg precision over all queries
		return avgPrecSum / (queries.size());
	}
}
