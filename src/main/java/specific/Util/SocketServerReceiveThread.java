package specific.Util;

import java.io.*;
import java.net.Socket;

public class SocketServerReceiveThread extends Thread {
    // 和本线程相关的Socket
    InputStream is = null;
    Socket socket = null;
    String meterNo = "";

    public SocketServerReceiveThread(Socket socket) throws IOException {
        this.socket = socket;
        this.is = socket.getInputStream();
    }

    //线程执行的操作，响应客户端的请求
    public void run(){
        while(true){
            meterNo = Meter.getCurrent(is);
            SocketServer.SocketMap.put(meterNo,socket);
            try {
                SocketServer.outputStream.put(meterNo,socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
