package com.unibuc.fmi.mycinema.dto;

import com.unibuc.fmi.mycinema.validator.OnlyLetters;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    @OnlyLetters
    private String name;

    @NotBlank(message = "The email address must be provided!")
    @Email(message = "The email should have an adequate format!")
    private String email;
}
