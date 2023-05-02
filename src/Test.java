import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;

/*  Author:Crash
    Date:2023/5/1
    Function：
*/public class Test {
    public static void main(String[] args) {
        try {
            Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
            Mixer mixer = AudioSystem.getMixer(mixerInfos[0]);
            mixer.open();

            Line.Info[] lineInfos = mixer.getTargetLineInfo();
            Line line = mixer.getLine(lineInfos[0]);
            line.open();

            FloatControl control = (FloatControl)line.getControl(FloatControl.Type.MASTER_GAIN);

            float range = control.getMaximum() - control.getMinimum();
            float gain = (range * 0.5f) + control.getMinimum();
            control.setValue(gain);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
