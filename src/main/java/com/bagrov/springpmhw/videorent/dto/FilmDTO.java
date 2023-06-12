package com.bagrov.springpmhw.videorent.dto;

import com.bagrov.springpmhw.videorent.model.Genre;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FilmDTO {

    private int id;
    private String title;
    private String premierYear;
    private String country;
    private Genre genre;
    private List<Integer> directorsIds;
    private List<DirectorDTO> directorsDTO;
}
