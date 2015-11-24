package com.skyline.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;



//交给spring 统一管理，实现解耦
@Repository
public class PoiExcel {
	/**
	 * @author Predisw
	 * @version 1.0
	 * @date 2015-3-9
	 * @param fileName    Excel文件名路径
	 * @param sheetNumber  sheet 的顺序，从0开始
	 * @param tbIndex 数据库表中属性的顺序
	 * @return  list
	 */
	
	/*
	 * 将表格中的数据按照tbIndex 的这个顺序放到list 中去,就是将excel数据读取出来,返回list
	 * 如果某个属性的值不存在，将用空字符串"" 代替。
	 * 有一个bug ，如果用libreoffice 去掉格式，有时会发生bug 错误
	 */
	
	//还要把excel 中国家为空的处理一下

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public List<String[]> readByPoi(String fileName,int sheetNumber,String[] tbIndex ) throws FileNotFoundException, IOException {
		Workbook workbook = null;  
        List<String[]> list = new ArrayList<String[]>();  

            String fileType=fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());  
                if (fileType.equals("xls")) {    
                    workbook = new HSSFWorkbook();  
                }    
                else if(fileType.equals("xlsx"))    
                {    
                    workbook = new XSSFWorkbook(new FileInputStream(fileName));    
                }    
                else    
                {    
                	throw new IllegalArgumentException("您的文档格式不正确 !,请上传excel");

                }    
                
            //创建sheet对象    
            Sheet sheet = workbook.getSheetAt(sheetNumber);  
             int firstRowIndex = sheet.getFirstRowNum();     //就是第一行，不会忽略空行
             int lastRowIndex = sheet.getLastRowNum(); 
             int firstRowOfContext=-1;  //表头属性行 的行 number，如果不存在就默认为-1
                
             Integer[] indexh =new Integer[tbIndex.length];  //用来保存excel表头属性的顺序
  
