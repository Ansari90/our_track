package tech.cube.soundtest.composers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tech.cube.soundtest.constants.Constants;
import tech.cube.soundtest.utilities.SAK;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.SequenceInputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class RandomComposer {

    private static final Logger log = LoggerFactory.getLogger(RandomComposer.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void merge() {

        Date now = new Date();
        log.info("The time is now {}", dateFormat.format(now));

        AudioInputStream appendedFiles = null, clip;
        File[] allTracks = new File(Constants.TRACK_FOLDER).listFiles();

        try {
            for(int count = 0; count < 10; count++) {
                File toMerge = allTracks[SAK.RANDOM(0, allTracks.length - 1)];
                clip = AudioSystem.getAudioInputStream(toMerge);
                log.info("{}. Merging " + toMerge.getName(), count);

                if(appendedFiles == null) {
                    count++;
                    appendedFiles = AudioSystem.getAudioInputStream(allTracks[SAK.RANDOM(0, allTracks.length - 1)]);
                }
                
                appendedFiles = new AudioInputStream(new SequenceInputStream(clip, appendedFiles),
                        clip.getFormat(), clip.getFrameLength() + appendedFiles.getFrameLength());
            }

            AudioSystem.write(appendedFiles,
                    AudioFileFormat.Type.WAVE,
                    new File(Constants.MERGE_FOLDER + now.getTime() + "-" + java.util.UUID.randomUUID().toString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
