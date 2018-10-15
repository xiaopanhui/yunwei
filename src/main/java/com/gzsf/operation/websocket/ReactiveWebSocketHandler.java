package com.gzsf.operation.websocket;

import com.gzsf.operation.bean.LogMessage;
import com.gzsf.operation.service.LogFileService;
import org.reactivestreams.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.adapter.ReactorNettyWebSocketSession;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class ReactiveWebSocketHandler implements WebSocketHandler {
    private Map<Long,List> idMap=new HashMap<>();
    private Map<String,Subscriber> subscriberHashMap=new HashMap<>();
    private Map<String,Subscriber> old=new HashMap<>();
    private DefaultDataBufferFactory factory=new DefaultDataBufferFactory();
    @Autowired
    private LogFileService logFileService;
    @Override
    public List<String> getSubProtocols() {
        return Collections.emptyList();
    }
    private List<WebSocketSession> webSocketSessions =new ArrayList<>();
    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        ReactorNettyWebSocketSession socketSession = (ReactorNettyWebSocketSession) webSocketSession;
//        webSocketSessions.add(webSocketSession);
        return webSocketSession.receive()
                .map(msg -> {
                    String idStr = msg.getPayloadAsText();
                    if (msg.getType() == WebSocketMessage.Type.PONG) {
                        subscriberHashMap.put(idStr,old.get(idStr));
                        return 0L;
                    }else {
                        Long id = Long.valueOf(idStr);
                        logFileService.addLogFile(id);
                        return id;
                    }

                })
                .flatMap(it -> webSocketSession.send(pu -> {
                    List list = idMap.containsKey(it) ? idMap.get(it) : new LinkedList();
                    list.add(webSocketSession.getId());
                    idMap.put(it, list);
                    subscriberHashMap.put(webSocketSession.getId(), pu);
                        })

                ).onErrorResume(it -> webSocketSession.close())
                .doFinally(sig -> {
                    System.out.println("Terminating WebSocket Session (client side) sig: [{}], [{}]");
                }).reduce(((aVoid, aVoid2) -> aVoid));
    }
    @EventListener
    public void send (LogMessage message){
        if(!idMap.containsKey(message.getLogId()))return;
        List<String> list= idMap.get(message.getLogId());
        List<String> newList= idMap.get(message.getLogId());
        WebSocketMessage webSocketMessage=new WebSocketMessage(WebSocketMessage.Type.TEXT,factory.wrap(message.getContent()));
        for (String id :list){
            if (subscriberHashMap.containsKey(id)){
                subscriberHashMap.get(id).onNext(webSocketMessage);
                newList.add(id);
            }
        }
        idMap.put(message.getLogId(),newList);
    }

    @Scheduled(fixedDelay = 10000)
    private void check(){
        old = subscriberHashMap;
        for(HashMap.Entry<String,Subscriber> item: subscriberHashMap.entrySet()){
                WebSocketMessage webSocketMessage=new WebSocketMessage(WebSocketMessage.Type.PING,factory.wrap( item.getKey().getBytes()));
                if (item.getValue()==null)
                {
                    subscriberHashMap.remove(item.getKey());
                    continue;
                }
                item.getValue().onNext(webSocketMessage);
        }
        subscriberHashMap=new HashMap<>();
    }
}
