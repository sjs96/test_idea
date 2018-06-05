package specific.Util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/*
 * 服务器线程处理类
 */
public class TokenSocketServerThread extends Thread {
    // 和本线程相关的Socket
    Socket socket = null;

    public TokenSocketServerThread(Socket socket) {
        this.socket = socket;
        TokenSocketServer.SocketMap.put(socket.getInetAddress().getHostAddress(),socket);
        try {
            TokenSocketServer.outputStream.put(socket.getInetAddress().getHostAddress(),socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //线程执行的操作，响应客户端的请求
    public void run(){

        try {
            new TokenSocketServerReceiveThread(socket).start();
            //send();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}