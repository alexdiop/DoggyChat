package com.example.doggychat;

public abstract class Communication {

    private String userid;
    private String[] feed;

    public abstract void findUsers();
    public abstract boolean editMessage();
    public abstract boolean sendFriendRequest();
    public abstract boolean createGroupChat();
    public abstract boolean addUserToChat();
    public abstract boolean sendMessage();
    public abstract boolean postToFeed();
    public abstract double viewActivityPoints();







}
