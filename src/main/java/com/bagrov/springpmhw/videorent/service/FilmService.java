package com.bagrov.springpmhw.videorent.service;

import com.bagrov.springpmhw.videorent.dto.FilmDTO;
import com.bagrov.springpmhw.videorent.model.Director;
import com.bagrov.springpmhw.videorent.model.Film;
import com.bagrov.springpmhw.videorent.model.Order;
import com.bagrov.springpmhw.videorent.repository.DirectorRepository;
import com.bagrov.springpmhw.videorent.repository.FilmRepository;
import com.bagrov.springpmhw.videorent.repository.OrderRepository;
import com.bagrov.springpmhw.videorent.repository.UserRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FilmService {
    private final FilmRepository filmRepository;
    private final ModelMapper modelMapper;
    private final DirectorRepository directorRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public FilmService(FilmRepository filmRepository, ModelMapper modelMapper
            , DirectorRepository directorRepository, UserRepository userRepository, OrderRepository orderRepository) {
        this.filmRepository = filmRepository;
        this.modelMapper = modelMapper;
        this.directorRepository = directorRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public List<FilmDTO> findAll() {
        return filmRepository.findAll().stream().map(this::convertToFilmDTO).toList();
    }

    public List<Film> findAllFilms() {
        return filmRepository.findAll();
    }

    public FilmDTO getOne(int id) {
        return convertToFilmDTO(filmRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с указанным id не найден")));
    }

    @Transactional
    public FilmDTO save(FilmDTO filmDTO) {
        return convertToFilmDTO(filmRepository.save(convertToFilm(filmDTO)));
    }

    @Transactional
    public FilmDTO update(int id, FilmDTO filmDTO) {
        Optional<Film> optionalFilm = filmRepository.findById(id);
        if (optionalFilm.isPresent()) {
            Film updatedFilm = enrichFilm(optionalFilm, filmDTO);
            updatedFilm.setId(id);
            return convertToFilmDTO(filmRepository.save(updatedFilm));
        }
        throw new NotFoundException("Фильм с указанным id не найден");
    }

    @Transactional
    public void delete(int id) {
        if (filmRepository.findById(id).isPresent()) {
            filmRepository.deleteById(id);
        } else {
            throw new NotFoundException("Фильм с указанным id не найден");
        }
    }

    @Transactional
    public FilmDTO addDirector(int directorId, int filmId)  {
        Director director = directorRepository.findById(directorId)
                .orElseThrow(() -> new NotFoundException("Режиссер с указанным id не найден"));
        FilmDTO filmDTO = getOne(filmId);
        filmDTO.getDirectorsIds().add(director.getId());
        update(filmId, filmDTO);
        return filmDTO;
    }

    @Transactional
    public Film addDirector(String directorsFIO, String filmTitle)  {
        Director director = directorRepository.findByDirectorsFIO(directorsFIO);
        Film film = filmRepository.findByTitle(filmTitle);
        if (!film.getDirectors().contains(director)) {
            film.getDirectors().add(director);
        }
        return film;
    }

    @Transactional
    public void rentFilm(int filmId, int userId, int rentPeriod) {
        Order order = new Order();
        order.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным id не найден")));
        order.setFilm(filmRepository.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с указанным id не найден")));
        order.setRentDate(LocalDateTime.now());
        order.setRentPeriod(rentPeriod);
        order.setPurchase(true);
        orderRepository.save(order);
    }

    public FilmDTO convertToFilmDTO(Film film) {
        if (modelMapper.getTypeMap(Film.class, FilmDTO.class) == null) {
            modelMapper.createTypeMap(Film.class, FilmDTO.class)
                    .addMappings(mapper -> mapper.skip(FilmDTO::setDirectorsIds))
                    .setPostConverter(postConverterToFilmDTO());
        }
        return modelMapper.map(film, FilmDTO.class);
    }

    private Converter<Film, FilmDTO> postConverterToFilmDTO()   {
        return context -> {
            Film film = context.getSource();
            FilmDTO filmDTO = context.getDestination();
            filmDTO.setDirectorsIds(
                    Objects.isNull(film) || Objects.isNull(film.getDirectors())
                            ? Collections.emptyList()
                            : film.getDirectors().stream().map(Director::getId).collect(Collectors.toList())
            );
            return context.getDestination();
        };
    }

    private Film convertToFilm(FilmDTO filmDTO) {
        if (modelMapper.getTypeMap(FilmDTO.class, Film.class) == null) {
            modelMapper.createTypeMap(FilmDTO.class, Film.class)
                    .addMappings(mapper -> mapper.skip(Film::setDirectors))
                    .setPostConverter(postConverterToFilm());
        }
        return modelMapper.map(filmDTO, Film.class);
    }

    private Converter<FilmDTO, Film> postConverterToFilm()   {
        return context -> {
            FilmDTO filmDTO = context.getSource();
            Film film = context.getDestination();
            if (!Objects.isNull(filmDTO.getDirectorsIds()))  {
                film.setDirectors(directorRepository.findAllById(filmDTO.getDirectorsIds()));
            } else {
                film.setDirectors(Collections.emptyList());
            }
            return context.getDestination();
        };
    }

    private Film enrichFilm(Optional<Film> optionalFilm, FilmDTO filmDTO) {
        Film updatedFilm = new Film();
        if (filmDTO.getTitle() != null) {
            updatedFilm.setTitle(filmDTO.getTitle());
        } else {
            updatedFilm.setTitle(optionalFilm.get().getTitle());
        }

        if (filmDTO.getPremierYear() != null) {
            updatedFilm.setPremierYear(filmDTO.getPremierYear());
        } else {
            updatedFilm.setPremierYear(optionalFilm.get().getPremierYear());
        }

        if (filmDTO.getCountry() != null) {
            updatedFilm.setCountry(filmDTO.getCountry());
        } else {
            updatedFilm.setCountry(optionalFilm.get().getCountry());
        }

        if (filmDTO.getGenre() != null) {
            updatedFilm.setGenre(filmDTO.getGenre());
        } else {
            updatedFilm.setGenre(optionalFilm.get().getGenre());
        }

        if (filmDTO.getDirectorsIds() != null) {
            updatedFilm.setDirectors(convertToFilm(filmDTO).getDirectors());
        } else {
            updatedFilm.setDirectors(optionalFilm.get().getDirectors());
        }
        return updatedFilm;
    }
}
