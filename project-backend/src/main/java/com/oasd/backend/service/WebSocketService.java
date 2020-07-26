package com.oasd.backend.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.oasd.backend.domain.SocketMsg;
import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2RTFDTM;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
  * websocket的具体实现类
  * 使用springboot的唯一区别是要@Component声明下，而使用独立容器是由容器自己管理websocket的，
  * 但在springboot中连容器都是spring管理的。
     虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，
     所以可以用一个静态set保存起来。
  */
@ServerEndpoint(value = "/websocket/{nickname}")
@Component
public class WebSocketService {
    //用来存放每个客户端对应的MyWebSocket对象
    //用来记录sessionId和该session进行绑定
    private static Map<String,Session> map = new HashMap<>();
    private static CopyOnWriteArraySet<WebSocketService> webSocketSet = new CopyOnWriteArraySet<>();
    private static Map<String, String> usersMap = new HashMap<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String nickname;

    /**
      * 连接建立成功调用的方法
      */
    @OnOpen
    public void onOpen(Session session,@PathParam("nickname") String nickname) {
        Map<String,Object> message= new HashMap<>();
        this.session = session;
        this.nickname = nickname;
        map.put(session.getId(), session);
        usersMap.put(nickname, session.getId());
        webSocketSet.add(this);

        System.out.println("有新连接加入:" + nickname + ",当前在线人数为" + webSocketSet.size());
        message.put("type",0); //消息类型，0-连接成功，1-用户消息
        message.put("people",webSocketSet.size()); //在线人数
        message.put("name",nickname); //昵称
        message.put("aisle",session.getId()); //频道号
        this.session.getAsyncRemote().sendText(new Gson().toJson(message));
    }
    /**
      * 连接关闭调用的方法
      */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        map.remove(this.session.getId());
        for (Map.Entry<String, String> entry : usersMap.entrySet()){
            if (entry.getValue().equals(this.session.getId())){
                usersMap.remove(entry.getKey());
                break;
            }
        }
        System.out.println("有一连接关闭！当前在线人数为" + webSocketSet.size());
    }
    /**
      * 收到客户端消息后调用的方法
      *
      * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session,@PathParam("nickname") String nickname) {
        System.out.println("来自客户端的消息-->"+nickname+": " + message);

        // 从客户端传过来的数据是json数据，所以这里使用jackson进行转换为SocketMsg对象，
        // 然后通过socketMsg的type进行判断是单聊还是群聊，进行相应的处理:
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
                    m.put("name",nickname);
                    m.put("msg",socketMsg.getMsg());
                    fromSession.getAsyncRemote().sendText(new Gson().toJson(m));
                    toSession.getAsyncRemote().sendText(new Gson().toJson(m));

                }else{
                    Map<String, String> map = new HashMap<>();
                    map.put("false", "false");
                    map.put("msg", "系统消息：对方不在线或者您输入的频道号不对");
                    fromSession.getAsyncRemote().sendText(new Gson().toJson(map));
                }
            }else{
                broadcast(nickname+": "+socketMsg.getMsg());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
      * 发生错误时调用
      *
      */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }
    /**
      * 群发自定义消息
      * */
    public void broadcast(String message){
        for (WebSocketService item : webSocketSet) {
            //同步异步说明参考：http://blog.csdn.net/who_is_xiaoming/article/details/53287691
            //this.session.getBasicRemote().sendText(message);
            item.session.getAsyncRemote().sendText(message);//异步发送消息
        }
    }
}