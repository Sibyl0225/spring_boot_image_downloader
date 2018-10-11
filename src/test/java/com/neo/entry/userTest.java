package com.neo.entry;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class userTest {

//	@Autowired
//	private UserMapper UserMapper;
//	
//	@Autowired
//	private MapMapper mapMapper;
	
	@Autowired
	private SqlSession sqlSession;
	
	@Test
	public void testInsert() throws Exception {
//		UserMapper.insert(new UserEntity("aa", "a123456", UserSexEnum.MAN));
//		UserMapper.insert(new UserEntity("bb", "b123456", UserSexEnum.WOMAN));
//		UserMapper.insert(new UserEntity("cc", "b123456", UserSexEnum.WOMAN));

//		Assert.assertEquals(3, UserMapper.getAll().size());
	}
	
	@Test
	public void testMapInsert() throws Exception {
//		mapMapper.insert(new HashMap<String,String>(){
//			private static final long serialVersionUID = 1L;
//		{put("C_SWJG_BM", "001");put("C_SWJG_MC", "中国第一");put("C_SWJG_JC", "第一");}});
//		mapMapper.insert(new HashMap<String,String>(){
//			private static final long serialVersionUID = 1L;
//		{put("C_SWJG_BM", "002");put("C_SWJG_MC", "中国第二");put("C_SWJG_JC", "第二");}});
//		mapMapper.insert(new HashMap<String,String>(){
//			private static final long serialVersionUID = 1L;
//		{put("C_SWJG_BM", "003");put("C_SWJG_MC", "中国第三");put("C_SWJG_JC", "第三");}});

//		Assert.assertEquals("第一", mapMapper.getOneByBm("001").get("C_SWJG_JC").toString());
//		
//		List<HashMap<String, String>> swjgs = mapMapper.getAll();
//		for (HashMap<String, String> swjg : swjgs.subList(swjgs.size()-3 , swjgs.size())) {
//			System.out.println(swjg);
//		}
	}
	
	@Test //动态sql语句
    public void selDongtai(){

        //Map<String,Object> map = new HashMap<String, Object>();
        //map.put("id",250l);
		HashMap<String,String> use = sqlSession.selectOne("com.neo.mapper.MapMapper.getOne",350l);
        //System.out.println(list.size() > 0 ? "无此用户":list.get(0));
        //HashMap<String, String> use = mapMapper.getOne(250L);
        System.out.println(use);
    }

}
