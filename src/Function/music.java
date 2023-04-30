package Function;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/*  Author:Crash
    Date:2023/4/30
    Function：播放服务器指定音频，可指定时间
*/public class music {
    public static String[] par;
    public final static String JarPath = "D:\\Crash\\";

    public static void CrashMain(String parameter) {
        par = parameter.split("\\+");
        System.out.println(par.length);
        System.out.println(par[0]);
        String[] time = par[1].toString().split(" ");
        new Reminder(Integer.parseInt(time[0]),(Integer.parseInt(time[1])));
    }

    public static void main(String[] args) {

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

                try {
                    String path = JarPath+par[0];
                    URL url = new URL("http://43.136.173.162:11451/Seewo/Music/"+par[0]);
                    URLConnection conn = url.openConnection();
                    InputStream inputStream = new BufferedInputStream(conn.getInputStream());
                    OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path));
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, len);
                    }
                    outputStream.flush();
                    inputStream.close();
                    outputStream.close();


                    AudioInputStream am;
                    am = AudioSystem.getAudioInputStream(new File(path));
                    AudioFormat af = am.getFormat();
                    SourceDataLine sd ;
                    sd = AudioSystem.getSourceDataLine(af);
                    sd.open();
                    sd.start();
                    int sumByteRead = 0;
                    byte [] b = new byte[320];
                    while (sumByteRead != -1) {
                        sumByteRead = am.read(b, 0, b.length);
                        if(sumByteRead >= 0 ) {
                            sd.write(b, 0, b.length);

                        }
                    }
                    //关闭
                    sd.drain();
                    sd.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                timer.cancel();
            }
        }
    }
}
