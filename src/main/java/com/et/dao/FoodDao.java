package com.et.dao;

import java.util.List;
import java.util.Map;

public interface FoodDao {
	/**
	 *分页获取数据
	 */
	public List<Map<String, Object>> queryFood(int start,int rows);
	/**
	 * 获取总行数
	 */
	public int queryFoodCount();	
	
}
