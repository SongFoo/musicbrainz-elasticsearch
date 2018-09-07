/**
 * Copyright 2013 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.javaetmoi.elasticsearch.musicbrainz.batch.mapper;

import com.javaetmoi.elasticsearch.musicbrainz.domain.Album;
import com.javaetmoi.elasticsearch.musicbrainz.domain.Artist;
import com.javaetmoi.elasticsearch.musicbrainz.domain.ArtistType;
import com.javaetmoi.elasticsearch.musicbrainz.domain.Track;
import fm.last.musicbrainz.data.model.Gender;
import fm.last.musicbrainz.data.model.ReleaseGroupPrimaryType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TrackRowMapper implements RowMapper<Track> {

    @Override
    public Track mapRow(ResultSet rs, int rowNum) throws SQLException {
        Track track = new Track();
        track.setId(rs.getInt("trackId"));
        track.setGid(rs.getString("trackGid"));
        track.setName(rs.getString("trackName"));
        Album album = track.getAlbum();

        album.setId(rs.getInt("albumId"));
        album.setGid(rs.getString("albumGid"));
        album.setName(rs.getString("albumName"));
        album.setType(ReleaseGroupPrimaryType.ALBUM);
        album.setYear(rs.getInt("albumYear"));
        album.getRating().setScore(rs.getInt("albumRatingScore"));
        album.getRating().setCount(rs.getInt("albumRatingCount"));


        Artist artist = album.getArtist();
        artist.setGid(rs.getString("artistGid"));
        artist.setName(rs.getString("artistName"));


        artist.setBeginDateYear(rs.getString("artistBeginDateYear"));
        if (rs.getObject("artistTypeId") != null) {
            artist.setType(ArtistType.valueOf(rs.getInt("artistTypeId")));
        }
        if (rs.getObject("artistGenderId") != null) {
            artist.setGender(Gender.valueOf(rs.getInt("artistGenderId")));
        }
        artist.setCountry(rs.getString("artistCountryName"));
        artist.getRating().setScore(rs.getInt("artistRatingScore"));
        artist.getRating().setCount(rs.getInt("artistRatingCount"));

        track.getRating().setScore(rs.getInt("trackRating"));
        track.getRating().setCount(rs.getInt("trackRatingCount"));
        track.setRecordingId(rs.getInt("recordingid"));

        return track;
    }
}