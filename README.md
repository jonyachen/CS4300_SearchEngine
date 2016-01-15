cs4300
======
CS 4300 Project 2
Group members: Jonya Chen (jc957), Yuxiao Tan (yt358), Valerie Hu (vh88)

Included in this folder, mini, are all the files needed to run our mini search engine for Project 2. The code in the project has a search engine implemented to re-rank documents retrieved for queries in two collections, CACM & Medlars, and to find MAP values of the collections. We have provided the following files in this folder:

EvaluateQueries.java - Retrieves the relevant documents, calculates corresponding MAP evaluation figures for all retrievals
IndexFiles.java - Retrieves tokens, creates index for documents and terms
IndexGenerator.java - Generates the index file
MyAnalyzer.java - Implements the tokenizing and stemming of files
SearchFiles.java - Takes the indexer and generates a set of retrieved documents using specified weighting method
WeightedTFIDF.java - For our own search engine, calculates the tf*idf weight per query-document pair
Cluster.java - Calculates the distance between two clusters
ClusterManager.java - Merges clusters until there are K clusters, where K is specified by Part 2a and 2b prompts
DocumentPair.java - Calculates cosine similarities between two documents

In order to run the search engine, just run EvaluateQueries and it will use our search engine to retrieve documents per query and re-rank documents for various scenarios and a resulting MAP measure. Each of the scenarios correspond to a different re-ranking method, such as pseudo-relevance feedback, complete link clustering, and Rocchio relevance feedback. Using our search engine, it will then print out the following outputs:

Currently, in this submission, the implementation is set up so that output for Part 1a for both the CACM and MED collections is displayed. 
Note: You can switch between the different methods by commenting out certain parts of the output code in EvaluateQueries.java, between lines 167-222.

The outputs printed for ALL scenarios can be found in:
/foo/CS4300_Project2_jc957.yt358.vh88/output.txt


