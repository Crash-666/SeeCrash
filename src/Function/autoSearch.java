package Function;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/*  Author:Crash
    Date:2023/4/29
    Function：指定时间打开浏览器自动搜索
*/public class autoSearch {
    public static String[] par;

    public static void CrashMain(String parameter) {
        par = parameter.split("\\+");

        String[] time = par[1].toString().split(" ");
        new Reminder(Integer.parseInt(time[0]),(Integer.parseInt(time[1])));
    }

    public static void main(String[] args) {
        CrashMain("哔哩哔哩+0 08");
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

                String keyword = autoSearch.par[0];
                try {
                    // 将搜索关键字进行 URL 编码
                    keyword = URLEncoder.encode(keyword, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // 使用 Desktop 类打开默认浏览器并搜索关键字
                try {
                    Desktop.getDesktop().browse(
                            new java.net.URI("https://www.bing.com/search?q=" + keyword)
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                timer.cancel();
            }
        }
    }

}