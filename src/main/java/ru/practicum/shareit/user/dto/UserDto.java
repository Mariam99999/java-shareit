package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    @Size(max = 20)
    private String name;
    @Email(groups = {Create.class, Update.class})
    @Size(max = 100)
    @NotBlank(groups = {Create.class})
    private String email;
}
