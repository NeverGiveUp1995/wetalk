package com.ty.wetalk.config;

import com.ty.wetalk.controller.WebSocketServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.Resource;


@Configuration
@EnableScheduling
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Resource
    private WebSocketServer webSocketServer;

    //3.添加定时任务
    //@Scheduled(cron = "0/5 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    @Scheduled(fixedRate = 50000)
    private void configureTasks() throws Exception {
        System.out.println("================================定时器发送================================");
        webSocketServer.sendMessageAll("");
    }
}
