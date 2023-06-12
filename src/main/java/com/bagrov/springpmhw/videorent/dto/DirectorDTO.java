package com.bagrov.springpmhw.videorent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectorDTO {

    private String directorsFIO;
    private String position;
    private List<Integer> filmsIds;
}
