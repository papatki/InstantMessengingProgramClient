package com.patrycjap;

import javax.swing.*;
import java.awt.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends JFrame {
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private String message = "";
    private String serverIP;
    private Socket connection;

    public Client(String host) {
        super("Talk2Me - Client");
        serverIP = host;
        chatWindow = new JTextArea();
        chatWindow.setBackground(Color.lightGray);
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        userText = new JTextField();
        userText.setBackground(Color.white);
        userText.setEditable(false);
        userText.addActionListener(e -> {
            sendMessage(e.getActionCommand());
            userText.setText("");
        });
        add(userText, BorderLayout.SOUTH);

        setSize(300, 300);
        setVisible(true);
    }

    public void startRunning() {
        try {
            connectToServer();
            setupStreams();
            whileChatting();

        } catch (EOFException e) {
            showMessage("\nClient terminated connection.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeChat();
        }
    }

    private void closeChat() {
        showMessage("\nClosing down...");
        ableToType(false);
        try {
            outputStream.close();
            inputStream.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void whileChatting() throws IOException {
        ableToType(true);
        do {
            try {
                message = (String) inputStream.readObject();
                showMessage("\n" + message);
            } catch (ClassNotFoundException e) {
                showMessage("\nError!");
            }
        } while (!message.equals("SERVER:  END"));
    }

    private void ableToType(final boolean trueOrFalse) {
        SwingUtilities.invokeLater(() -> userText.setEditable(trueOrFalse));
    }

    private void setupStreams() throws IOException {
        outputStream = new ObjectOutputStream(connection.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(connection.getInputStream());
    }

    private void connectToServer() throws IOException {
        showMessage("Attempting connection...\n");
        connection = new Socket(InetAddress.getByName(serverIP), 6789);
        showMessage("Connected to: " + connection.getInetAddress().getHostName()+".");
    }

    private void showMessage(final String message) {
        SwingUtilities.invokeLater(() -> chatWindow.append(message));
    }

    private void sendMessage(String message) {
        try {
            outputStream.writeObject("CLIENT:  " + message);
            outputStream.flush();
            showMessage("\nCLIENT:  " + message);
        } catch (IOException e) {
            chatWindow.append("\nSomething messed up sending message!");
        }
    }
}
