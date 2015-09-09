package com.skyline.service.imple;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyline.dao.BaseDao;
import com.skyline.dao.CustomerDao;
import com.skyline.pojo.Customer;
import com.skyline.pojo.Email;
import com.skyline.service.JavaMailService;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
@Service
public class JavaMailServiceImple implements JavaMailService {
	@Autowired
	private BaseDao baseDao;
	/**
	 * 邮件内容中支持 上传插入多图片
	 * 发送邮件的几个步骤
	 * 1.创建session
	 * 2.创建邮件消息体message
	 * 3.连接smtp 服务器,发送
	 * 
	 * @throws MessagingException 
	 * @throws AddressException 
	 * @param fileName 是附件的绝对路径,带有后缀名
	 * @param uploadPath 图片上传的绝对路径
	 * @param bccList 是密送的地址,用逗号"," 隔开多个地址
	 */
	
	@Override
	public boolean sendMail(Email email ) throws IOException, MessagingException{
		//加载properties 文件的属性
				InputStream in = this.getClass().getResourceAsStream("/javaMail.properties");
				Properties props = new Properties();
				props.load(in);
				//读取部分非默认属性
				String host =props.getProperty("mail.smtp.host");
				String port =props.getProperty("mail.smtp.port");
				String user=props.getProperty("mail.sender.username");
				String password=props.getProperty("mail.sender.password");
				String debug=props.getProperty("debug");
				

				//创建发送邮件的session
				Session session = Session.getInstance(props);
				session.setDebug(Boolean.parseBoolean(debug));  //设置调试信息
				
				//------------创建消息体------
				String from=props.getProperty("mail.sender.username");
			//	String subject="Rate Change";
			//	String content="Hello,here is ChinaSkyline.I am sorry to tell you that the Rate is changed again ,Please notice that carefully";
				InternetAddress[] to=InternetAddress.parse(email.getAddrList()); // 是以","逗号分割的邮件地址字符串,或者单个邮箱地址
				MimeMessage message= new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				message.setRecipients(Message.RecipientType.TO, to);
				
				if(email.getCcAdddrList()!=null && !"".equals(email.getCcAdddrList())){ //cc 是抄送 
					InternetAddress[] toCc=InternetAddress.parse(email.getCcAdddrList());
					message.setRecipients(Message.RecipientType.CC, toCc);
				}
				if(email.getBccAddrList()!=null && !"".equals(email.getBccAddrList())){ //bcc 是密送 
					InternetAddress[] toBcc=InternetAddress.parse(email.getBccAddrList());
					message.setRecipients(Message.RecipientType.BCC, toBcc);
				}
				
				message.setSubject(email.getSubject());
				String content=email.getContent();
				
				//创建用于组合文本消息体和 附件的mimeMultiPart 对象 
				MimeMultipart multiPart= new MimeMultipart("mixed");
				
				//正文
				MimeBodyPart contentPart= new MimeBodyPart();
				
				int img_begin=content.indexOf("<img");
				int img_end=content.indexOf("/>", img_begin);

				
				if(img_begin>0){ //假如存在插入图片(通过<img 来判断)
				
					MimeMultipart contentMultiPart = new MimeMultipart("related");
					MimeBodyPart htmlBodyPart= new MimeBodyPart();
		
				
					while(img_begin>0){ //用于处理多幅图片
						String img_html=content.substring(img_begin, img_end);//带有<img 标签的img 地址
						// 如<img alt="" src="http://localhost:8090/SkylineRate/upload//sk2645天一泓logo.jpg" style="height:900px; width:967px"
						
						int img_path_begin=img_html.indexOf("src=\"")+5;
						String img_name=img_html.substring(img_path_begin,img_html.indexOf("\"", img_path_begin));
						
						String fullPathName=email.getResourcePath()+img_name.substring(img_name.lastIndexOf("/"));
						//缩放图片
						if(img_html.contains("width")){
							int width_begin = img_html.indexOf("width:")+6;
							int height_begin = img_html.indexOf("height:")+7;
							String str_width = img_html.substring(width_begin,img_html.indexOf("px", width_begin));
							String srt_height = img_html.substring(height_begin,img_html.indexOf("px",height_begin));
							//对图片进行缩放并替换掉原来的图片
							this.zoomImg(fullPathName, fullPathName, Integer.parseInt(str_width), Integer.parseInt(srt_height));
						}
						
						String chars = "0123456789"; 
					  	char[] rands = new char[4]; 
					  	for (int i = 0; i < 4; i++) 
					  	     { 
					  	         int rand = (int) (Math.random() * 10); 
					  	         rands[i] = chars.charAt(rand); 
					  	     } 
					  	String randomS=String.valueOf(rands); //把随机生成的四个整形数字变成String型
						
						String img_mime="<img src=\"cid:"+randomS+"\"";
						content=content.replace(img_html, img_mime);
						
						contentMultiPart.removeBodyPart(htmlBodyPart); //移除之前的htmlBodyPart,下面重新添加
						
						htmlBodyPart.setContent(content, "text/html;charset=utf8");
						contentMultiPart.addBodyPart(htmlBodyPart); 

						//创建图片的包含图片的mime体
						MimeBodyPart imgPart= new MimeBodyPart();
						FileDataSource img_fds= new FileDataSource(fullPathName);  //图片的绝对路径
						imgPart.setDataHandler(new DataHandler(img_fds));
						imgPart.setContentID(randomS);
						contentMultiPart.addBodyPart(imgPart);//new MimeMultipart("related"); 可以包含多个图片 
					
						img_begin= content.indexOf("<src", img_end); //用下一个图片的位置代替上一个.如果这个不为-1 ,则表名还存在下一个图片,则需要循环处理
						img_end=content.indexOf("/>", img_begin);
						
					}


					contentPart.setContent(contentMultiPart);
					
					
				}else{
					contentPart.setContent(content, "text/html;charset=utf8");
					
				}
				
				
				
				
				//附件
				MimeBodyPart attachPart= new MimeBodyPart();  //包含附件
				FileDataSource fds= new FileDataSource(email.getAttacheFullName());  //传进来的附件名称
				attachPart.setDataHandler(new DataHandler(fds));
				attachPart.setFileName(email.getAttacheName());
				
				//添加正文的Mime 和 附件的mime元素到Multipart 中
				multiPart.addBodyPart(contentPart);
				multiPart.addBodyPart(attachPart);
				
				//添加整份邮件到message 中
				message.setContent(multiPart);
				message.saveChanges();
				
				//------------------发送-----------
				Transport transport = session.getTransport();
				transport.connect(host,Integer.parseInt(port), user, password);
				//发送地址不为空才发送
				if(message.getRecipients(Message.RecipientType.TO)!=null){
					transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
				}
				//抄送地址不为空才抄送
				if(message.getRecipients(Message.RecipientType.CC)!=null){
					transport.sendMessage(message, message.getRecipients(Message.RecipientType.CC));
				}
				//暗送地址不为空才暗送
				if(message.getRecipients(Message.RecipientType.BCC)!=null){
					transport.sendMessage(message, message.getRecipients(Message.RecipientType.BCC));

				}
				
				transport.close();
				
			//	message.writeTo(new FileOutputStream("/home/predisw/test.eml"));
				
				return true;
	}
	
	
	
	
	//缩放图片
	@Override
	public void zoomImg(String srcImgName, String destImgName, int width, int height) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		
		Image srcImg  = ImageIO.read(new FileInputStream(srcImgName) );//取源图
//	    int  width  =  150; //假设要缩放的像数
	// int  height =  srcImg.getHeight(null)*width/srcImg.getWidth(null);//按比例，将高度缩减

	    BufferedImage result= new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); //创建一个想要的图片的容器模板
	    Image smallImg =srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);//从原图获取一个和模板一样的图片实例
	    result.getGraphics().drawImage(smallImg, 0, 0, null); //将原图获取的实例写入 想要的模板 中
	    
	    FileOutputStream imgOut=new FileOutputStream(destImgName);
	    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(imgOut); //编码
	    encoder.encode(result); //将想要的图片模板写出到文件中
	}

}
