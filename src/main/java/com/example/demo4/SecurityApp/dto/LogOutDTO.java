package com.example.demo4.SecurityApp.dto;

import com.example.demo4.SecurityApp.entities.User;
import lombok.*;

@Data

@NoArgsConstructor
@AllArgsConstructor
public class LogOutDTO {

    private Long userId;
    private String refreshToken;

        // Getters and setters


}
