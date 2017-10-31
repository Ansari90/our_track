package tech.cube.soundtest.files;

import org.springframework.web.multipart.MultipartFile;
import tech.cube.soundtest.constants.Constants;
import tech.cube.soundtest.entities.Track;

import java.io.File;
import java.nio.file.Files;

public class FileManager {

    public static void SAVE_FILE(Track track, MultipartFile file) throws Exception {

        track.setPath(Constants.TRACK_FOLDER
                    + Constants.TRACK_PREFIX + "-"
                    + track.getTimestamp() + '-'
                    + java.util.UUID.randomUUID().toString() + '-'
                    + track.getContributor());

        file.transferTo(new File(track.getPath()));
    }

    public static void GET_LATEST_FILE() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
