package org.lenuscreations.lelib.sockets;

import org.jetbrains.annotations.Nullable;

import java.net.Socket;

public class LSocket {

    private final String host;
    private final int port;

    @Nullable
    private Socket socket;

    public LSocket(int port) {
        this("127.0.0.1", port);
    }

    public LSocket(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
        try {
            this.socket = new Socket(host, port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        try {
            this.socket.close();
            this.socket = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isConnected() {
        return this.socket != null;
    }

    public boolean isClosed() {
        return !this.isConnected() || this.socket.isClosed();
    }

    public void send(String message) {
        if (this.isClosed()) throw new RuntimeException("Socket is closed.");
        if (!this.isConnected()) throw new RuntimeException("Socket is not connected.");

        try {
            this.socket.getOutputStream().write(message.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String read() {
        if (this.isClosed()) throw new RuntimeException("Socket is closed.");
        if (!this.isConnected()) throw new RuntimeException("Socket is not connected.");

        try {
            byte[] bytes = new byte[1024];
            this.socket.getInputStream().read(bytes);
            return new String(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
