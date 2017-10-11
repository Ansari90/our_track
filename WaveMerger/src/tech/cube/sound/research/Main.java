package tech.cube.sound.research;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.SequenceInputStream;

public class Main {

    public static final String BASE_PATH = "/Users/ansari/tracks/wav";

    public static void main(String[] args) {

        AudioInputStream appendedFiles = null;
        AudioInputStream clip, clip2;
        try {
            for(int count = 1; count < 7; count++) {
                clip = AudioSystem.getAudioInputStream(new File(BASE_PATH + count + ".wav"));

                if(count == 1) {
                    count++;
                    clip2 = AudioSystem.getAudioInputStream(new File(BASE_PATH + count + ".wav"));
                } else {
                    clip2 = appendedFiles;
                }

                appendedFiles = new AudioInputStream(new SequenceInputStream(clip, clip2),
                                        clip .getFormat(), clip.getFrameLength() + clip2.getFrameLength());
            }

            AudioSystem.write(appendedFiles,
                    AudioFileFormat.Type.WAVE,
                    new File("/Users/ansari/tracks/wavAppended.wav"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
