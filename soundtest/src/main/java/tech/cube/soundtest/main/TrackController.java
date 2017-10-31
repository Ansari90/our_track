package tech.cube.soundtest.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import tech.cube.soundtest.entities.Track;
import tech.cube.soundtest.entities.TrackList;
import tech.cube.soundtest.files.FileManager;
import tech.cube.soundtest.repositories.TrackListRepository;
import tech.cube.soundtest.repositories.TrackRepository;

import java.util.Date;

@Controller
@EnableAutoConfiguration
public class TrackController {

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private TrackListRepository trackListRepository;

    @PostMapping("/submit")
    public void save(@RequestParam("file") MultipartFile file,
                     @RequestParam("contributor") String contributor,
                     @RequestParam("description") String description) {

        Track track = new Track();
        track.setContributor(contributor);
        track.setDescription(description);
        track.setTimestamp(new Date().getTime());

        try {
            FileManager.SAVE_FILE(track, file);
        } catch (Exception e) { e.printStackTrace(); }

        trackRepository.save(track);
    }

    @GetMapping("/tracks")
    public @ResponseBody String getCurrentTrack() {

        boolean found = false;
        TrackList trackList = null;
        while(!found) {
            trackList = trackListRepository.findOne(Long.valueOf((long)Math.floor(Math.random()
                                                                * trackListRepository.count())));
            if(trackList != null) found = true;
        }

        return  trackList.getPath();
    }
}