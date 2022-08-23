package org.spring.data.jpa.embeddedid.service;

import org.spring.data.jpa.embeddedid.model.SongDto;
import org.spring.data.jpa.embeddedid.repo.LongKeySong;
import org.spring.data.jpa.embeddedid.repo.LongKeySongId;
import org.spring.data.jpa.embeddedid.repo.LongKeySongRepository;
import org.spring.data.jpa.embeddedid.repo.Song;
import org.spring.data.jpa.embeddedid.repo.SongId;
import org.spring.data.jpa.embeddedid.repo.SongsRepository;
import org.spring.data.jpa.embeddedid.web.SongNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SongsService {
	@Autowired
	private SongsRepository repository;
	@Autowired
	private LongKeySongRepository longKeySongRepository;

	public Song addSong(SongDto dto) {
		return repository.save(dtoToSong(dto));
	}

	public Song find(SongDto dto) {
		return repository.findById(dtoToSongId(dto)).orElseThrow(SongNotFoundException::new);
	}

	private Song dtoToSong(SongDto dto) {
		return new Song(dtoToSongId(dto), dto.getDuration(), dto.getGenre(), dto.getReleaseDate(), dto.getRating(),
				dto.getDownloadUrl());
	}

	private SongId dtoToSongId(SongDto dto) {
		return new SongId(dto.getName(), dto.getAlbum(), dto.getArtist());
	}

	public List<LongKeySong> findByIdPartially(String name, String artist, String album, String coArtist,
			String composer, String soundEngineer, String producer, String recordingArtist) {
		return longKeySongRepository
				.findByIdNameAndIdArtistAndIdAlbumAndIdCoArtistAndIdComposerAndIdSoundEngineerAndIdProducerAndIdRecordingArtist(
						name, artist, album, coArtist, composer, soundEngineer, producer, recordingArtist);
	}

	public List<LongKeySong> findByIdPartiallyWithExample(String name, String artist, String album, String coArtist,
			String composer, String soundEngineer, String producer, String recordingArtist) {
		LongKeySong longKeySong = new LongKeySong();
		LongKeySongId longKeySongId = new LongKeySongId();
		longKeySong.setId(longKeySongId);

		longKeySongId.setName(name);
		longKeySongId.setAlbum(album);
		longKeySongId.setArtist(artist);
		longKeySongId.setCoArtist(coArtist);
		longKeySongId.setComposer(composer);
		longKeySongId.setSoundEngineer(soundEngineer);
		longKeySongId.setProducer(producer);
		longKeySongId.setRecordingArtist(recordingArtist);

		Example<LongKeySong> songExample = Example.of(longKeySong);
		return longKeySongRepository.findAll(songExample);
	}

}