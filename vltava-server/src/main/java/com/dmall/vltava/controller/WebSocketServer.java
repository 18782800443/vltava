package com.testhuamou.vltava.controller;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Rob
 * @date Create in 3:18 PM 2020/3/11
 */
@ServerEndpoint(value = "/vltava/ws/")
@Component
public class WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private static AtomicInteger onlineCount = new AtomicInteger(0);

    private static ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        logger.info("新连接介入，当前连接人数" + onlineCount.incrementAndGet());
    }

    @OnClose
    public void onClose(Session session) {
        for (String key : sessionMap.keySet()) {
            if (sessionMap.get(key) == session) {
                sessionMap.remove(key);
                break;
            }
        }
        logger.info("有连接关闭，当前总连接：" + onlineCount.decrementAndGet() + " 当前map总数：" + sessionMap.size());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("ws msg: " + message);
        sessionMap.put(message, session);
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        logger.error(throwable.getMessage(), throwable);
    }

    public synchronized void sendMessage(Integer attachType, Integer taskId, String message) {
        Session session = sessionMap.get(attachType + "@" + taskId);
        if (session == null){
            logger.warn("未收到前端请求");
            return;
        }
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void close(Integer attachType, Integer taskId) {
        Session session = sessionMap.get(attachType + "@" + taskId);
        onClose(session);
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
