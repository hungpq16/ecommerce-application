package com.example.demo;

import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.controllers.CartControllerTest;
import com.example.demo.controllers.ItemControllerTest;
import com.example.demo.controllers.OrderControllerTest;
import com.example.demo.controllers.UserControllerTest;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SareetaApplicationTests {

	@Test
	public void contextLoads() {
	       JUnitCore junit = new JUnitCore();
	        junit.addListener(new TextListener(System.out));

	        Result result = junit.run(
	                UserControllerTest.class,
	                ItemControllerTest.class,
	                CartControllerTest.class,
	                OrderControllerTest.class);

	        System.out.println("Finished. Result: Failures: " +
	                result.getFailureCount() + ". Ignored: " +
	                result.getIgnoreCount() + ". Tests run: " +
	                result.getRunCount() + ". Time: " +
	                result.getRunTime() + "ms.");
	}

}
