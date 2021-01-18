package com.example.springcloud.controller;

import com.example.springcloud.model.Friend;
import com.example.springcloud.service.MyService;
import com.example.springcloud.service.impl.RequestCacheService;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import lombok.Cleanup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eddie.lee
 * @ProjectName springcloud-demo-dec
 * @Package com.example.springcloud.controller
 * @ClassName DemoController
 * @blog blog.eddilee.cn
 * @description
 * @date created in 2021-01-18 10:39
 * @modified by
 */
@RestController
public class DemoController {

	@Autowired
	private MyService myService;

	@Autowired
	private RequestCacheService requestCacheService;

	@GetMapping("/fallback")
	public String fallback() {
		return myService.error();
	}

	@GetMapping("/timeout")
	public String timeout(Integer timeout) {
		return myService.retry(timeout);
	}

	/**
	 * lombok.@Cleanup 注解： <br>
	 * 		<p> 可以帮你自动插入一些代码, 比如一些关闭操作 </p>
	 * 		<p> 如果不是调用close, 可以使用 @Cleanup("shutdown") </p>
	 *
	 * @param name
	 *            user
	 * @return friend
	 */
	@GetMapping("/cache")
	public Friend cache(String name) {
		@Cleanup
		HystrixRequestContext context = HystrixRequestContext.initializeContext();

		// try {
		Friend friend = requestCacheService.requestCache(name);
		name += "!";
		friend = requestCacheService.requestCache(name);
		return friend;
		// } finally {
		// context.close();
	}
}
