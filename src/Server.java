import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.HashMap;

public class Server {
    private final static String jarPath = "/www/Crash/43.136.173.162/Seewo/Jar/";

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4000);
            while (true){
                System.out.println("服务器启动");
                Socket socket = serverSocket.accept();
                ServerThead serverThead = new ServerThead(socket);
                serverThead.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, String> getHashMap() {
        File directory = new File(jarPath);
        File[] files = directory.listFiles(); // 获取目录下的所有文件

        HashMap<String, String> fileChecksums = new HashMap<>(); // 存储文件名和MD5值的HashMap

        for (File file : files) {
            try {
                String fileName = file.getName(); // 获取文件名
                // 计算文件的MD5值
                MessageDigest md = MessageDigest.getInstance("MD5");
                FileInputStream fis = new FileInputStream(file);
                byte[] dataBytes = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(dataBytes)) != -1) {
                    md.update(dataBytes, 0, bytesRead);
                }
                byte[] mdBytes = md.digest();
                // 将MD5值转换为十六进制字符串
                StringBuilder sb = new StringBuilder();
                for (byte mdByte : mdBytes) {
                    sb.append(Integer.toString((mdByte & 0xff) + 0x100, 16).substring(1));
                }
                String md5Checksum = sb.toString();
                fileChecksums.put(fileName, md5Checksum); // 将文件名和MD5值存储到HashMap中
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return fileChecksums;
    }
}
