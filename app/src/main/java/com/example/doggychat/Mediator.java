package com.example.doggychat;

import java.util.HashMap;

public interface Mediator {


    public void sendMessage(String user, String message, String recipient);

    public void receiveMessage();

    public void updateHistory(String message);




}
