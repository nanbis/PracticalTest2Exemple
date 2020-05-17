package ro.pub.cs.systems.eim.practicaltestv2;

import android.widget.EditText;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
    EditText server_port;
    ServerSocket server_socket;

    boolean isRunning = false;

    public ServerThread(EditText server_port) {
        this.server_port = server_port;
    }

    public void startServer() {
        isRunning = true;
        start();
    }

    public void stopServer() throws IOException {
        if (server_socket != null)
            server_socket.close();
    }

    @Override
    public void run() {
        try {
            server_socket = new ServerSocket(Integer.valueOf(server_port.getText().toString()));
            while (isRunning) {
                Socket socket = server_socket.accept();

                if (socket != null) {
                    CommunicationThread comm = new CommunicationThread(socket);
                    comm.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
