package Function;

import java.io.*;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/*  Author:Crash
    Date:2023/4/30
    Function：指定时间用垃圾文件填满桌面，然后5秒后删除，每添加(删除)一个文件延时0.1秒
*/public class bomb {

    public static final String desktop = "C:\\Users\\Administrator\\Desktop\\";

    public static void CrashMain(String parameter) {

        String[] time = parameter.toString().split(" ");
        new Reminder(Integer.parseInt(time[0]), Integer.parseInt(time[1]));
    }

    public static void main(String[] args) {
       // CrashMain("21 20");
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
            timer.schedule(new RemindTask(), date.getTime());
        }

        class RemindTask extends TimerTask {
            @Override
            public void run() {
                int i = 0;
                while (new File(desktop).listFiles().length <  150) {
                    File file = new File(desktop + "I am a sun");
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
                    Thread.sleep(5000);
                    File[] fs = new File(desktop).listFiles();
                    for (File f : fs) {
                        try {
                            if (f.isFile() && readStringFromtxt(f.toString()).trim().equals("Crash666") && f.getName().contains("I am a sun")) {
                                f.delete();
                                try {
                                    Thread.sleep(50);
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
                timer.cancel();
            }
        }
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

