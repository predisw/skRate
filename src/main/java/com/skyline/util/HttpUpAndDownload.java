package com.skyline.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUpAndDownload {

	
	/**1.写入前查询一下文件夹内有没有重名的,如果有就在"."前加"-1".要确保线程安全,用DCL ?技巧?
	 * 
	 * @param req
	 * @return  返回一个map ,map 包含了 input 的 name 和value.其中 input 类型为file的input ,则是返回 nane 和上传的绝对路径
	 * 				名字相同的上传文件会被覆盖,不会提示覆盖
	 * @throws Exception 
	 * @throws IOException
	 */
	public static  Map<String,String> getUploadInput(HttpServletRequest req) throws Exception {
		
		Logger logger = LoggerFactory.getLogger(HttpUpAndDownload.class);
		File file ;
		int maxFileSize = 15000 * 1024;
		int maxMemSize = 6000 * 1024;
		
		String contextType=req.getContentType();
		String path=req.getServletContext().getRealPath("/upload");
		Map<String,String>  uploadInput=new HashMap<String, String>();
		if( ( contextType.indexOf("multipart/form-data")>=0 ) ){

			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(maxMemSize);//超过了这个大小则保存到临时文件夹
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(maxFileSize); //设置上传文件最大的大小

				List fileItem = upload.parseRequest(req);
				Iterator it = fileItem.iterator();
				while(it.hasNext()){
					FileItem fi=(FileItem)it.next();
					if(!fi.isFormField()){
						String fieldName = fi.getFieldName();
						String fileName=fi.getName();
						logger.debug("the form fieldName is [{}] and the value is [{}]",fieldName,fileName);
						if( fileName.lastIndexOf("\\") >= 0 ){
				            file = new File( path , fileName.substring( fileName.lastIndexOf("\\"))) ;
						}else{
				            file = new File( path ,fileName.substring(fileName.lastIndexOf("\\")+1)) ;
				            }

						String fileFullName=path+File.separator+file.getName();
						uploadInput.put(fieldName, fileFullName); //将upload 文件的绝对路径放到map 中
						 logger.debug("the store path of the upload file  is {}",fileFullName);
					    fi.write( file ) ;
						
					}else if(fi.isFormField()){
						String fieldName = fi.getFieldName();
						String fieldValue=fi.getString();
						uploadInput.put(fieldName,fieldValue);
						logger.debug("the form fieldName is [{}] and the value is [{}]",fieldName,fieldValue);
						
					}
				}
				
				return uploadInput;

		}
		//返回null，假如contextType不正确
		return uploadInput;
		
	}
	
	
		
	
	
	
	/**
	 * 
	 * @author predisw
	 *@param fileName 绝对路径文件名
	 */
	public static void downLoadFile(String fileName,HttpServletResponse res) throws IOException{

		String name_ori=fileName.substring(fileName.lastIndexOf(File.separator)+1);
		String name =URLEncoder.encode(name_ori, "UTF-8"); //对中文进行url 编码
		//读取目标文件
		FileInputStream in=new FileInputStream(fileName);  //必须设置在res.setXX之前,因为抛出异常之后,res 可能会继续传递到下一个action.这样子就会把设置好的参数传过去了.
		res.setCharacterEncoding("UTF-8");
		//设置文件MIME类型  
        res.setContentType("multipart/form-data");  
        //设置Content-Disposition  
        res.setHeader("Content-Disposition", "attachment;filename="+name);
        //读取目标文件，通过response将目标文件写到客户端  
   		

		OutputStream out = res.getOutputStream();
		//写文件  
        int b;  
        while((b=in.read())!= -1)  
        {  
            out.write(b);
        }  
        in.close();  
        out.close();  
		
		
	}
		
		
}
