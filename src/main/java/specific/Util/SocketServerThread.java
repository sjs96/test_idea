package specific.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.InputStreamReader;
    import java.io.OutputStream;
    import java.io.PrintWriter;
    import java.net.Socket;

/*
 * 服务器线程处理类
 */
public class SocketServerThread extends Thread {
    // 和本线程相关的Socket
    Socket socket = null;

    public SocketServerThread(Socket socket) {
        this.socket = socket;
    }

    //线程执行的操作，响应客户端的请求
    public void run(){

        try {
            new SocketServerReceiveThread(socket).start();
            //send();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}