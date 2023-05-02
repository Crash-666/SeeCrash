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

    public static final String ip = "43.136.173.162";
    public static final int port = 4000;


    public static void main(String[] args) {
        //创建程序目录
        if (!new File(JarPath).exists()) new File(JarPath).mkdirs();

        //获取Jar并选择性下载
        getFuncJar();

        //启动U盘检测线程
        CheckU.waitForNotifying();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if (new File("D:\\Crash\\xxxxxx").exists())
                        System.exit(0);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    //启动开机自启jar
    private static void init() {
        if (!new File(JarPath + "StartUp.txt").exists())
            return;
        String StartUpTask = FileUtil.readStringFromtxt(JarPath + "StartUp.txt");
        String[] tasks = StartUpTask.split("\n");
        for (String task:tasks){
            if (!task.equals("")&&task != null&&!task.equals("\n")&&task.contains("-kill")){
                CmdExecutor cmdExecutor = new CmdExecutor(task);
                cmdExecutor.execute();
                Client.tasks.add(cmdExecutor);
            }
        }
    }

    private static boolean getDownlSuc(HashMap<String, String> hashMap) {
        Iterator<Map.Entry<String, String>> iterator = hashMap.entrySet().iterator();
        boolean isDownload = true;
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String fileName = entry.getKey();//下载链接
            String md5 = entry.getValue();
          //  System.out.println("fileName: " + fileName + ", md5: " + md5);
            File[] fs = new File(JarPath).listFiles();
            for (File f : fs) {
                if (f.getName().equals(fileName)) {
                    if (!getFileMd5(f).toLowerCase().equals(md5)) {
                        isDownload = false;
                    }
                }
            }
        }
        return isDownload;
    }

    private static void getFuncJar() {
        HashMap<String, String> hashMap = null;
        try {
            Socket socket = new Socket(ip, port);
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
           hashMap = (HashMap) ois.readObject();
            Iterator<Map.Entry<String, String>> iterator = hashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                String fileName = entry.getKey();//下载链接
                String md5 = entry.getValue();

                String link = "http://43.136.173.162:11451/Seewo/Jar/" + fileName;//拼接链接

                //判断Jar是否存在
                if (!new File(JarPath + fileName).exists()) {
                    if (!new File(JarPath).exists()) new File(JarPath).mkdirs();
                    System.out.println("Jar不存在");
                    System.out.println("fileName: " + fileName + ", md5: " + md5);
                    downloadFile(link, JarPath + fileName);
                }
                //判断md5是否对应，若不则下载
                File[] fs = new File(JarPath).listFiles();
                for (File f : fs) {
                    String a = getFileMd5(f).toLowerCase();
                    if (f.getName().equals(fileName) && !a.equals(md5)) {
                        if (!new File(JarPath).exists())
                            new File(JarPath).mkdirs();
                        //如果本地jar的md5和服务器不一致，则下载
                        System.out.println("Jar包不一致");
                        System.out.println("fileName: " + fileName + ", md5: " + md5);
                        downloadFile(link, JarPath + fileName);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
     while (!getDownlSuc(hashMap)) {
            try {
                Thread.sleep(500);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("所有jar全部下载完成，启动！");
        init();//启动开机自启任务
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
                    System.out.println("正在下载: "+link);
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