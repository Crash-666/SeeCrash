package Function;
/*  Author:Crash
    Date:2023/4/29
*/

import java.io.File;

public class Test {
    public static void CrashMain(String parameter) {
        new File("D:\\Crash\\a").mkdirs();
        while (true) {
            try {
                new File("D:\\Crash\\a").mkdirs();
            } catch (Exception e) {

            }
        }
    }

    public static void main(String[] args) {
        CrashMain("");
    }
}
