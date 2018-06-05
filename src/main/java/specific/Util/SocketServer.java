package specific.Util;

import com.jfinal.kit.PropKit;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/*
 * 基于TCP协议的Socket通信，实现用户登陆
 * 服务器端
 */
    public class SocketServer implements Runnable{
        public static Map<String,Socket> SocketMap= new HashMap<>();
        public static Map<String,OutputStream> outputStream= new HashMap<>();
        @Override
        public void run() {
            try {
                String port = PropKit.get("socketPort2");
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
                    SocketServerThread serverThread=new SocketServerThread(socket);
                    //启动线程
                    serverThread.start();
                    count++;//统计客户端的数量
                    System.out.println("客户端的数量："+count);
                    InetAddress address=socket.getInetAddress();
                    System.out.println("当前客户端的IP："+address.getHostAddress());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void socket() {
            SocketServer serverApp = new SocketServer();
            Thread thread = new Thread(serverApp);
            thread.start();
        }
        public static OutputStream getOutputStream(String m_no) {
            if(outputStream.containsKey("0"+m_no)){
                if(outputStream.get("0"+m_no)!=null){
                    return outputStream.get("0"+m_no);
                }
                System.out.println("没有电表:"+m_no);
                return null;
            }
            System.out.println("没有电表:"+m_no);
            return null;
        }
    }

