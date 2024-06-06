package br.com.biopark.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.biopark.models.Video;

public interface VideoRepository extends JpaRepository<Video, Long>{}
