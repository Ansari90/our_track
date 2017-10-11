package tech.cube.soundtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/*

=====
\_|_/
/_|1\
\*|0/
/ | \
=====

 */

@SpringBootApplication
@EnableScheduling
public class SoundRunner {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SoundRunner.class, args);
    }
}
