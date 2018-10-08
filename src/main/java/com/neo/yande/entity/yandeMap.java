package com.neo.yande.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class yandeMap extends HashMap<String, Object> {

	private static final long serialVersionUID = -3103572308064455531L;

	public Yande mapToYande(Map<String,?> map) throws IllegalAccessException, InvocationTargetException {
		Yande yande = new Yande();
		org.apache.commons.beanutils.BeanUtils.populate(yande, map);
		return yande;
	}
}
