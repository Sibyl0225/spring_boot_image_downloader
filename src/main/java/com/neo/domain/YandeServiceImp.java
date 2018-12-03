package com.neo.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neo.entity.YandeEntry;
import com.neo.mapper.YandeMapper;

@Service
public class YandeServiceImp implements YandeService {

	@Autowired
	private YandeMapper yandeMapper;

	@Override
	@Transactional
	public YandeEntry insertYande(YandeEntry yande) {
		yandeMapper.insert(yande);
		return yande;
	}
   
	public static void main(String[] args) {
		
	}
}