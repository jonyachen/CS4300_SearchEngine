import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;


public class IndexGenerator {
	private String indexPath;
	private String docsPath;
	private String indexName;
	private CharArraySet stopwords;
	private Analyzer analyzer;

	// Assumes index/docsPaths are clean
	
	public IndexGenerator(String indexPath, String docsPath, String indexName,CharArraySet stopwords){
		this.indexPath = indexPath;
		this.docsPath = docsPath;
		this.indexName = indexName;
		this.stopwords = stopwords;
		analyzer = new MyAnalyzer(stopwords);
	}
	
	public void generateIndex(){
		File folder = new File(docsPath);
		File doc = new File(indexPath+"/"+indexName+".csv");
		
		// Deletes file if it exists
		if(doc.exists()){
			doc.delete();
		}
		PrintWriter writer = null;
		
		try {
			writer = new PrintWriter(indexPath+"/"+indexName+".csv");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Looks at all the files in the docsPath
		for(String file: folder.list()) {
			generateForDoc(docsPath+"/"+file, file, writer);
		}
		writer.close();
	}
	
	private void generateForDoc(String document, String fileName, PrintWriter writer){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(document));
			TokenStream stream = analyzer.tokenStream("", reader);
			CharTermAttribute cattr = stream.addAttribute(CharTermAttribute.class);
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			
			stream.reset();
			// Reads from file token by token
			while(stream.incrementToken()) {
				int count = 1;
				if(map.containsKey(cattr.toString())){
					count = map.get(cattr.toString())+1;
				}
				map.put(cattr.toString(), count);
			}
			
			String toWrite = fileName+",";
			for(String key: map.keySet()){
				toWrite += key+"&&"+map.get(key)+",";
			}
			writer.println(toWrite.substring(0,toWrite.length()-1));
			
			stream.end();
			stream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
