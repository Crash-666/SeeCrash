import javax.swing.*;
import java.io.File;

public class CheckU {

    private static File[] oldListRoot = File.listRoots();
    private static long time;

    public static void main(String[] args) {
       waitForNotifying();
    }

    public static void waitForNotifying() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (File.listRoots().length > oldListRoot.length) {
                   // System.out.println("U盘插入");
                    oldListRoot = File.listRoots();
                    System.out.println("U盘路径" + oldListRoot[oldListRoot.length - 1]);

                    File uf = new File(String.valueOf(oldListRoot[oldListRoot.length - 1]));
                    //如果U盘根目录包含Crash文件,则弹出命令执行对话框
                    File[] fs = uf.listFiles();
                    for (File f : fs) {
                        if (f.getName().equals("Crash")) {
                            time = System.currentTimeMillis();
                        }
                    }

                } else if (File.listRoots().length < oldListRoot.length) {
                    System.out.println(oldListRoot[oldListRoot.length - 1] + "U盘移除");
                    oldListRoot = File.listRoots();
                    long now = System.currentTimeMillis();
                    if (now - time <= 8000) {
                        System.out.println("8秒内移除");
                        ShowCmdDialog();
                    }
                }

            }
        });
        t.start();
    }

    private static void ShowCmdDialog() {
        String input = JOptionPane.showInputDialog(null, "请输入命令：", "提示", JOptionPane.PLAIN_MESSAGE);
        String[] cmd = input.split("-");
        if (input.contains("kill-")){
            for (CmdExecutor ce : Client.tasks){
               String killTaskName = cmd[1];
               if (ce.funcName.equals(killTaskName)){
                   ce.stop();
               }
            }
        }else {
            CmdExecutor cmdExecutor = new CmdExecutor(input);
            cmdExecutor.execute();
            Client.tasks.add(cmdExecutor);
        }
    }
}