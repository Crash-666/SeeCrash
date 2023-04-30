import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*  Author:Crash
    Date:2023/4/30
    Function：查看当前正在运行的功能，查看服务器有的功能，查看程序日志
*/public class query {
    public static void CrashMain(String parameter) {
        switch (parameter) {
            case "running":
                getRunning();
                break;
            case "list":
                getList();
                break;
            case "log":
                getRLog();
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        getList();
    }

    private static void getRunning() {
        StringBuffer sb = new StringBuffer();
        for (CmdExecutor ce : Client.tasks) {
            sb.append(ce.funcName + "\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private static void getList() {
        HashMap<String, String> hashMap = null;
        StringBuffer sb = new StringBuffer();
        try {
            Socket socket = new Socket(Client.ip, Client.port);
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            hashMap = (HashMap) ois.readObject();
            Iterator<Map.Entry<String, String>> iterator = hashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                String fileName = entry.getKey();//下载链接
      //          String md5 = entry.getValue();
                sb.append(fileName.replace(".jar","") + "\n");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
        JOptionPane.showMessageDialog(null, sb.toString());

    }

    private static void getRLog() {

    }
}
