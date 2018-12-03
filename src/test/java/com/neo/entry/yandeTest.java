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

import com.neo.domain.UserRepository;
import com.neo.domain.YandeRepository;
import com.neo.entity.YandeEntry;
import com.neo.mapper.YandeMapper;
import com.neo.util.DateUtil;
import com.neo.yande.entity.Yande;


@RunWith(SpringRunner.class)
@SpringBootTest
public class yandeTest {

	@Autowired
	private YandeMapper yandeMapper;

	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	private YandeRepository yandeRepository;
	
	@Test
	public void testInsert() throws Exception {
		YandeEntry yande = new YandeEntry();
        yande.setImageId("p493351");
        yande.setImageName("yande.re 493351 ass garter halloween izumiyuhina tagme tail thighhighs wings witch.jpg");
        yande.setLargeSizeImage("https://files.yande.re/jpeg/6d8b73c9188f04850d087f1172065f6a/yande.re%20497515%20tagme.jpg");
        yande.setPreviewImage("https://assets.yande.re/data/preview/6d/8b/6d8b73c9188f04850d087f1172065f6a.jpg");
        yande.setImageResolution("1920x1080");
        yande.setCreateDate(DateUtil.getDate());
        yande.setHadDownload(false);
        yande.setImageFileSize(512000);
        yandeMapper.insert(yande);
	}
	
	@Test
	public void findePid() {
		List<Yande> images = yandeRepository.findByImageId("p493351");
		if(images != null && images.size() > 0) {
			images.forEach(image ->{
				System.out.println(image.toString());
			});
		}
	}
	
	@Test //动态sql语句
    public void selDongtai(){

		HashMap<String,String> use = sqlSession.selectOne("com.neo.mapper.MapMapper.getOne",350l);
        System.out.println(use);
    }

}
