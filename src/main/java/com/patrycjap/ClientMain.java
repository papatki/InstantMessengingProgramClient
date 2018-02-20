package com.patrycjap;
import javax.swing.*;

public class ClientMain {
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1"); //local host - computer there I am right now
        client.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        client.startRunning();

    }
}
