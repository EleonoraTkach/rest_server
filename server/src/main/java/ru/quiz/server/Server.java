package ru.quiz.server;

import com.sun.net.httpserver.HttpServer;
import org.hibernate.sql.Select;
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
            httpServer.createContext("/user/selectTests", new SelectTestsHandler());
            httpServer.createContext("/user/selectQuestions", new SelectQuestionsAndAnswersHandler());
            httpServer.createContext("/user/selectUsers", new SelectUsersHandler());
            httpServer.createContext("/user/selectAppoint", new SelectAppointHandler());
            httpServer.createContext("/user/selectResultTest", new SelectResultTestHandler());
            httpServer.createContext("/user/updateResult", new UpdateResultTestHandler());
            httpServer.createContext("/user/selectTestsStudents", new SelectTestsStudentsHandler());
            httpServer.createContext("/user/selectResultTestYour", new SelectResultYourTestHandler());
            httpServer.createContext("/user/selectUsersConfirm", new SelectUsersConfirmHandler());
            httpServer.createContext("/user/updateConfirm", new UpdateConfirmHandler());
            httpServer.createContext("/user/deleteUser", new DeleteUserHandler());
            httpServer.createContext("/user/selectUsersAll", new SelectUsersAllHandler());
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
