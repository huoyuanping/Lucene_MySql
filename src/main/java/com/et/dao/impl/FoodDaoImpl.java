package com.et.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.et.dao.FoodDao;
@Repository
public class FoodDaoImpl implements FoodDao{
	@Autowired
	private JdbcTemplate jdbc;
	
	/**
	 *分页获取数据
	 */
	public List<Map<String, Object>> queryFood(int start, int rows) {
		String sql="select * from food limit "+start+","+rows;
		return jdbc.queryForList(sql);
	}
	
	/**
	 * 获取总行数
	 */
	@Override
	public int queryFoodCount() {
		String sql="select count(*) as foodCount from food";
		return Integer.parseInt(jdbc.queryForList(sql).get(0).get("foodCount").toString());
	}

}
