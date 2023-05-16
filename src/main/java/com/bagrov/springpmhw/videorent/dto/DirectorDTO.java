package com.bagrov.springpmhw.videorent.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DirectorDTO {

    private String directorsFIO;
    private String position;
    private List<Integer> filmsIds;
}
