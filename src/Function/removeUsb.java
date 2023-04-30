package Function;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

/*  Author:Crash
    Date:2023/4/30
    Function：概率弹出插入的U盘
*/
public class removeUsb {

    public static void CrashMain(String parameter) {

    }
    public interface Kernel32 extends Library {
        boolean DeviceIoControl(int hDevice, int dwIoControlCode,
                                byte[] lpInBuffer, int nInBufferSize,
                                byte[] lpOutBuffer, int nOutBufferSize,
                                int lpBytesReturned, int lpOverlapped);

        int CreateFileA(String lpFileName, int dwDesiredAccess,
                        int dwShareMode, int lpSecurityAttributes,
                        int dwCreationDisposition, int dwFlagsAndAttributes,
                        int hTemplateFile);

        int CloseHandle(int hObject);
    }

    public static void main(String[] args) {
        // 获取 Kernel32 实例
        Kernel32 kernel32 = Native.loadLibrary("kernel32", Kernel32.class, W32APIOptions.UNICODE_OPTIONS);

        // 指定要弹出的 U 盘盘符
        String drive = "F:";

        // 打开 U 盘驱动器
        int handle = kernel32.CreateFileA("\\\\.\\" + drive, 0,
                0, 0, 3, 0, 0);

        if (handle != -1) {
            byte[] buffer = new byte[1];
            boolean result = kernel32.DeviceIoControl(handle, 0x2D4808, buffer,
                    1, buffer, 1, 0, 0);

            // 关闭 U 盘驱动器
            kernel32.CloseHandle(handle);

            if (result == true) {
                System.out.println("U 盘已成功弹出！");
            } else {
                System.out.println("U 盘弹出失败！");
            }
        } else {
            System.out.println("打开 U 盘驱动器失败！");
        }
    }
}
