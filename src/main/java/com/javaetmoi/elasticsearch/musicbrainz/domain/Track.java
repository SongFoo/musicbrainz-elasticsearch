package com.javaetmoi.elasticsearch.musicbrainz.domain;

import java.util.List;

public class Track {
    private Integer id;
    private Integer recordingId;
    private String gid;
    private String name;

    private Album album = new Album();
    private List<String> tags;
    private Rating rating = new Rating();

    public Integer getId() {
        return id;
    }

    public void setRecordingId(Integer recordingId) {
        this.recordingId = recordingId;
    }

    public Integer getRecordingId() {
        return recordingId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Album getAlbum() {
        return album;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Rating getRating() {
        return rating;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Track{");
        sb.append("id=").append(id);
        sb.append(", recordingId=").append(recordingId);
        sb.append(", gid='").append(gid).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", album=").append(album);
        sb.append(", tags=").append(tags);
        sb.append(", rating=").append(rating);
        sb.append('}');
        return sb.toString();
    }
}
