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
package com.javaetmoi.elasticsearch.musicbrainz.batch;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.*;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import com.javaetmoi.elasticsearch.musicbrainz.batch.mapper.TrackRowMapper;
import com.javaetmoi.elasticsearch.musicbrainz.domain.Track;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.javaetmoi.elasticsearch.musicbrainz.batch.mapper.AlbumRowMapper;
import com.javaetmoi.elasticsearch.musicbrainz.domain.Album;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@DirtiesContext
public class TestDatabaseSetup {

	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private JdbcTemplate musicBrainzJdbcTemplate;

	private static volatile boolean dbInitialized = false;

	@PostConstruct
	public void initDatabase() {
		if (dbInitialized) {
			return;
		} else {
			dbInitialized = true;
		}
		System.out.println("###############################################################");
		System.out.println("============================initDatabase()============================");
		System.out.println("###############################################################");
		Operation operation = sequenceOf(StaticDataOperations.ALL_STATIC_DATA_ROWS,
				ArtistU2Operations.RELEASE_GROUP_U2_ROWS);
		DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource),
				operation);
		dbSetup.launch();
	}
	

	@Test
	public void selectReleaseGroup() {
		String sql = "SELECT "
				+"    release_group.id AS albumId,"				
				+"    release_group.gid AS albumGid,"
				+"    release_group.type AS albumPrimaryTypeId,"
				+"    release_group.name AS albumName,"
				+"    artist.name AS artistName,"
				+"    artist.gid AS artistGid,"
				+"    artist.type as artistTypeId,"	
				+"    artist.begin_date_year artistBeginDateYear,"
				+"    artist.gender as artistGenderId,"
				+"    area.name as artistCountryName,"
				+"    artist_meta.rating artistRatingScore,"
				+"    artist_meta.rating_count artistRatingCount,"
				+"    release_group_meta.first_release_date_year albumYear,"
				+"    release_group_meta.rating albumRatingScore,"
				+"    release_group_meta.rating_count albumRatingCount "
				+"FROM"
				+"    artist"
				+" INNER JOIN artist_credit_name"
				+"    ON artist_credit_name.artist = artist.id"
				+" INNER JOIN artist_credit"
				+"    ON artist_credit.id = artist_credit_name.artist_credit"
				+" INNER JOIN release_group"
				+"    ON release_group.artist_credit = artist_credit.id"
				+" INNER JOIN area"
				+"   ON artist.area = area.id"
				+" LEFT OUTER JOIN release_group_secondary_type_join"
				+"    ON release_group_secondary_type_join.release_group = release_group.id"
				+" LEFT OUTER JOIN artist_meta"
				+"    ON artist.id = artist_meta.id    "
				+" LEFT OUTER JOIN release_group_meta"
				+"    ON release_group_meta.id = release_group.id "
				+"WHERE"
				+"    release_group.type = '1'"
				+"	  AND artist_credit.artist_count = 1"
				+"    AND release_group_secondary_type_join.secondary_type IS NULL"
				+"    AND upper(artist.name) = 'U2'";
		 List<Album> albums = musicBrainzJdbcTemplate.query(sql, new AlbumRowMapper());
		assertEquals(albums.size(), 13);

	}

	@Test
	public void selectTrack() {
		String sql =
				"SELECT DISTINCT track.name AS trackName,\n" +
						"       track.position AS trackPos,\n" +
						"       track.id AS trackId,\n" +
						"       track.gid AS trackGid,\n" +
						"       track.medium AS mediumid,\n" +
						"       track.recording AS recordingid,\n" +
						"       track.artist_credit AS artist_creditid,\n" +
						"       medium.format AS mediumFormat,\n" +
						"       medium.name AS mediumName,\n" +
						"       release.name AS releaseName,\n" +
						"       release_group.id AS albumId,\n" +
						"       release_group.gid AS albumGid,\n" +
						"       release_group.type AS albumPrimaryTypeId,\n" +
						"       release_group.name AS albumName,\n" +
						"       artist_credit.name AS artistName,\n" +
						"       artist.gid AS artistGid,\n" +
						"       artist.begin_date_year AS artistBeginDateYear,\n" +
						"       artist.type AS artistTypeId,\n" +
						"       artist.gender AS artistGenderId,\n" +
						"       artist_meta.rating AS artistRatingScore,\n" +
						"       artist_meta.rating_count AS artistRatingCount,\n" +
						"       area.name AS artistCountryName,\n" +
						"       release_group_meta.first_release_date_year AS albumYear,\n" +
						"       release_group_meta.rating AS albumRatingScore,\n" +
						"       release_group_meta.rating_count AS albumRatingCount,\n" +
						"       recording_meta.rating AS trackRating,\n" +
						"       recording_meta.rating_count AS trackRatingCount\n" +
						"FROM track\n" +
						"  INNER JOIN medium ON track.medium = medium.id\n" +
						"  INNER JOIN recording ON track.recording = recording.id\n" +
						"  INNER JOIN recording_meta ON recording_meta.id = recording.id\n" +
						"  INNER JOIN release ON medium.release = release.id\n" +
						"  INNER JOIN release_group ON release_group.id = release.release_group\n" +
						"  INNER JOIN artist_credit ON artist_credit.id = release_group.artist_credit\n" +
						"  INNER JOIN artist ON artist_credit.id = artist.id\n" +
						"  INNER JOIN area ON artist.area = area.id\n" +
						"  LEFT OUTER JOIN release_group_secondary_type_join ON release_group_secondary_type_join.release_group = release_group.id\n" +
						"  LEFT OUTER JOIN release_group_meta ON release_group_meta.id = release_group.id\n" +
						"  LEFT OUTER JOIN artist_meta ON artist.id = artist_meta.id\n" +
				"WHERE  release_group.type = '1'\n" +
				"       AND artist_credit.artist_count = 1\n" +
				"       AND medium.format = 1\n" +
				"       AND release_group_secondary_type_join.secondary_type IS NULL\n" +
				"       AND track.id = 15093481\n";

		System.out.println("###############################################################");
		System.out.println("============================TRACKING============================");
		System.out.println("###############################################################");
		List<Track> tracks = musicBrainzJdbcTemplate.query(sql, new TrackRowMapper());
		System.out.println("The trax is "+tracks);
		assertEquals(tracks.size(), 1);

	}

}
