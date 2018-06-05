package specific.Util;

import java.io.*;
import java.net.Socket;

public class SocketServerSendThread extends Thread {
    // 和本线程相关的Socket
    Socket socket = null;
    OutputStream os=null;

    public SocketServerSendThread(Socket socket) throws IOException {
        this.socket = socket;
        this.os = socket.getOutputStream();
    }

    //线程执行的操作，响应客户端的请求
    public void run(){
        Instructions ins = new Instructions();
        byte a []= MeterUtil.Hex2ToByte(ins.getReadInstructions());
        try {
            os.write(a);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
