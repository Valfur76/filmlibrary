package com.bagrov.springpmhw.videorent.service;

import com.bagrov.springpmhw.videorent.dto.DirectorDTO;
import com.bagrov.springpmhw.videorent.model.Director;
import com.bagrov.springpmhw.videorent.model.Film;
import com.bagrov.springpmhw.videorent.repository.DirectorRepository;
import com.bagrov.springpmhw.videorent.repository.FilmRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DirectorService {
    private final DirectorRepository directorRepository;
    private final FilmRepository filmRepository;
    private final ModelMapper modelMapper;

    public DirectorService(DirectorRepository directorRepository, FilmRepository filmRepository, ModelMapper modelMapper) {
        this.directorRepository = directorRepository;
        this.filmRepository = filmRepository;
        this.modelMapper = modelMapper;
    }

    public List<DirectorDTO> findAll() {
        return directorRepository.findAll().stream().map(this::convertToDirectorDTO).toList();
    }

    public DirectorDTO getOne(int id) {
        return convertToDirectorDTO(directorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Режиссер с указанным id не найден")));
    }

    @Transactional
    public DirectorDTO save(DirectorDTO directorDTO) {
        return convertToDirectorDTO(directorRepository.save(convertToDirector(directorDTO)));
    }

    @Transactional
    public DirectorDTO update(int id, DirectorDTO directorDTO) {
        Optional<Director> optionalDirector = directorRepository.findById(id);
        if (optionalDirector.isPresent()) {
            Director updatedDirector = enrichDirector(optionalDirector, directorDTO);
            updatedDirector.setId(id);
            return convertToDirectorDTO(directorRepository.save(updatedDirector));
        }
        throw new NotFoundException("Режиссер с указанным id не найден");
    }

    @Transactional
    public void delete(int id) {
        if (directorRepository.findById(id).isPresent()) {
            directorRepository.deleteById(id);
        } else {
            throw new NotFoundException("Режиссер с указанным id не найден");
        }
    }

    @Transactional
    public DirectorDTO addFilm(int filmId, int directorId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с указанным id не найден"));
        DirectorDTO directorDTO = getOne(directorId);
        if (!directorDTO.getFilmsIds().contains(filmId)) {
            directorDTO.getFilmsIds().add(film.getId());
            update(directorId, directorDTO);
        }
        return directorDTO;
    }

    public DirectorDTO convertToDirectorDTO(Director director) {
        if (modelMapper.getTypeMap(Director.class, DirectorDTO.class) == null) {
            modelMapper.createTypeMap(Director.class, DirectorDTO.class)
                    .addMappings(mapper -> mapper.skip(DirectorDTO::setFilmsIds))
                    .setPostConverter(postConverterToDirectorDTO());
        }
        return modelMapper.map(director, DirectorDTO.class);
    }

    private Converter<Director, DirectorDTO> postConverterToDirectorDTO() {
        return context -> {
            Director director = context.getSource();
            DirectorDTO directorDTO = context.getDestination();
            directorDTO.setFilmsIds(
                    Objects.isNull(director) || Objects.isNull(director.getFilms())
                            ? Collections.emptyList()
                            : director.getFilms().stream().map(Film::getId).collect(Collectors.toList())
            );
            return context.getDestination();
        };
    }

    private Director convertToDirector(DirectorDTO directorDTO) {
        if (modelMapper.getTypeMap(DirectorDTO.class, Director.class) == null) {
            modelMapper.createTypeMap(DirectorDTO.class, Director.class)
                    .addMappings(mapper -> mapper.skip(Director::setFilms))
                    .setPostConverter(postConverterToDirector());
        }
        return modelMapper.map(directorDTO, Director.class);
    }

    private Converter<DirectorDTO, Director> postConverterToDirector() {
        return context -> {
            DirectorDTO directorDTO = context.getSource();
            Director director = context.getDestination();
            if (!Objects.isNull(directorDTO.getFilmsIds())) {
                director.setFilms(filmRepository.findAllById(directorDTO.getFilmsIds()));
            } else {
                director.setFilms(Collections.emptyList());
            }
            return context.getDestination();
        };
    }

    private Director enrichDirector(Optional<Director> optionalDirector, DirectorDTO directorDTO) {
        Director updatedDirector = new Director();
        if (directorDTO.getDirectorsFIO() != null) {
            updatedDirector.setDirectorsFIO(directorDTO.getDirectorsFIO());
        } else {
            updatedDirector.setDirectorsFIO(optionalDirector.get().getDirectorsFIO());
        }

        if (directorDTO.getPosition() != null) {
            updatedDirector.setPosition(directorDTO.getPosition());
        } else {
            updatedDirector.setPosition(optionalDirector.get().getPosition());
        }

        if (directorDTO.getFilmsIds() != null) {
            updatedDirector.setFilms(convertToDirector(directorDTO).getFilms());
        } else {
            updatedDirector.setFilms(optionalDirector.get().getFilms());
        }

        return updatedDirector;
    }
}
