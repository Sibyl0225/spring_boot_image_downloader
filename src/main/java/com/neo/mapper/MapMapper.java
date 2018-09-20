package com.neo.mapper;

import java.util.HashMap;
import java.util.List;

import com.neo.entity.UserEntity;

public interface MapMapper {
	
	List<HashMap<String,String>> getAll();
	
	HashMap<String,String> getOne(Long id);
	
	HashMap<String,String> getOneByBm(String bm);

	void insert(HashMap<String,String> map);

	void update(HashMap<String,String> map);

	void delete(Long id);

}