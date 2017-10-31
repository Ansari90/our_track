package tech.cube.soundtest.composers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tech.cube.soundtest.constants.Constants;
import tech.cube.soundtest.entities.TrackList;
import tech.cube.soundtest.repositories.TrackListRepository;
import tech.cube.soundtest.utilities.SAK;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.SequenceInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;

@Component
public class RandomComposer {

    private static final Logger log = LoggerFactory.getLogger(RandomComposer.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    TrackListRepository trackListRepository;

    @Scheduled(fixedRate = Constants.RANDOM_COMPOSER_RATE)
    public void merge() {

        Date now = new Date();
        log.info("The time is now {}", dateFormat.format(now));

        AudioInputStream appendedFiles = null, clip;
        ArrayList<File> allTracks = new ArrayList<>();
        File toMerge = null;

        try {
            Files.list(new File(Constants.TRACK_FOLDER).toPath()).forEach((path) -> {
                File pathFile = path.toFile();
                if(pathFile.getName().contains(".wav")) { //Constants.TRACK_PREFIX)) {
                    allTracks.add(pathFile);
                } else {
                    log.info("Rejected {}", pathFile.getName());
                }
            });

            String[] trackListComponentPaths = new String[Constants.TRACKS_PER_MINUTE];

            for(int count = 0; count < Constants.TRACKS_PER_MINUTE; count++) {
                try {
                    toMerge = allTracks.get(SAK.RANDOM(0, allTracks.size() - 1));
                    clip = AudioSystem.getAudioInputStream(toMerge);
                    log.info("{}. Merging " + toMerge.getName(), count);

                    if (appendedFiles == null) {
                        File firstTrack = allTracks.get(SAK.RANDOM(0, allTracks.size() - 1));
                        trackListComponentPaths[count++] = firstTrack.getName();
                        appendedFiles = AudioSystem.getAudioInputStream(firstTrack);
                    }

                    trackListComponentPaths[count] = toMerge.getName();
                    appendedFiles = new AudioInputStream(new SequenceInputStream(clip, appendedFiles),
                            clip.getFormat(), clip.getFrameLength() + appendedFiles.getFrameLength());

                } catch (Exception e) {
                    count--;
                    log.info("Error occurred for track # {}! File is {}" , count,
                                toMerge != null ? toMerge.getPath() : "not available!");
                    e.printStackTrace();

                } finally {
                    toMerge = null; //to ensure logging tertiary works properly, not recording previous error file
                                    // in case no error file was present (for whatever reason...)
                }
            }

            File mergeOutputFile = new File(Constants.MERGE_FOLDER +
                                            Constants.TRACK_PREFIX +
                                            now.getTime() + "-" +
                                            java.util.UUID.randomUUID().toString());
            AudioSystem.write(appendedFiles,
                                AudioFileFormat.Type.WAVE,
                                mergeOutputFile);

            TrackList trackList = new TrackList();
            trackList.setTimestamp();
            trackList.setTracks(trackListComponentPaths);
            trackList.setPath(mergeOutputFile.getPath());
            trackListRepository.save(trackList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
