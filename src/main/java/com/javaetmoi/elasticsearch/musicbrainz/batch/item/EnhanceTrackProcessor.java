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

import com.javaetmoi.elasticsearch.musicbrainz.domain.Track;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;


public class EnhanceTrackProcessor implements ItemProcessor<Track, Track> {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EnhanceTrackProcessor(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Track process(Track track) throws Exception {
        // Add to the album the top five tags
        String sql = "select t.name from recording_tag rgt "
                +" inner join tag t on rgt.tag = t.id"
                +" where recording=?"
                +" order by rgt.count desc"
                +" limit 5";
        List<String> tags = jdbcTemplate.queryForList(sql, String.class, track.getRecordingId());
        track.setTags(tags);
        return track;
    }

}
