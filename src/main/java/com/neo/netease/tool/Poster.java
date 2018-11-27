package com.neo.netease.tool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.reflect.TypeToken;
import com.neo.yande.entity.Yande;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Poster {
	
	private static RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(15000)
            .setConnectTimeout(15000)
            .setConnectionRequestTimeout(15000)
            .build();
	
	private static final String calback = "jQuery1113018816295356722046_1543202774853";
	private static final String Search = "search";
	private static final String Url = "url";
	private static  String Source = "source";
	private static  int Limit = 20;
	private static  int total = 0;
	
	public static void main(String[] args) throws Exception{ 
		String keyword = "aimer";
		for(int i=0;i<= 1;i++) {
			List<Yande> yandes = getQueryListByKeyword(keyword,i);
			System.out.println(yandes);
		}

	}

	public static List<Yande> getQueryListByKeyword(String keyword, int j) throws UnsupportedEncodingException, InterruptedException {
		
		JsonArray JsonAry = postForList( keyword,j);
		List<Yande> yandes = new ArrayList<Yande>();
		for(int i=0;i<JsonAry.size();i++) {
//		    "id": 552448584,
//		    "name": "Name.",
//		    "artist": ["N.O.B.U!!!"],
//		    "album": "スタートライン",
//		    "pic_id": "109951163252765106",
//		    "url_id": 552448584,
//		    "lyric_id": 552448584,
//		    "source": "netease"
			JsonObject contacts = JsonAry.get(i).getAsJsonObject();
			String id = getValueToString(contacts,"id");//
			String name = getValueToString(contacts,"name");//
			String artist = getValueToString(contacts,"artist");//
			String album = getValueToString(contacts,"album");//
			String pic_id = getValueToString(contacts,"pic_id");//
			String url_id = getValueToString(contacts,"url_id");//
			String lyric_id = getValueToString(contacts,"lyric_id");//
			String source = getValueToString(contacts,"source");//
			String size = getValueToString(contacts,"size");//
			String url = getValueToString(contacts,"url");//	
			String br = getValueToString(contacts,"br");//				
	
			Yande yande = new Yande();
			yande.setImageFileSize(Integer.valueOf(size));
			yande.setImageName(name);
			yande.setPreviewImage(url);
			yandes.add(yande);
		}
		System.out.println("总共解析："+total+"首歌");
		return yandes;
	}


	private static JsonArray postForList(String keyword,int page) throws UnsupportedEncodingException, InterruptedException {
		HttpPost httpPost = new HttpPost("http://music.zhuolin.wang/api.php?callback=jQuery1113018816295356722046_1543202774853");
        setHead(httpPost);  
        httpPost.setHeader("X-Requested-With","XMLHttpRequest");	
        httpPost.setHeader("Referer","http://music.zhuolin.wang/");
        
		ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
        
        pairs = getKeyWordQueryNameValuePairs(keyword, Source, page, Limit);

        
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
        
        String response = sendHttpPost(httpPost);
        System.out.println("##########解析关键字搜索结果列表##########");  
        JsonArray queryList = parseKeyWordQueryListToListMap(response);
        
        for (int i = 0; i < queryList.size(); i++) {			     
        	postForUrl(queryList.get(i).getAsJsonObject());
        	//暂停30秒
			Thread.sleep(3000);
			System.out.println("暂停3秒！");
        	total++;
		}
        System.out.println(queryList);
        return queryList;
        
	}
	
	private static void postForUrl(JsonObject info) throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost("http://music.zhuolin.wang/api.php?callback=jQuery1113018816295356722046_1543202774853");
        setHead(httpPost);  
        httpPost.setHeader("X-Requested-With","XMLHttpRequest");	
        httpPost.setHeader("Referer","http://music.zhuolin.wang/");
        
		ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
  	
        	//获取歌曲地址
        	pairs = getUrlNameValuePairs(getValueToString(info, "id"));

        
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
        String response = sendHttpPost(httpPost);
        System.out.println("##########解析mp3地址##########");       
        JsonObject jsonObj = parseMp3ToMap(response,info);
	}

	private static JsonObject parseMp3ToMap(String response,JsonObject info) {
		if(StringUtils.isNotEmpty(response)&&response.startsWith(calback)) {
        	String data = response.substring(calback.length());
        	data = StringUtils.stripStart( data,"(");
        	data = StringUtils.stripEnd( data,")");
        	JsonObject JsonObj = new JsonParser().parse(data).getAsJsonObject();
            String br = getValueToString(JsonObj,"br");//比特率
            String size = getValueToString(JsonObj,"size");//比特率
            String url = getValueToString(JsonObj,"url");//比特率
            
            info.addProperty("br", br);
            info.addProperty("size", size);
            info.addProperty("url", url);
            System.out.println(info.toString());
            return JsonObj;
        }
		return null;
	}
	
	private static JsonArray parseKeyWordQueryListToListMap(String response) {
		if(StringUtils.isNotEmpty(response)&&response.startsWith(calback)) {
        	String data = response.substring(calback.length());
        	data = StringUtils.stripStart( data,"(");
        	data = StringUtils.stripEnd( data,")");
        	
            
			//Type type = new TypeToken<List<HashMap<String,Object>>>() {}.getType();
        	JsonArray JsonAry = new JsonParser().parse(data).getAsJsonArray();
			for(int i=0;i<JsonAry.size();i++) {
//			    "id": 552448584,
//			    "name": "Name.",
//			    "artist": ["N.O.B.U!!!"],
//			    "album": "スタートライン",
//			    "pic_id": "109951163252765106",
//			    "url_id": 552448584,
//			    "lyric_id": 552448584,
//			    "source": "netease"
				JsonObject contacts = JsonAry.get(i).getAsJsonObject();
				String id = getValueToString(contacts,"id");//
				String name = getValueToString(contacts,"name");//
				String artist = getValueToString(contacts,"artist");//
				String album = getValueToString(contacts,"album");//
				String pic_id = getValueToString(contacts,"pic_id");//
				String url_id = getValueToString(contacts,"url_id");//
				String lyric_id = getValueToString(contacts,"lyric_id");//
				String source = getValueToString(contacts,"source");//
		
				System.out.println(contacts.toString());
			}
            return JsonAry;
        }
		return null;
	}

	private static Gson getGson() {
		Gson gson = new GsonBuilder().setLongSerializationPolicy(LongSerializationPolicy.STRING).serializeSpecialFloatingPointValues().create();
		return gson;
	}

	private static String getValueToString(JsonObject contacts,String key) {
		if(contacts.has(key)) {
			JsonElement jsonElement = contacts.get(key);
			if(jsonElement.isJsonArray()) {	
				JsonArray jsonArray = jsonElement.getAsJsonArray();
				return jsonArray.toString();

			}else if(jsonElement.isJsonPrimitive()) {
				JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
				if(jsonPrimitive.isNumber()) {
					return jsonPrimitive.getAsString();
					
				}else if(jsonPrimitive.isString()) {
					return jsonPrimitive.getAsString();
				}
			}
		}
		return ""; 
	}
	
    public static String startUrl="http://hd.chinatax.gov.cn/xxk/";//违法案件
    public static String cookie="qd80-cookie=qdyy33-80; yfx_c_g_u_id_10003701=_ck18111215082419367923284815892; _gscu_244235366=42006789lpgzaq10; qd80-cookie=qdyy33-80; JSESSIONID=-9cRMJaDWs6Przi5klVnDcc3pprayvbF8MfZBt8w_3Un7o9dDvPU!-2087668943; taxCode=localtax; _Jo0OQK=464ED65473EA17F317132AFB165C64B9800AD963B41BE1F418F9D50B37A76958EF6290BABAFD3B8063DF57E4849273ABBAF7E72D41F52A28202816B9447C2A245C01B918CCA8FE3BB9470EE0D297309F84070EE0D297309F8405885FAEFBC52E21F629CFEE18B43F1A6GJ1Z1cA==; _gscbrs_627338063=1; _gscu_627338063=42006870e5zf0e17; yfx_f_l_v_t_10003701=f_t_1542006504889__r_t_1542159353576__v_t_1542186220828__r_c_2";
    public static int curTax=0,curPage=0;
    
    //设置httpGet的头部参数信息 
    public static void setHead(HttpRequestBase request){
    	request.setHeader("Accept", "Accept text/javascript,application/javascript, application/ecmascript, application/x-ecmascript,*/*;q=0.01");
    	request.setHeader("Accept-Encoding", "gzip,deflate");  
    	request.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
    	request.setHeader("Connection", "keep-alive"); 
    	//Content-Length: 55
    	request.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
    	request.setHeader("Cookie", "UM_distinctid=1674a6af0ad230-041d0e6676c4cb-2711938-100200-1674a6af0ae7a5; CNZZDATA1260050386=1928895977-1543139953-http%253A%252F%252Flink.zhihu.com%252F%7C1543199318");
    	request.setHeader("Host", "music.zhuolin.wang");
    	//Origin: http://music.zhuolin.wang

    	request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36");
	}
    
	/**
	 * 获取mp3文件地址
	 * 
	 * @param id
	 * @return
	 */
	public static ArrayList<BasicNameValuePair> getUrlNameValuePairs(String id) {
		ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("types", "url"));
		pairs.add(new BasicNameValuePair("id", id));
		return pairs;
	}

	/**
	 * 关键词搜索
	 * 
	 * @param id
	 * @return
	 */
	public static ArrayList<BasicNameValuePair> getKeyWordQueryNameValuePairs(String keyword,String source,int page,int limit) {
		ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("types", "search"));
		pairs.add(new BasicNameValuePair("count", limit+""));
		pairs.add(new BasicNameValuePair("source", source));
		pairs.add(new BasicNameValuePair("pages", page+""));
		pairs.add(new BasicNameValuePair("name", keyword));
		return pairs;
	}
    
    
	/**
	 * 发送Post请求
	 * @param httpPost
	 * @return
	 */
	private static String sendHttpPost(HttpPost httpPost) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpPost.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpPost);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}
	
    //获取响应字符串
    public static String getResponseString(HttpResponse response) throws ParseException, IOException {
    	Header[] headers = response.getHeaders("Content-Encoding");
        boolean isGzip = false;
        for(Header h:headers){
            if(h.getValue().equals("gzip")){
                    //返回头中含有gzip
                    isGzip = true;
               }
        }
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
			String content ="";
			if (isGzip==true)
				content= EntityUtils.toString(new GzipDecompressingEntity(resEntity));
			else
				content= EntityUtils.toString(resEntity);
			return content;
		}
        return null;
    }
    
//    /**
//     * 获取 HttpClient
//     * @param host
//     * @param path
//     * @return
//     */
//    private static HttpClient wrapClient(String host,String path) {
//        HttpClient httpClient = HttpClientBuilder.create().build();
//        if (host != null && host.startsWith("https://")) {
//            return sslClient();
//        }else if (StringUtils.isBlank(host) && path != null && path.startsWith("https://")) {
//            return sslClient();
//        }
//        return httpClient;
//    }

}
