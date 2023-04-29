import java.io.*;
import java.net.Socket;

/*  Author:Crash
    Date:2023/4/29
*/
public class ServerThead extends Thread {
    private Socket socket;

    public ServerThead(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        //3.获取输入流并获取客户信息
        ObjectOutputStream oos = null;
        System.out.println("线程启动");
        try{
          oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
          oos.writeObject(Server.getHashMap());//发送hashmap
          oos.flush();
          oos.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                oos.close();
            } catch (Exception e) {}
            try {
                socket.close();
            } catch (Exception e) {}}

    }


}

