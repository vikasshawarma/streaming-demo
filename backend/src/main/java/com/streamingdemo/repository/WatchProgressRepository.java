package com.streamingdemo.repository;

import com.streamingdemo.domain.WatchProgress;
import com.streamingdemo.domain.WatchProgressId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchProgressRepository extends JpaRepository<WatchProgress, WatchProgressId> {

    List<WatchProgress> findByUserId(String userId);
}
