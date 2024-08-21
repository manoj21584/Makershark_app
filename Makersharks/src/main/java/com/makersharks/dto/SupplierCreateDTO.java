package com.makersharks.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class SupplierCreateDTO {

    @NotBlank(message = "Company name cannot be blank or contain only whitespace")
    @Pattern(regexp = "^[\\S]+$", message = "Company name must not contain any whitespace")
    private String companyName;

    @NotBlank(message = "Website cannot be blank or contain only whitespace")
    @Pattern(regexp = "^(https?:\\/\\/)?([\\w\\d\\-_]+\\.)+[\\w\\d\\-_]+(\\/.*)?$", message = "Invalid website URL format")
    private String website;

    @NotBlank(message = "Location cannot be blank and cant contain any whitespace")
    @Pattern(regexp = "^[\\S]+$", message = "location name must not contain any whitespace")
    private String location;

    @NotBlank(message = "Nature of business cannot be blank")
    @Pattern(regexp = "^[\\S]+$", message = "natureOfBusiness cant be blank and  can not contain any whitespace")
    private String natureOfBusiness;

    @NotBlank(message = "Nature of business cannot be blank")
    @Pattern(regexp = "^[\\S]+$", message = "manufacturingProcesses cant be blank and  can not contain any whitespace")
    private String manufacturingProcesses;
}
