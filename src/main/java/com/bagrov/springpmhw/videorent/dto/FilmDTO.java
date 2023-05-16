package com.bagrov.springpmhw.videorent.dto;

import com.bagrov.springpmhw.videorent.model.Genre;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FilmDTO {

    private String title;
    private String premierYear;
    private String country;
    private Genre genre;
    private List<Integer> directorsIds;
}
