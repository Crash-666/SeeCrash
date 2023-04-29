import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.*;

public class Client {
    public final static String JarPath = "D:\\Crash\\";
    public static List<CmdExecutor> tasks = new ArrayList<CmdExecutor>();
    public static Object lock = new Object();

    private static final String ip = "127.0.0.1";
    private static final int port = 8888;


    public static void main(String[] args) {
        //创建程序目录
        if (!new File(JarPath).exists()) new File(JarPath).mkdirs();

        //获取Jar并选择性下载
        getFuncJar();

        //启动U盘检测线程
        CheckU.waitForNotifying();

        //启动被设置为开机自启的jar
    }

    private static void init( HashMap<String, String> hashMap){
        Iterator<Map.Entry<String, String>> iterator = hashMap.entrySet().iterator();
        boolean isDownload = true;
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String fileName = entry.getKey();//下载链接
            String md5 = entry.getValue();
            System.out.println("fileName: " + fileName + ", md5: " + md5);
            File[] fs = new File(JarPath).listFiles();

            for (File f : fs) {
                if (f.getName().equals(fileName)) {
                  if (!getFileMd5(f).equals(md5)){
                      
                  }
                }
            }
        }
    }

    private static void getFuncJar() {
        try {
            Socket socket = new Socket(ip, port);
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            HashMap<String, String> hashMap = (HashMap) ois.readObject();
            Iterator<Map.Entry<String, String>> iterator = hashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                String fileName = entry.getKey();//下载链接
                String md5 = entry.getValue();
                System.out.println("fileName: " + fileName + ", md5: " + md5);
                String link = "http://43.136.173.162:11451/Seewo/Jar/" + fileName;//拼接链接

                //判断Jar是否存在
                if (!new File(JarPath + fileName).exists()) {
                    if (!new File(JarPath).exists()) new File(JarPath).mkdirs();
                    downloadFile(link, JarPath + fileName);
                }
                //判断md5是否对应，若不则下载
                File[] fs = new File(JarPath).listFiles();
                for (File f : fs) {
                    if (f.getName().equals(fileName) && !getFileMd5(f).equals(md5)) {
                        if (!new File(JarPath).exists())
                            new File(JarPath).mkdirs();
                        //如果本地jar的md5和服务器不一致，则下载
                        downloadFile(link, JarPath + fileName);
                    }
                }

                init(hashMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFileMd5(File file) {
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            InputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            inputStream.close();
            byte[] digest = md.digest();
            md5 = DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    public static void downloadFile(String link, String savePath) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();
                    InputStream inputStream = new BufferedInputStream(conn.getInputStream());
                    OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(savePath));
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, len);
                    }
                    outputStream.flush();
                    inputStream.close();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}