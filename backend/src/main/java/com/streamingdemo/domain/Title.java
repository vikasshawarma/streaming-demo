package com.streamingdemo.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "titles")
public class Title {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TitleType type;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private int runtimeSec;

    @Column(nullable = false)
    private String posterPath;

    @Column(nullable = false)
    private String previewManifestPath;

    @Column(nullable = false)
    private String featureManifestPath;

    @Column(nullable = false)
    private String introPath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IntroPolicy introPolicy = IntroPolicy.ALWAYS;

    @Column(nullable = false)
    private int creditsStartSec;

    private String collectionId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "title_genres", joinColumns = @JoinColumn(name = "title_id"))
    @Column(name = "genre")
    private List<String> genres = new ArrayList<>();

    @Column(nullable = false)
    private int popularity = 0;

    @Column(nullable = false)
    private String browseRowId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TitleType getType() {
        return type;
    }

    public void setType(TitleType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRuntimeSec() {
        return runtimeSec;
    }

    public void setRuntimeSec(int runtimeSec) {
        this.runtimeSec = runtimeSec;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPreviewManifestPath() {
        return previewManifestPath;
    }

    public void setPreviewManifestPath(String previewManifestPath) {
        this.previewManifestPath = previewManifestPath;
    }

    public String getFeatureManifestPath() {
        return featureManifestPath;
    }

    public void setFeatureManifestPath(String featureManifestPath) {
        this.featureManifestPath = featureManifestPath;
    }

    public String getIntroPath() {
        return introPath;
    }

    public void setIntroPath(String introPath) {
        this.introPath = introPath;
    }

    public IntroPolicy getIntroPolicy() {
        return introPolicy;
    }

    public void setIntroPolicy(IntroPolicy introPolicy) {
        this.introPolicy = introPolicy;
    }

    public int getCreditsStartSec() {
        return creditsStartSec;
    }

    public void setCreditsStartSec(int creditsStartSec) {
        this.creditsStartSec = creditsStartSec;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getBrowseRowId() {
        return browseRowId;
    }

    public void setBrowseRowId(String browseRowId) {
        this.browseRowId = browseRowId;
    }
}
