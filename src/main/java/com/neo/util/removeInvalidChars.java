package com.neo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class removeInvalidChars {

	public static void main(String[] args) {
		String fileName = "【缴款】 接口?|*";
		 //1.文件名在操作系统中不允许出现  / \ " : | * ? < > 故将其以空替代
	      Pattern pattern = Pattern.compile("[\\s\\\\/:\\*\\?\\\"<>\\|]");
	      Matcher matcher = pattern.matcher(fileName);
	     //2.在创建文件前，对文件名进行合法性校验
	      if(!isValidFileName(fileName)) {
		      fileName= matcher.replaceAll(" "); // 将匹配到的非法字符以空替换
		      System.err.println("已替换非法字符！\n");
	      }
	      System.out.println(fileName);

	}
	
	public static boolean isValidFileName(String fileName) { if (fileName == null || fileName.length() > 255) return false; else return fileName.matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$"); }

}