                //表中查找各自属性在哪里，找到后将按照属性在数据库的顺序在indexh 中记录下来
             for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex ++){    
                 Row rowh=sheet.getRow(rIndex);
                 
                 if(rowh != null){
                	
                	 
	                 int firstCellIndexh = rowh.getFirstCellNum();    //获取表头开始的单元格的序号
	                 //   int lastCellIndexh = rowh.getPhysicalNumberOfCells();
	                 int  lastCellIndexh=sheet.getRow(rIndex).getLastCellNum();         //获得第一行总列数,不会跳过空格列，空列也会统计到列数里
	//                 System.out.println("the"+rIndex+" row's lastCellIndexh is " + lastCellIndexh);
	            	
	                 for(int cIndexh = firstCellIndexh; cIndexh < lastCellIndexh; cIndexh ++){  
	
		                	Cell cellh=rowh.getCell(cIndexh);
		                	if(cellh!=null){
		                		   
		                		for(int i=0;i<tbIndex.length;i++){
		                			if (cellh.toString().trim().equalsIgnoreCase(tbIndex[i])){  //去掉标题的左右的空格
		                				indexh[i]=cIndexh;   //按照传进来的数组中的属性调整excel 中属性的顺序
		                				 firstRowOfContext=rIndex;  //设置标题所在的行的index 给firstRowOfContext
		                				}
				                }
		                	} 
		                }
                 }
             }   
             
    //下面是将内容放到list 中         
                Cell cell;
 //               System.out.println("the attribute row's index is "+firstRowOfContext+1);
            if(firstRowOfContext !=-1){//-1 为默认值，就是指没有找到标题行
                for(int rIndex = firstRowOfContext+1; rIndex <= lastRowIndex; rIndex ++){    
                    Row row = sheet.getRow(rIndex);
                    
                    String[]  s=new String[tbIndex.length];		//用来保存excel 一行的数据，必须放到行的循环内
                    																						//，这样s 就会指向不同的行，因为新new 一个数组来保存新的行
                    if(row != null){    
                    //分别取出没列中对应的值按照顺序放进s 字符串
                        for(int j=0;j<tbIndex.length;j++){
                    //    	System.out.println(indexh[j]);
                        	String value = "";//加入单元格的值为空，就用“” 空字符串来代替 
                        	if(indexh[j]!=null){
	                        	cell=row.getCell( indexh[j] );
	                        	 if(cell != null){
	                            	 int type = cell.getCellType();  
	                            	
	                            	 value = cell.toString();
	//                            	 if(value.equals("")){value=null;}
	                                if (Cell.CELL_TYPE_NUMERIC == type) {   
	                                	//value = String.valueOf(cell.getNumericCellValue());   
	                                      Double val = cell.getNumericCellValue();  
	                                      if(val == val.longValue()){ //比较地址，判断为同一个东西，如果不是这个value用空字符串写入S 
	                                          value= "" + val.longValue();  //将long转换为字符串类型
	                                      }   
	                                }  //转换为字符串类型    
	                            }   

                        	}
                            s[j]=value;    //将cell 的值放到字符串数组中
//                          System.out.print(s[j] + " ");
                        }
                        //将所有元素为空行 不加入list中
                        for(int i=0;i<s.length;i++){
                        	if(!"".equals(s[i])){
                                list.add(s);
                                break;
                        	}
                        }

//						System.out.println("第"+(rIndex-1)+"row\n");
				
                    }
              
                }
/*                System.out.println("*****************");
                for(int i=0;i<list.size();i++){
                	for(int j=0;j<list.get(i).length;j++){
                	System.out.print((list.get(i))[j]+" ");
                	
                	}System.out.print(i+"\n");
                }
                System.out.println("the list's size is "+list.size());    */
                
            }else{
            	System.out.println("Excel文件中没有找到符合的标题属性");
            }

        return list;  
    }  

	/**
	 * 将 strArr的字符串元素按照顺序一一读出,设置到对象 obj 属性名为字符串数组 tbIndex 元素的域中.并返回obj.
	 * @author predisw
	 * @version 1.0
	 * @since 2015-7-10
	 * 
	 * @param strArr 要保存到对象中的字符串数组
	 * 
	 * @param tbIndex
	 * 	这个是obj对象的属性名数组,	tbIndex 数组和strArr 的长度和顺序要一致
	 * 
	 * @param str_date  将字符串转换为java.util.Date 类型.
	 * 
	 * @param obj strArr 的元素值 会被设置到 obj 对象中
	 * 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws NumberFormatException 
	 * @throws ParseException 
	 */
	public Object setStringArrayToObj(String[] strArr,String[] tbIndex,Object obj,SimpleDateFormat strToDate ) throws NoSuchFieldException, SecurityException, NoSuchMethodException, NumberFormatException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ParseException{
		Class cla = obj.getClass();
		
		for(int i=0;i<strArr.length;i++){
			Field field=cla.getDeclaredField(tbIndex[i]);
			Class paramType=field.getType();
			String methodStr="set"+ tbIndex[i].substring(0, 1).toUpperCase()+ tbIndex[i].substring(1);
			Method method=cla.getMethod(methodStr,paramType);
	//		logger.debug("the field type is [{}] ,the method is [{}]",paramType.getSimpleName(),method);
//			logger.debug("xxxxxxxxxxxxxxxx[{}]",paramType);
			if(paramType.getSimpleName().equals("Integer")){
				method.invoke(obj, Integer.parseInt(strArr[i]));
				continue;
			}
			
			if(paramType.getSimpleName().equals("Double")){
				method.invoke(obj, Double.parseDouble(strArr[i]));
				continue;
			}
			
			if(paramType.getName().equals("java.util.Date")){
				method.invoke(obj, strToDate.parse(strArr[i]));
				continue;
			}

			if(paramType.getName().equals("java.lang.Boolean")){
				method.invoke(obj, Boolean.parseBoolean(strArr[i]));
				continue;
			}
			
			if(paramType.isEnum() ){
				method.invoke(obj,Enum.valueOf(paramType,strArr[i]) );
				continue;
			}
		
			//其他类型直接设置
			method.invoke(obj,strArr[i]);


		}
		
		return obj;
	}
	

	/**
	 * used to insert the image to excel
	 * @throws IOException 
	 * 
	 * 
	 * 
	 */
	public void  addImage(Sheet sheet,String imgName) throws IOException{
		
		FileInputStream fin=new FileInputStream(imgName);
		//将图片转为字节流流
		byte[] byteArray = IOUtils.toByteArray(fin);
		
		//设置图片的类型
		int imgType=0;
		if( imgName.endsWith(".png") ) {	
            imgType = Workbook.PICTURE_TYPE_PNG;
		}
		else if( imgName.endsWith("jpg") || imgName.endsWith(".jpeg") ) {
            imgType = Workbook.PICTURE_TYPE_JPEG;
		}
		else  {
            throw new IllegalArgumentException("Invalid Image file : " +
                imgName);
		}
		
		//设置图片的位置
		ClientAnchor anchor =sheet.getWorkbook().getCreationHelper().createClientAnchor();
		anchor.setDx1(0);
		anchor.setDy1(0);
		anchor.setDx2(0);
		anchor.setDy2(0);
		anchor.setCol1(0);
		anchor.setRow1(0);
		anchor.setCol2(1);
		anchor.setRow2(1);
		//anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);
		//把索引号为picIndex 的图片插入anchor 的位置中
		Drawing drawing = sheet.createDrawingPatriarch();
		int picIndex = sheet.getWorkbook().addPicture(byteArray, imgType);
		Picture pic=drawing.createPicture(anchor, picIndex);
		pic.resize();
	}
	
	
	/**
	 * 将list 中的内容追加写入已存在的路径为fileName 的 excel文件  中.
	 * @param list  写入的内容
	 * @param fileName 写入文件的绝对路径
	 * @param db_header 数据库中的字段
	 * @param excel_header excel 表头 的顺序
	 * @param sheetIndex 写入到sheet中第几个sheet
	 * @param newSheetName 假如不为null,则改写为 newSheetName
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * 
	 */
	public void toExisting(List list ,String fileName,String[] db_header,String[] excel_header,int sheetIndex,String newSheetName,SimpleDateFormat str_date) throws FileNotFoundException, IOException{
	
		Workbook workbook = null;  
            String fileType=fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());  
                if (fileType.equals("xls")) {    
                    workbook = new HSSFWorkbook(new FileInputStream(fileName));  
                }    
                else if(fileType.equals("xlsx"))    
                {    
                    workbook = new XSSFWorkbook(new FileInputStream(fileName));    
                }    
                else    
                {    
                    System.out.println("您的文档格式不正确！");    
                }    
			
		if(list!=null && list.size()>0 && db_header!=null){
			Sheet sheet = workbook.getSheetAt(sheetIndex);
			if(newSheetName!=null){
				workbook.setSheetName(sheetIndex, newSheetName);
				}
			
			int firstRow = sheet.getLastRowNum()+2;
			
			//--------------
			//创建标题行
/*			sheet.setDefaultColumnWidth((short) 15); //列的宽度
			sheet.setDefaultRowHeight((short)380);
			CellStyle style = workbook.createCellStyle(); //标题行的单元格格式 
			Font font = workbook.createFont();
//		      font.setColor(XSSFColor.VIOLET.index);
		    font.setFontHeightInPoints((short) 12);
		    font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		      // 把字体应用到当前的样式
		    CellStyle style = workbook.createCellStyle(); //标题行的单元格格式 
		    style.setFont(font);
		    */
		    CellStyle style = workbook.createCellStyle(); //标题行的单元格格式 
			Font font = workbook.createFont();
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		    style.setFont(font);
			Row row = sheet.createRow((short)firstRow); //创建第一行作为标题

			for(int i=0;i<excel_header.length;i++){  //给excel 的第一行设置表头的标题
				Cell cell = row.createCell(i);
				cell.setCellStyle(style);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(excel_header[i]);
			}
			
			
			//---------------------------写入list 的内容到excel----------------
			CellStyle cellStyle = workbook.createCellStyle(); //excel 内容的单元格格式 
			cellStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);  //主要是为了解决double 类型的单元格数值没有左对齐,都右对齐了
			//对应于headers 的方法数组集合
			Class cls=list.get(0).getClass();
			String[] methods = new String[db_header.length];
			for(int i=0;i<db_header.length;i++){
				methods[i]="get"+ db_header[i].substring(0, 1).toUpperCase()+ db_header[i].substring(1);
			}
			//将list 的元素对象的域值一一按照db_headers的顺序写入excel 表中
			 for(int i=0;i<list.size();i++){
				 row = sheet.createRow(i+firstRow+1); // 跳过第标题行
				 for(int j=0;j<db_header.length;j++){
					Cell cell = row.createCell(j);
					 cell.setCellStyle(cellStyle);
					 Method method=null;
					try {
						method = cls.getMethod(methods[j]);
					} catch (NoSuchMethodException | SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 try {
						if(method.invoke(list.get(i)) instanceof Double){
							 Double d_value=(Double)(method.invoke(list.get(i)));
							 cell.setCellValue(d_value);
							 cell.setCellType(Cell.CELL_TYPE_NUMERIC); //java double 类型的数值输出形式(toString)默认为科学计数法 的表示形式,所以保存为小数形式到excel中
							 
						 }else if(method.invoke(list.get(i)) instanceof Date){ //如果是时间类型,则格式化
							 Date d_value=(Date)(method.invoke(list.get(i)));
							 cell.setCellValue(str_date.format(d_value));
							 
						 }else if(method.invoke(list.get(i))==null){ //假如是null 对象,则写入空字符串到excel 中
							 cell.setCellValue(""); 
						 }else{

							String value=String.valueOf(method.invoke(list.get(i))); 
							cell.setCellValue(value); //list 元素对象的方法
						 
						 }
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
			 }
		}
	//写出内容到文件	
		FileOutputStream fOut = new FileOutputStream(fileName);
		workbook.write(fOut);
		fOut.flush();
		fOut.close();
	}
	
	/**
	 *到处list 中的内容到文件名fileName 的文件内 
	 * @param list 
	 * @param fileName 
	 * @param db_header 
	 * @param excel_header
	 * @param str_date
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	
	public void  export(List list ,String fileName,String[] db_header,String[] excel_header,SimpleDateFormat str_date) throws IOException {
		//判断文件名
		Workbook workbook =null;
        String fileType=fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());  
        if (fileType.equals("xls")) {
                workbook = new HSSFWorkbook();  
            }    
        else if(fileType.equals("xlsx"))
            {    
                workbook = new XSSFWorkbook();    
            }    
        else    
            {    
                throw new IllegalArgumentException(fileType+" is not corrected");
            }    
     
        //创建标题行
        Sheet sheet = workbook.createSheet();

		int firstRow = sheet.getLastRowNum()+1;
	
	    CellStyle style = workbook.createCellStyle(); //标题行的单元格格式 
		Font font = workbook.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
	    style.setFont(font);
		Row row = sheet.createRow((short)firstRow); //创建第一行作为标题

		for(int i=0;i<excel_header.length;i++){  //给excel 的第一行设置表头的标题
			Cell cell = row.createCell(i);
			cell.setCellStyle(style);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(excel_header[i]);
		}
		
		
		//---------------------------写入list 的内容到excel----------------
		CellStyle cellStyle = workbook.createCellStyle(); //excel 内容的单元格格式 
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);  //主要是为了解决double 类型的单元格数值没有左对齐,都右对齐了
		//对应于headers 的方法数组集合
		Class cls=list.get(0).getClass();
		String[] methods = new String[db_header.length];
		for(int i=0;i<db_header.length;i++){
			methods[i]="get"+ db_header[i].substring(0, 1).toUpperCase()+ db_header[i].substring(1);
		}
		//将list 的元素对象的域值一一按照db_headers的顺序写入excel 表中
		 for(int i=0;i<list.size();i++){
			 row = sheet.createRow(i+firstRow+1); // 跳过第标题行
			 for(int j=0;j<db_header.length;j++){
				Cell cell = row.createCell(j);
				 cell.setCellStyle(cellStyle);
				 Method method=null;
				try {
					method = cls.getMethod(methods[j]);
				} catch (NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 try {
					if(method.invoke(list.get(i)) instanceof Double){
						 Double d_value=(Double)(method.invoke(list.get(i)));
						 cell.setCellValue(d_value);
						 cell.setCellType(Cell.CELL_TYPE_NUMERIC); //java double 类型的数值输出形式(toString)默认为科学计数法 的表示形式,所以保存为小数形式到excel中
						 
					 }else if(method.invoke(list.get(i)) instanceof Date){ //如果是时间类型,则格式化
						 Date d_value=(Date)(method.invoke(list.get(i)));
						 cell.setCellValue(str_date.format(d_value));
						 
					 }else if(method.invoke(list.get(i))==null){ //假如是null 对象,则写入空字符串到excel 中
						 cell.setCellValue(""); 
					 }else{

						String value=String.valueOf(method.invoke(list.get(i))); 
						cell.setCellValue(value); //list 元素对象的方法
					 
					 }
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		 }


    	//写出内容到文件	
    	FileOutputStream fOut = new FileOutputStream(fileName);
    	workbook.write(fOut);
    	fOut.flush();
    	fOut.close();
	}
	
}
