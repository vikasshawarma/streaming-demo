package com.streamingdemo.repository;

import com.streamingdemo.domain.Title;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TitleRepository extends JpaRepository<Title, String> {

    List<Title> findByBrowseRowIdOrderByPopularityDesc(String browseRowId);

    List<Title> findAllByOrderByPopularityDesc();
}
