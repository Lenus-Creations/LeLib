package org.lenuscreations.lelib.sockets;

import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class LServerSocket {

    private ServerSocket serverSocket;

    private final String host;
    private final int port;

    private final List<SocketListener> listeners;

    private Thread thread;

    public LServerSocket(int port) {
        this("127.0.0.1", port);
    }

    public LServerSocket(String host, int port) {
        this.host = host;
        this.port = port;

        this.listeners = new ArrayList<>();
    }

    public void start() {
        if (this.thread != null) throw new RuntimeException("Server socket is already running.");
        try {
            this.serverSocket = new ServerSocket(port, 50, new InetSocketAddress(host, port).getAddress());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.listen();
    }

    private void listen() {
        this.thread = new Thread(() -> {
            while (true) {
                try {
                    Socket socket = this.serverSocket.accept();
                    if (socket == null) continue;

                    DataInputStream in = new DataInputStream(socket.getInputStream());

                    String line;
                    while ((line = in.readLine()) != null) {
                        for (SocketListener listener : listeners) {
                            String response = listener.onMessage(line);
                            if (response == null) continue;

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            PrintStream ps = new PrintStream(baos);
                            ps.println(response);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        this.thread.start();
    }

    @SneakyThrows
    public void stop() {
        this.serverSocket.close();

        this.thread.interrupt();
        this.thread.stop();
        this.thread = null;
    }

}
