/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package io.github.p0lbang.gofish;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;

public class oldApp {
    Client client;
    public String getGreeting() {
        client = new Client();
        client.start();
        return "Hello World!";
    }

    public static void main(String[] args) {
        // System.out.println(new App().getGreeting());
        // new ChatClient();
        // try {
        //     new ChatServer();
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }
}
