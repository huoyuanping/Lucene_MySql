package com.et.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.et.dao.FoodDao;
import com.et.service.FoodService;
@Service
public class FoodServiceImpl implements FoodService{
	@Autowired
	FoodDao dao;
	
	/**
	 *分页获取数据
	 */
	@Override
	public List<Map<String, Object>> queryFood(int start, int rows) {
		return dao.queryFood(start, rows);
	}
	
	/**
	 * 获取总行数
	 */
	@Override
	public int queryFoodCount() {
		return dao.queryFoodCount();
	}

}
