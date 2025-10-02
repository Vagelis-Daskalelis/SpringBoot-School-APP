package com.vaggelis.SpringSchool.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImageReadDTO {
    private Long id;
    private String fileName;
    private String fileType;
    private String downloadUrl; // the link to download
}