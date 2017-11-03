package com.x.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

/**
 * Hello world!
 *
 */
@EnableZuulProxy
@EnableFeignClients("com.x.api.auth.client")
@SpringCloudApplication
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class})
public class ZuulServer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ZuulServer.class, args);
        System.out.println("ZuulServer Started!");
        System.out.println(Resources.toString(ZuulServer.class.getResource("/logo.txt"), Charsets.UTF_8));
    }

}
