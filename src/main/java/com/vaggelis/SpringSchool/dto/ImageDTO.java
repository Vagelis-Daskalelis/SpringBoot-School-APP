package com.vaggelis.SpringSchool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {
    Long id;
    private String fileName;
    private String downloadUrl;
}
