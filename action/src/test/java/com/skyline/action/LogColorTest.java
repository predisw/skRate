package com.skyline.action;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogColorTest {
		@Test
		public void TestColorInLog() throws IOException{
			Logger logger = LoggerFactory.getLogger(this.getClass());
/*			OutputStream out= new FileOutputStream("/var/log/skyline/skylineRate.log");
			System.setOut(new PrintStream(new BufferedOutputStream(out)));
			System.out.println((char)27 + "[34;43mBlue text with yellow background");
			System.out.flush();*/
//			logger.debug((char)27 + "[34;43mBlue text with yellow background");
		}
}
