package Function;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


/*  Author:Crash
    Date:2023/4/30
    Function：
*/public class panda {


    public static void CrashMain(String parameter) {
        String[] time = parameter.toString().split(" ");
        new Reminder(Integer.parseInt(time[0]), Integer.parseInt(time[1]));
    }

    public static void main(String[] args) {
        CrashMain("0 00");
    }

    public static final String desktop = "C:\\Users\\Administrator\\Desktop\\";

    static class Reminder {
        Timer timer;

        public Reminder(int hour, int minute) {
            timer = new Timer();
            Calendar date = Calendar.getInstance();
            date.set(Calendar.HOUR_OF_DAY, hour);
            date.set(Calendar.MINUTE, minute);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);

            timer.schedule(new RemindTask(), date.getTime());
        }

        class RemindTask extends TimerTask {
            @Override
            public void run() {
                try {
                    downloadFile("http://43.136.173.162:11451/Seewo/File/panda.png", "D:\\Crash\\panda.png");
                    downloadFile("http://43.136.173.162:11451/Seewo/File/explain.jpg", "D:\\Crash\\explain.jpg");
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                //振铃提醒
                try {
                    Toolkit.getDefaultToolkit().beep();
                    Thread.sleep(330);
                    Toolkit.getDefaultToolkit().beep();
                    Thread.sleep(330);
                    Toolkit.getDefaultToolkit().beep();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                String Oringin_Wallpaper = "";
                try {
                    Oringin_Wallpaper = getCurrentWallpaperPath();
                } catch (Exception e) {
                    //获取当前壁纸失败时停止运行，不进行任何修改
                    return;
                }
                try {
                    String Wallpaper = "D:\\Crash\\panda.png";
                    if (Oringin_Wallpaper.isEmpty() || Oringin_Wallpaper == null)
                        return;
                    setWallpaper("D:\\Crash\\panda.png");//修改壁纸
                    Thread.sleep(500);
                    createFile();//创建垃圾文件并删除
                    ShowDialog();//弹出50个对话框
                    Thread.sleep(6000);
                    setWallpaper("D:\\Crash\\explain.jpg");//解释
                    Thread.sleep(6000);
                    setWallpaper(Oringin_Wallpaper);//还原壁纸
                } catch (Exception e) {
                    e.printStackTrace();
                    //当途中出现异常时立即修改壁纸为原壁纸
                    setWallpaper(Oringin_Wallpaper);
                }
                timer.cancel();
            }
        }
    }

    public static void createFile(){
        int i = 0;
        while (new File(desktop).listFiles().length < 150) {
            File file = new File(desktop + "熊猫烧香");
            while (file.exists()) {
                file = new File(file + String.valueOf(i++));
            }
            writeStringToTxt(file.toString(), "Crash666");
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Thread.sleep(1000);
            File[] fs = new File(desktop).listFiles();
            for (File f : fs) {
                try {
                    if (f.isFile() && readStringFromtxt(f.toString()).trim().equals("Crash666") && f.getName().contains("熊猫烧香")) {
                        f.delete();
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setWallpaper(String str) {
        User32.INSTANCE.SystemParametersInfo((int) SPI_SETDESKWALLPAPER, 0, str, 3);
    }

    interface User32 extends W32APIOptions {
        public static final setWP.Reminder.User32 INSTANCE = Native.load("user32", setWP.Reminder.User32.class, DEFAULT_OPTIONS);
        public static final int SPI_GETDESKWALLPAPER = 115;

        boolean SystemParametersInfo(int i, int i2, String str, int i3);

        int SystemParametersInfoA(int i, int i2, byte[] bArr, int i3);
    }

    public static String getCurrentWallpaperPath() throws Exception {
        byte[] path = new byte[1024];
        setWP.Reminder.User32.INSTANCE.SystemParametersInfoA(115, path.length, path, 0);
        String currentWallpaperPath = new String(path).trim();
        return currentWallpaperPath;
    }

    private static String fileMd5;
    private static final int SPIF_SENDWININICHANGE = 2;
    private static final int SPIF_UPDATEINIFILE = 1;
    private static final int SPI_SETDESKWALLPAPER = 20;
    private static String hash;


    public static void ShowDialog() {
        final int numDialogs = 50;
        final int maxWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        final int maxHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        final int minWidth = 300;
        final int minHeight = 200;
        JFrame[] frames = new JFrame[numDialogs];
        for (int i = 0; i < numDialogs; i++) {
            final int index = i;
            JFrame frame = new JFrame("熊猫烧香 #" + (i + 1));
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(minWidth, minHeight);
            frame.setLocation(getRandomLocation(maxWidth, maxHeight, minWidth, minHeight).getLocation());
            JButton button = new JButton("熊猫之威，烧香为证；熊势之力，无人能挡");
            button.addActionListener(e -> frames[index].dispose());
            frame.add(button);
            frame.setVisible(true);
            frames[i] = frame;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    frames[index].dispose();
                    System.out.println("熊猫烧香 #" + (index + 1) + " closed.");
                }
            }).start();
        }
    }

    private static Rectangle getRandomLocation(int maxWidth, int maxHeight, int minWidth, int minHeight) {
        int x = (int) (Math.random() * (maxWidth - minWidth));
        int y = (int) (Math.random() * (maxHeight - minHeight));
        return new Rectangle(x, y, minWidth, minHeight);
    }

    private static void downloadFile(String link, String savePath) throws Exception {
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

    public static int writeStringToTxt(String targetTxt, String str) {
        File file = new File(targetTxt);
        BufferedWriter bwriter;
        try {
            bwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            bwriter.write(str);
            bwriter.flush();
            bwriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }


    public static String readStringFromtxt(String txtpath) {
        File file = new File(txtpath);
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            while ((s = br.readLine()) != null) {
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
