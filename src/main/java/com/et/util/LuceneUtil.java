package com.et.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.TextFragment;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class LuceneUtil {
	static String dir="E:\\index";
	 //定义分词器
	static Analyzer analyzer = new IKAnalyzer();
	/**
	 * 创建索引
	 * @throws IOException 
	 */
	public static void write(Document doc) throws IOException{
		//索引库的存储目录
		Directory directory = FSDirectory.open(new File(dir));
		//关联lucene版本和当前分词器
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
		//创建索引 （传入写入的目录和分词器）
		IndexWriter iwriter = new IndexWriter(directory, config);
		iwriter.addDocument(doc);
		//提交事务
		iwriter.commit();
		iwriter.close();
	}
	/**
	 * 搜索索引
	 * @throws Exception 
	 */
	public static List<Map> search(String field,String value) throws Exception {
		
		//索引库的存储目录
		Directory directory = FSDirectory.open(new File(dir));
		//读取索引库的存储目录
		DirectoryReader ireader = DirectoryReader.open(directory);
		//搜索类
		IndexSearcher isearcher = new IndexSearcher(ireader);
		//lucene的查询解析器 用于指定查询的属性名和分词器
		QueryParser parser = new QueryParser(Version.LUCENE_47, "foodname", analyzer);
		//开始搜索
	    Query query = parser.parse(value);
	    //创建lucene的高亮对象  最终结果被分词添加前缀和后缀的处理类
	    SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<font color=red>","</font>");
	    //高亮搜索的词 添加到高亮处理器中
	    Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));
	    //用来存储等分高的document  获取搜索的结果 指定返回的document个数
	    ScoreDoc[] hits = isearcher.search(query, null, 10).scoreDocs;
	    List<Map> list=new ArrayList<Map>();
	    //从ScoreDoc数组中获取单独的document
	    for (int i = 0; i < hits.length; i++) {
	      //获取document的编号
	      int id = hits[i].doc;
	      Document hitDoc = isearcher.doc(hits[i].doc);
	      Map map=new HashMap();
	      map.put("foodid", hitDoc.get("foodid"));
	      //获取你要高亮的词field
	     // String text = doc.get("notv");
	      String foodname=hitDoc.get("foodname");
	      //将查询的结果和搜素的次匹配添加前缀和后缀
	      TokenStream tokenStream = TokenSources.getAnyTokenStream(isearcher.getIndexReader(), id, "foodname", analyzer);
	      TextFragment[] frag = highlighter.getBestTextFragments(tokenStream, foodname, false, 10);
	      String foodNameHign="";
	      for (int j = 0; j < frag.length; j++) {
	        if ((frag[j] != null) && (frag[j].getScore() > 0)) {
	        	foodNameHign=((frag[j].toString()));
	        }
	      }
	      //map中添加被高亮之后的值foodNameHign
	      map.put("foodname", foodNameHign);
	    // map.put("foodname", hitDoc.get("foodname"));
	      map.put("price", hitDoc.get("price"));
	      map.put("imagepath", hitDoc.get("imagepath"));
	      list.add(map);
	    }
	    ireader.close();
	    directory.close();
	    return list;
	}
}
