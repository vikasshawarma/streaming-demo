package com.streamingdemo.repository;

import com.streamingdemo.domain.PlaybackSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlaybackSessionRepository extends JpaRepository<PlaybackSession, UUID> {
}
