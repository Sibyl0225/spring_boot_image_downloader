package com.neo.yande.downLoader;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.neo.yande.entity.Const;
import com.neo.yande.entity.Yande;


public class YandeParse implements YandeParseInterface {

	private int retrayTimes = 0;
	
	private static Logger logger = LogManager.getLogger(YandeParse.class.getName());
	
	public YandeParse() {
		
	}

	@Override
	public List<Yande> getListFromYande(int page) {
		String yandeUrl = new String(Const.yande_url);
		if (page == 0 || page == 1)
			yandeUrl += "/post";
		if (page > 1)
			yandeUrl += "/post?page=" + page;
		long start = System.currentTimeMillis();
		List<Yande> yandes = new ArrayList<Yande>();
		Document doc = null;
		Elements selectlis = null;
		try {
			doc = Jsoup.connect(yandeUrl).get();
			logger.info("获取数据完毕，开始解析html……");
			if(doc == null) {
				logger.error("http error,为接收到返回数据！");
				throw new Exception();
			} 
			selectlis = doc.select("img.preview");
			logger.info("解析html完毕,遍历子节点……");
			
		} catch (Exception e) {
			logger.error("解析              " + yandeUrl + "            失败！");
			logger.error("重试！");
			retrayTimes++;
			if (retrayTimes <= 3) {
				getListFromYande(page);	
			}else {
				e.printStackTrace();
			}			
		} finally {
			logger.info("Time is:" + (System.currentTimeMillis() - start) / 1000 + "s");
		}

		
		
		if(selectlis == null || selectlis.size()<1) {
			logger.error("网页标签格局更变，请重写Jsoup parse业务！");
            return yandes;
		} 

		for (Element element : selectlis) {
			Element parent = element.parent().parent().parent();
			Yande yande = new Yande();
			// id
			if (parent.hasAttr("id")) {
				String imgId = parent.attr("id");
				yande.setImageId(imgId);
			}
			// 预览图
			String preImg = element.attr("src");
			// logger.info(preImg);
			yande.setPreviewImage(preImg);

			Elements children = parent.children();
			if (children.hasClass("directlink")) {
				Elements directlink = children.select("a.directlink");
				// 大图
				String largeImg = directlink.attr("href");
				yande.setLargeSizeImage(largeImg);
				String imageName = FilenameUtils.getName(largeImg);
				try {
					String decodeImageName = URLDecoder.decode(imageName,"UTF-8");
					yande.setImageName(decodeImageName);
				} catch (UnsupportedEncodingException e) {
					yande.setImageName(imageName);
					e.printStackTrace();
				}
				// logger.info(largeImg);
				// 大图尺寸
				String imgSize = directlink.select("span.directlink-res").html();
				yande.setImageResolution(imgSize);
			}
			
			yande.setCreateDate(new SimpleDateFormat().format(new Date()));
			yandes.add(yande);
			logger.info(yande);
		}
		return yandes;
	}

}
