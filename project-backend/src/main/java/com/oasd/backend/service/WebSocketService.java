package com.oasd.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.oasd.backend.domain.SocketMsg;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket/{nickname}")
@Component
public class WebSocketService {
    //bind sessionId and session
    private static Map<String,Session> map = new HashMap<>();
    //store WebSocketService
    private static CopyOnWriteArraySet<WebSocketService> webSocketSet = new CopyOnWriteArraySet<>();
    private static Map<String, String> usersMap = new HashMap<>();
    private Session session;

    /**
      * on connect
      */
    @OnOpen
    public void onOpen(Session session,@PathParam("nickname") String nickname) {
        Map<String,Object> message= new HashMap<>();
        this.session = session;
        map.put(session.getId(), session);
        usersMap.put(nickname, session.getId());
        webSocketSet.add(this);

        System.out.println("New connection: " + nickname + ", id: " + session.getId() + ",current people:" + webSocketSet.size());
        message.put("type",0); // 0-connect success，1-message
        message.put("people",webSocketSet.size());
        message.put("name",nickname);
        message.put("aisle",session.getId());
        this.session.getAsyncRemote().sendText(new Gson().toJson(message));
    }

    /**
     * close
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        map.remove(this.session.getId());
        for (Map.Entry<String, String> entry : usersMap.entrySet()){
            if (entry.getValue().equals(this.session.getId())){
                usersMap.remove(entry.getKey());
                break;
            }
        }
        System.out.println("Close 1, current people: " + webSocketSet.size());
    }

    /**
     * @param message message from client
     */
    @OnMessage
    public void onMessage(String message, Session session,@PathParam("nickname") String nickname) {
        ObjectMapper objectMapper = new ObjectMapper();
        SocketMsg socketMsg;

        try {
            socketMsg = objectMapper.readValue(message, SocketMsg.class);
            if(socketMsg.getType() == 1){
                socketMsg.setFromUser(session.getId());
                Session fromSession = map.get(socketMsg.getFromUser());
                String username = socketMsg.getToUser();
                String id = usersMap.get(username);
                Session toSession = map.get(id);

                if(toSession != null){
                    Map<String,Object> m= new HashMap<>();
                    m.put("type",1);
                    m.put("to", username);
                    m.put("name",nickname);
                    m.put("msg",socketMsg.getMsg());
                    fromSession.getAsyncRemote().sendText(new Gson().toJson(m));
                    toSession.getAsyncRemote().sendText(new Gson().toJson(m));
                }else{
                    Map<String, String> map = new HashMap<>();
                    map.put("false", "false");
                    fromSession.getAsyncRemote().sendText(new Gson().toJson(map));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}