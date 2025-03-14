package me.vasujain.booksync.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String username;
    private String email;
    private String role; // "admin" or "member"
    private String firstName;
    private String lastName;
}
