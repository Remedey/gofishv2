package io.github.p0lbang.gofish.network;

public interface ChatInterface {
    void sendMessage(String message);

    void joinServer(String name);

    void GUI_startGame();
}
