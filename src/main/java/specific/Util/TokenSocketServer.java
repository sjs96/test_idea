package specific.Util;

import com.jfinal.kit.PropKit;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/*
 * 基于TCP协议的Socket通信，实现用户登陆
 * 服务器端
 */
    public class TokenSocketServer implements Runnable{
        public static Map<String,Socket> SocketMap= new HashMap<>();
        public static Map<String,OutputStream> outputStream= new HashMap<>();
        @Override
        public void run() {
            try {

                String port = PropKit.get("socketPort1");
                //1.创建一个服务器端Socket，即ServerSocket，指定绑定的端口，并监听此端口v
                ServerSocket serverSocket=new ServerSocket(Integer.parseInt(port) );
                Socket socket=null;
                //记录客户端的数量
                int count=0;
                System.out.println("***服务器即将启动，等待客户端的连接***");
                //循环监听等待客户端的连接
                while(true){
                    //调用accept()方法开始监听，等待客户端的连接
                    socket=serverSocket.accept();

                    //创建一个新的线程
                    TokenSocketServerThread serverThread=new TokenSocketServerThread(socket);
                    //启动线程
                    serverThread.start();
                    count++;//统计客户端的数量
                    System.out.println("客户端的数量："+count);
                    System.out.println("当前客户端的IP："+socket.getInetAddress().getHostAddress());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void socket() {
            TokenSocketServer serverApp = new TokenSocketServer();
            Thread thread = new Thread(serverApp);
            thread.start();
        }
        public static OutputStream getOutputStream() {
            for (Map.Entry<String,OutputStream> entry : outputStream.entrySet()) {
                if(entry.getValue()!=null){
                    return entry.getValue();
                }
            }
            System.out.println("没有加密服务器");
            return null;
        }
    }

