package com.bagrov.springpmhw.videorent.dto;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    private String login;
    private String password;
}
