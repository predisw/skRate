package com.skyline.service.imple;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.skyline.dao.BaseDao;
import com.skyline.dao.LogDao;
import com.skyline.dao.imple.LogDaoImple;
import com.skyline.pojo.Log;
import com.skyline.pojo.User;
import com.skyline.service.LogService;
import com.skyline.util.PageInfo;
@Service
public class LogServiceImple extends LogDaoImple  implements LogService {
	@Autowired
	private LogDao logDao;
	@Autowired
	private BaseDao baseDao;
	
	@Override
	public void modifyXml(Map<String, String> params, String fileName)
			throws DocumentException, java.io.IOException {
		// TODO Auto-generated method stub
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(fileName));
		Element root=document.getRootElement();
//		System.out.println(root.getName());
		List<Element> list=root.elements("logger"); //获取根元素 名为 logger 的子元素

//		修改子元素的属性
		for(Element ele:list){
			for(Map.Entry<String, String> entry:params.entrySet()){
				if(ele.attributeValue("name").equals(entry.getKey())){
					ele.attribute("level").setText(entry.getValue());
				}
			}
		}
		//写入文件
		OutputFormat format = OutputFormat.createPrettyPrint();   
	    format.setEncoding("UTF-8");    //设置编码格式,可以支持中文
		 FileOutputStream fos = new FileOutputStream(fileName);   
		 XMLWriter  writer = new XMLWriter(fos,format);
	    writer.write(document);   
	    writer.close();  
	    
	}

	@Override
	public Map<String, String> getParamsMap(String fileName) throws DocumentException {
		// TODO Auto-generated method stub
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(fileName));
		Element root=document.getRootElement();
//		System.out.println(root.getName());
		List<Element> list=root.elements("logger"); //获取根元素 名为 logger 的子元素
		Map<String, String> paramsMap=new HashMap<String, String>();
		for(Element ele:list){
			paramsMap.put(ele.attributeValue("name"), ele.attributeValue("level"));
		}
		
		return paramsMap;
	}

	@Override
	public List<String> getLogFileName(String logDir)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		File dir = new File(logDir);
		if(dir.isDirectory()){
			String[] logNames=dir.list();
			for(int i=0;i<logNames.length;i++){
				list.add(logNames[i]);
			}
		}else {
			throw new FileNotFoundException("文件夹不存在");
		}
		return list;
	}



	@Override
	public  void logToDb(String how,String what,String content){
		Log log = new Log();
		
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		User user =(User)req.getSession().getAttribute("user");
		Date time=new Date();
		
		log.setWho(user.getUName());
		log.setTime(time);
		log.setHow(how);
		log.setWhat(what);
		log.setContent(content);
		
		super.saveLog(log);

	}


}