package Function;
/*  Author:Crash
    Date:2023/4/29
    Function：下载服务器指定文件夹下某文件到桌面
*/

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;


public class get {
    public static void CrashMain(String parameter) {
        String url = "http://43.136.173.162:11451/Seewo/File/"+parameter;
        File f = new File("C:\\Users\\Administrator\\Desktop\\"+url.substring(url.lastIndexOf("/")));
        while (f.exists())
            f = new File(f.getParent()+"/1"+f.getName());
        downloadFile(url,f.toString());

    }

    public static void main(String[] args) {

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
                    JOptionPane.showMessageDialog(null, "下载成功");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
