package com.neo.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.autoconfigure.social.LinkedInAutoConfiguration;
import org.springframework.http.converter.json.GsonBuilderUtils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neo.yande.entity.Const;
import com.neo.yande.entity.RedisClient;

import redis.clients.jedis.Jedis;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by Administrator on 2018/05/09.
 */
public class HttpClientUtil {

    private static HttpClientContext context = HttpClientContext.create();
    private static RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(12000).setSocketTimeout(15000)
            .setConnectionRequestTimeout(12000).setCookieSpec(CookieSpecs.STANDARD_STRICT).
                    setExpectContinueEnabled(true).
                    setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST)).
                    setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();

    //https
    private static SSLConnectionSocketFactory socketFactory;
    private static TrustManager manager = new X509TrustManager() {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    private static void enableSSL() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{manager}, null);
            socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private static HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
        @Override
        public boolean retryRequest(IOException exception,
                                    int executionCount, HttpContext context) {
            return false;
        }};
    static {
        enableSSL();
    }
    private static Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", socketFactory).build();

    private static PoolingHttpClientConnectionManager pccm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
    static {
        pccm.setMaxTotal(100); // 连接池最大并发连接数
        pccm.setDefaultMaxPerRoute(20); // 单路由最大并发数
    }
    private static HttpClientBuilder clientBuilder = HttpClients.custom().setConnectionManager(pccm).
            setConnectionManagerShared(true).setRetryHandler(myRetryHandler)
            .setDefaultRequestConfig(requestConfig);
    /**
     * https get
     * @param url
     * @param data
     * @return
     * @throws IOException
     */
    public static CloseableHttpResponse doGet(String url, String data){
        CloseableHttpClient httpClient = clientBuilder.build();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet, context);
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }


    /**
     * https/http post
     * @param url
     * @param values
     * @return
     * @throws IOException
     */
    public static CloseableHttpResponse doPost(String url, List<NameValuePair> values) {
        CloseableHttpClient httpClient = clientBuilder.build();
        HttpPost httpPost = new HttpPost(url);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(values, Consts.UTF_8);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost, context);
        }catch (Exception e){}
        return response;
    }


    public static CloseableHttpResponse doJsonPost(String url,String json){
        CloseableHttpClient httpClient = clientBuilder.build();

        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse httpResponse;
        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
        httpPost.addHeader("charset", "utf-8");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost, context);
        }catch (Exception e){}
        return response;
    }


    /**
     * 直接把Response内的Entity内容转换成String
     *
     * @param httpResponse
     * @return
     */
    public static String toString(CloseableHttpResponse httpResponse) {
        if(httpResponse == null) return null;
        // 获取响应消息实体
        String result = null;
        try {
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity,"UTF-8");
            }
        }catch (Exception e){}finally {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;


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
	
	public static LinkedList<HashMap<String, Object>> zhuolinApiSearch() {
		final String callback = "jQuery1113048840066041851693_1556587354327"; 
		final String url ="http://music.zhuolin.wang/api.php?callback="+callback;
		final String songOrSingerName = "aimer";
		Map<String, String> map = new HashMap<String,String>();
		map.put("types","search");
		map.put("count","20");
		map.put("source","netease");
		map.put("pages","5");
		map.put("name",songOrSingerName);

		
        List<NameValuePair> values = new ArrayList<NameValuePair>();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, String> elem = (Entry<String, String>) iterator.next();
            values.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
        }
        CloseableHttpClient httpClient = clientBuilder.build();
        HttpPost httpPost = new HttpPost(url);
        
        httpPost.setHeader("Accept","text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
		httpPost.setHeader("Accept-Encoding","gzip, deflate");
		httpPost.setHeader("Accept-Language","zh-CN,zh;q=0.9");
		httpPost.setHeader("Connection","keep-alive");
		httpPost.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
		httpPost.setHeader("Cookie","UM_distinctid=16a6bd3ff5711a-0028170eb5a062-e323069-100200-16a6bd3ff584e5; CNZZDATA1260050386=1929486278-1556584964-%7C1556584964");
		httpPost.setHeader("DNT","1");
		httpPost.setHeader("Host","music.zhuolin.wang");
		httpPost.setHeader("Origin","http://music.zhuolin.wang");
		httpPost.setHeader("Referer","http://music.zhuolin.wang/");
		httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");
		httpPost.setHeader("X-Requested-With","XMLHttpRequest");
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(values, Consts.UTF_8);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost, context);
        }catch (Exception e){
        	e.printStackTrace();
        }
        HttpEntity responseEntity = response.getEntity();
        
    	try {
    		
			String responseContent = EntityUtils.toString(responseEntity, Consts.UTF_8);
			if(StringUtils.isNotEmpty(responseContent) && responseContent.startsWith(callback)) {
	        	String data = responseContent.substring(callback.length());
	        	data = StringUtils.stripStart( data,"(");
	        	data = StringUtils.stripEnd( data,")");
	        	ObjectMapper objectMapper = new ObjectMapper();
	        	JavaType jvt = objectMapper.getTypeFactory().constructParametricType(LinkedList.class, HashMap.class);
	        	LinkedList<HashMap<String, Object>> lists= objectMapper.readValue(data, jvt);
	        	return lists;	        	
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
	}
	
	public static HashMap<String, Object> zhuolinApiSongInfo(String id) {
		final String callback = "jQuery1113048840066041851693_1556587354327"; 
		final String url ="http://music.zhuolin.wang/api.php?callback="+callback;
        CloseableHttpClient httpClient = clientBuilder.build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("X-Requested-With","XMLHttpRequest");	
        httpPost.setHeader("Referer","http://music.zhuolin.wang/");
        
		ArrayList<BasicNameValuePair> values = new ArrayList<BasicNameValuePair>();
		values.add(new BasicNameValuePair("types", "url"));
		values.add(new BasicNameValuePair("id", id));
		values.add(new BasicNameValuePair("source", "netease"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(values, Consts.UTF_8);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost, context);
        }catch (Exception e){
        	e.printStackTrace();
        }
        HttpEntity responseEntity = response.getEntity();       
    	try {
    		
			String responseContent = EntityUtils.toString(responseEntity, Consts.UTF_8);
			if(StringUtils.isNotEmpty(responseContent) && responseContent.startsWith(callback)) {
	        	String data = responseContent.substring(callback.length());
	        	data = StringUtils.stripStart( data,"(");
	        	data = StringUtils.stripEnd( data,")");
	        	ObjectMapper objectMapper = new ObjectMapper();
	        	JavaType jvt = objectMapper.getTypeFactory().constructParametricType(HashMap.class,String.class,Object.class);
	        	HashMap<String, Object> songInfo = objectMapper.readValue(data, jvt);
	        	return songInfo;	        	
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
	}
	


    public static void main(String[] args){
    	RedisClient redisClient = new RedisClient(Const.redis_song);
    	Jedis jedis = redisClient.jedisPool.getResource();
    	LinkedList<HashMap<String, Object>> list = zhuolinApiSearch();
    	for (int i = 0; i < list.size(); i++) {
    		HashMap<String, Object> song = list.get(i);
			String url_id = song.get("url_id")+"";
			HashMap<String, Object> songDataInfo = zhuolinApiSongInfo(url_id);
			song.putAll(songDataInfo);
			HashMap<String, String> soingRedisSaveMap = new HashMap<String,String>();
			for(Iterator<Entry<String, Object>> iterator = song.entrySet().iterator();iterator.hasNext();){
				Entry<String, Object> entry = iterator.next();
				soingRedisSaveMap.put(entry.getKey(), entry.getValue()+"");
			}
			String hmsetFlag = jedis.hmset(url_id, soingRedisSaveMap);
			System.out.println(song.get("name")+" "+hmsetFlag);
			
			
		}
    }
    
    
//    public static void main(String[] args){
////      CloseableHttpResponse response = HttpClientUtil.doGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxb2ebe42765aad029&secret=720661590f720b1f501ab3f390f80d52","");
////      System.out.println(HttpClientUtil.toString(response));
////
////      response = HttpClientUtil.doPost("http://www.baidu.com/cgi-bin/token?grant_type=client_credential&appid=wxb2ebe42765aad029&secret=720661590f720b1f501ab3f390f80d52",
////              new ArrayList<NameValuePair>());
////      System.out.println(HttpClientUtil.toString(response));
//      String url ="https://files.yande.re/image/d9217536a23994c49af98b07036b14cf/yande.re%20523145%20akashio%20bottomless%20breast_hold%20girls_und_panzer%20kay_%28girls_und_panzer%29%20sweater.jpg";
//      CloseableHttpResponse response = doGet(url,"");
//      HttpEntity responseEntity = response.getEntity();
//      boolean isStream = responseEntity.isStreaming();
//
//      System.out.println("流文件："+isStream);
//      if(isStream){
//          ByteArrayOutputStream baos = new ByteArrayOutputStream();
//          String name = FilenameUtils.getName(url);
//          try {
//              name= URLDecoder.decode(name, "UTF-8");
//          } catch (UnsupportedEncodingException e) {
//              e.printStackTrace();
//          }
//          File file = new File("E:/yandeTest/"+name);
//          try {
//              FileOutputStream fileOutputStream = FileUtils.openOutputStream(file, false);
//              responseEntity.writeTo(fileOutputStream);
//              fileOutputStream.flush();
//              fileOutputStream.close();
//          } catch (IOException e) {
//              e.printStackTrace();
//          }
//      }
//
//
////      void	consumeContent()
////                                              Deprecated. (4.1) Use EntityUtils.consume(HttpEntity)
////      InputStream	getContent()
////                                              Returns a content stream of the entity.
////      Header	getContentEncoding()
////                                              Obtains the Content-Encoding header, if known.
////      long	getContentLength()
////                                              Tells the length of the content, if known.
////      Header	getContentType()
////                                              Obtains the Content-Type header, if known.
////      boolean	isChunked()
////                                              Tells about chunked encoding for this entity.
////      boolean	isRepeatable()
////                                              Tells if the entity is capable of producing its data more than once.
////      boolean	isStreaming()
////                                              Tells whether this entity depends on an underlying stream.
////      void	writeTo(OutputStream outstream)
////                                               Writes the entity content out to the output stream.
//  }
}
