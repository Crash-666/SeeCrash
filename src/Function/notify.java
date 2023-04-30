package Function;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/*  Author:Crash
    Date:2023/4/30
    Function：指定时间弹出指定通知
*/public class notify {
    public static String[] par;

    public static void CrashMain(String parameter) {
        //new Reminder(7,50);
        par = parameter.split("\\+");
        String[] time = par[1].toString().split(" ");
        new Reminder(Integer.parseInt(time[0]),(Integer.parseInt(time[1])));
    }

    public static void main(String[] args) {
      //  CrashMain("666+7 58");
    }

    static class Reminder {
        Timer timer;

        public Reminder(int hour, int minute) {
            timer = new Timer();
            Calendar date = Calendar.getInstance();
            date.set(Calendar.HOUR_OF_DAY, hour);
            date.set(Calendar.MINUTE, minute);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);

            // schedule the task
            timer.schedule(new Reminder.RemindTask(), date.getTime());
        }

        class RemindTask extends TimerTask {
            @Override
            public void run() {
                if (SystemTray.isSupported()) {
                    SystemTray tray = SystemTray.getSystemTray();
                    BufferedImage image = null;
                    try {
                        if (!new File("D:\\Crash\\Crash.jpg").exists()){
                            try {
                                URL url = new URL("http://43.136.173.162:11451/Seewo/File/Crash.jpg");
                                URLConnection conn = url.openConnection();
                                InputStream inputStream = new BufferedInputStream(conn.getInputStream());
                                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File("D:\\Crash\\Crash.jpg")));
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
                        image = ImageIO.read(new File("D:\\Crash\\Crash.jpg"));
                        TrayIcon trayIcon = new TrayIcon(image,"通知");
                        trayIcon.setImageAutoSize(true);
                        // 将 TrayIcon 对象添加到系统托盘中
                        tray.add(trayIcon);
                        // 弹出通知
                        trayIcon.displayMessage("Crash", par[0], TrayIcon.MessageType.INFO);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("System tray is not supported");
                }

                timer.cancel();
            }
        }
    }

}

