package com.streamingdemo.config;

import com.streamingdemo.domain.IntroPolicy;
import com.streamingdemo.domain.Title;
import com.streamingdemo.domain.TitleType;
import com.streamingdemo.repository.TitleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final TitleRepository titleRepository;

    public DataSeeder(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (titleRepository.count() > 0) {
            return;
        }
        log.info("Seeding catalog with demo titles");
        titleRepository.saveAll(List.of(
                buildTitle(
                        "big-buck-bunny",
                        "Big Buck Bunny",
                        "A large-hearted rabbit takes on bullying bullies in this open movie.",
                        596,
                        "open-movies",
                        "trending",
                        List.of("Animation", "Comedy", "Family"),
                        100,
                        IntroPolicy.ALWAYS,
                        530),
                buildTitle(
                        "sintel",
                        "Sintel",
                        "A lonely young woman searches for a dragon she once befriended.",
                        888,
                        "open-movies",
                        "action",
                        List.of("Animation", "Fantasy", "Adventure"),
                        90,
                        IntroPolicy.FIRST_TIME,
                        800),
                buildTitle(
                        "elephants-dream",
                        "Elephants Dream",
                        "Two men explore a surreal mechanical world deep underground.",
                        653,
                        "open-movies",
                        "family",
                        List.of("Animation", "Sci-Fi"),
                        85,
                        IntroPolicy.ALWAYS,
                        580),
                buildTitle(
                        "tears-of-steel",
                        "Tears of Steel",
                        "Sci-fi short blending live action and VFX in a dystopian Amsterdam.",
                        734,
                        "blender-live",
                        "action",
                        List.of("Sci-Fi", "Action"),
                        95,
                        IntroPolicy.SKIP,
                        650)));
    }

    private Title buildTitle(
            String id,
            String name,
            String description,
            int runtimeSec,
            String collectionId,
            String browseRowId,
            List<String> genres,
            int popularity,
            IntroPolicy introPolicy,
            int creditsStartSec) {
        Title title = new Title();
        title.setId(id);
        title.setType(TitleType.MOVIE);
        title.setName(name);
        title.setDescription(description);
        title.setRuntimeSec(runtimeSec);
        title.setCollectionId(collectionId);
        title.setBrowseRowId(browseRowId);
        title.setGenres(genres);
        title.setPopularity(popularity);
        title.setIntroPolicy(introPolicy);
        title.setCreditsStartSec(creditsStartSec);
        title.setPosterPath("/titles/" + id + "/poster.jpg");
        title.setPreviewManifestPath("/titles/" + id + "/preview/preview.m3u8");
        title.setFeatureManifestPath("/titles/" + id + "/feature/master.m3u8");
        title.setIntroPath("/intro/intro.mp4");
        return title;
    }
}
