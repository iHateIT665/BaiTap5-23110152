package vn.iotstar.service;

import java.util.List;
import java.util.Optional;
import vn.iotstar.entity.Video;

public interface VideoService {
    List<Video> findAll();
    Optional<Video> findById(String id);
    Video save(Video video);
    void deleteById(String id);
}