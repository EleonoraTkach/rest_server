package ru.quiz.server;

import com.sun.net.httpserver.HttpServer;
import ru.quiz.server.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private final int PORT = 8000;
    private final int THREADS = 0;
    private HttpServer httpServer;

    public Server() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), THREADS);
            httpServer.createContext("/user/register", new UserRegisterHandler());
            httpServer.createContext("/user/auth", new UserAuthHandler());
            httpServer.createContext("/user/updatePassword", new UserUpdatePasswordHandler());
            httpServer.createContext("/user/updatePasswordAndEmail", new UserUpdatePasswordAndEmailHandler());
            httpServer.createContext("/user/createTest", new CreateTestHandler());
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
