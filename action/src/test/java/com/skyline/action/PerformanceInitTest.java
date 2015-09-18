package com.skyline.action;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.skyline.schedule.PerformanceInit;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class PerformanceInitTest {
	@Test
	public void test(){
		String pathFileName="/var/log/skyline/performance.log";
		PerformanceInit performance = new PerformanceInit();
		performance.setPathFileName(pathFileName);
		new Thread(performance).run();
	}
}
