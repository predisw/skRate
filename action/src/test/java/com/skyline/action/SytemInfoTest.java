package com.skyline.action;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class SytemInfoTest {

	@Test
	public void getSystemInfo() throws IOException{
		//get cpu
		//get memory


		Process process=Runtime.getRuntime().exec("top -b -n 1");
		 java.util.Scanner in= new java.util.Scanner(process.getInputStream());
		while(in.hasNextLine()){
			System.out.println(in.nextLine());
		}
		
	}
}
