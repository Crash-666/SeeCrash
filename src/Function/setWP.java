package Function;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/*  Author:Crash
    Date:2023/4/30
    Function：指定时间切换壁纸并指定还原时间
*/public class setWP {
    public static String[] par;

    public static void CrashMain(String parameter) {
        par = parameter.split("\\+");
        String[] time = par[1].toString().split(" ");
        new Reminder(Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(par[2]));
    }

    public static void main(String[] args)  {
     //   CrashMain("3.png+20 00+8");

    }

    static class Reminder {
        Timer timer;
        int recoveryTime;

        public Reminder(int hour, int minute, int recoveryTime) {
            this.recoveryTime = recoveryTime * 1000;

            timer = new Timer();
            Calendar date = Calendar.getInstance();
            date.set(Calendar.HOUR_OF_DAY, hour);
            date.set(Calendar.MINUTE, minute);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);

            // schedule the task
            timer.schedule(new RemindTask(), date.getTime());
        }

        public static void setWallpaper(String str) {
            User32.INSTANCE.SystemParametersInfo((int) SPI_SETDESKWALLPAPER, 0, str, 3);
        }

        interface User32 extends W32APIOptions {
            public static final User32 INSTANCE = Native.load("user32", User32.class, DEFAULT_OPTIONS);
            public static final int SPI_GETDESKWALLPAPER = 115;

            boolean SystemParametersInfo(int i, int i2, String str, int i3);

            int SystemParametersInfoA(int i, int i2, byte[] bArr, int i3);
        }

        public static String getCurrentWallpaperPath() throws Exception {
            byte[] path = new byte[1024];
            User32.INSTANCE.SystemParametersInfoA(115, path.length, path, 0);
            String currentWallpaperPath = new String(path).trim();
            return currentWallpaperPath;
        }

        private static String fileMd5;
        private static final int SPIF_SENDWININICHANGE = 2;
        private static final int SPIF_UPDATEINIFILE = 1;
        private static final int SPI_SETDESKWALLPAPER = 20;
        private static String hash;


        class RemindTask extends TimerTask {
            @Override
            public void run() {

                String Oringin_Wallpaper = "";
                try {
                    Oringin_Wallpaper = getCurrentWallpaperPath();
                } catch (Exception e) {
                    return;
                    //获取当前壁纸失败时停止运行，不进行任何修改
                }
                try {
                    String Wallpaper = "D:\\Crash\\" + par[0];
                    if (Oringin_Wallpaper.isEmpty() || Oringin_Wallpaper == null)
                        return;
                    downloadFile("http://43.136.173.162:11451/Seewo/WP/"+par[0],Wallpaper);
                    Toolkit.getDefaultToolkit().beep();
                    Thread.sleep(330);
                    Toolkit.getDefaultToolkit().beep();
                    //壁纸下载完成之后开始设置
                    setWallpaper(Wallpaper);//设置第一张壁纸
                    Thread.sleep(recoveryTime);//停止
                    setWallpaper(Oringin_Wallpaper);//切换原壁纸
                } catch (Exception e) {
                    //当途中出现异常时立即修改壁纸为原壁纸
                    setWallpaper(Oringin_Wallpaper);
                }
                timer.cancel();
            }
        }
        private void downloadFile(String link,String savePath) throws Exception{
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
        }
    }
}
