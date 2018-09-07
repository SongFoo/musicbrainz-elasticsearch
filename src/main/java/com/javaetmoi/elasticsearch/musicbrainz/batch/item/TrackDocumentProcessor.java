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
package com.javaetmoi.elasticsearch.musicbrainz.batch.item;

import com.javaetmoi.core.batch.processor.EsDocumentProcessor;
import com.javaetmoi.elasticsearch.musicbrainz.domain.Album;
import com.javaetmoi.elasticsearch.musicbrainz.domain.Artist;
import com.javaetmoi.elasticsearch.musicbrainz.domain.Rating;
import com.javaetmoi.elasticsearch.musicbrainz.domain.Track;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public class TrackDocumentProcessor extends EsDocumentProcessor<Track> {
    
    
    private String documentType;

    @Override
    protected void fillContentBuilder(XContentBuilder content, Track track) throws IOException {
        content.field("id", track.getGid());
        content.field("name", track.getName());

        if ((track.getTags()!=null) && !track.getTags().isEmpty()) {
            content.array("tags", track.getTags().toArray(new String[track.getTags().size()]));
        }
        fillRatingBuilder(content, track.getRating());
        fillAlbumBuilder(content, track.getAlbum());
    }

    private void fillAlbumBuilder(XContentBuilder content, Album album) throws IOException {
        XContentBuilder albumBuilder = content.startObject("album");

        content.field("id", album.getGid());
        content.field("name", album.getName());
        content.field("year", album.getYear());
        if ((album.getTags()!=null) && !album.getTags().isEmpty()) {
            content.array("tags", album.getTags().toArray(new String[album.getTags().size()]));
        }
        fillRatingBuilder(content, album.getRating());
        fillArtistBuilder(content, album.getArtist());

        albumBuilder.endObject();

    }


    private void fillArtistBuilder(XContentBuilder content, Artist artist) throws IOException {
        XContentBuilder artistBuilder = content.startObject("artist");
        artistBuilder.field("name", artist.getName());
        artistBuilder.field("id", artist.getGid());
        if (artist.getType() != null) {
            artistBuilder.field("type_id", artist.getType().getId());
            artistBuilder.field("type_name", artist.getType().getName());
        }
        artistBuilder.field("begin_date_year", artist.getBeginDateYear());
        artistBuilder.field("country_name", artist.getCountry());
        if (artist.getGender() != null) {
            artistBuilder.field("gender", artist.getGender().getId());
        }
        fillRatingBuilder(artistBuilder, artist.getRating());
        artistBuilder.endObject();

    }

    private void fillRatingBuilder(XContentBuilder content, Rating rating) throws IOException {
        XContentBuilder ratingBuilder = content.startObject("rating");
        ratingBuilder.field("score", rating.getScore());
        ratingBuilder.field("count", rating.getCount());
        ratingBuilder.endObject();
    }

    @Override
    protected String getDocumentId(Track item) {
        return item.getGid();
    }

    @Override
    protected String getDocumentType(Track item) {
        return documentType; 
    }

    
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
    
}
