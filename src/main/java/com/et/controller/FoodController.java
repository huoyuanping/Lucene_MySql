package com.et.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.et.service.FoodService;
import com.et.util.LuceneUtil;

@RestController
public class FoodController {
	@Autowired
	private FoodService service;
	@GetMapping("/searchFood")
	public List<Map> getFood(String keyWord) throws Exception{
		
		return  LuceneUtil.search("foodname", keyWord);
	}
	@GetMapping("/createIndex")
	public String createIndex(){
		try {
			//数据库查询数据 查询数据批量查询
			int queryFoodCount =service.queryFoodCount();
			//第一次0--1000
			//第二次1001--2000
			//开始位置
			int startIndex=0;
			//总行数
			int rows=1000; 
			//批量
			while(startIndex<=queryFoodCount){
				//每次拉取的数据
				List<Map<String, Object>> queryFood=service.queryFood(startIndex, rows);
				for(int i=0;i<queryFood.size();i++){
					Map<String, Object> mso=queryFood.get(i);
					Document  doc=new Document();
					Field field1=new Field("foodid",mso.get("foodid").toString(),TextField.TYPE_STORED);
					Field field2=new Field("foodname",mso.get("foodname").toString(),TextField.TYPE_STORED); 
					Field field3=new Field("price",mso.get("price").toString(),TextField.TYPE_STORED); 
					Field field4=new Field("imagepath",mso.get("imagepath").toString(),TextField.TYPE_STORED); 
					doc.add(field1);
					doc.add(field2);
					doc.add(field3);
					doc.add(field4);
					//写入lucene索引中
					LuceneUtil.write(doc);
				}
				startIndex+=1+rows;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "0";
		}
		return "1";
	}
}
