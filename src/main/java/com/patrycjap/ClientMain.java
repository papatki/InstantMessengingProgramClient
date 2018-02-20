package com.patrycjap;
import javax.swing.*;

public class ClientMain {
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1");
        client.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        client.startRunning();

    }
}
