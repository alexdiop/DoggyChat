 package com.example.doggychat;

public abstract class ChatMediator {

    protected String user;
    protected Mediator mediator;

    public ChatMediator(Mediator mediator, String user){
        this.mediator = mediator;
        this.user = user;
    }

    public abstract void sendMessage(String message, String recipient);

    public abstract void receiveMessage(String sender);



}
