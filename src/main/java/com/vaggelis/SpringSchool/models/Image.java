package com.vaggelis.SpringSchool.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;


    //@Lob
    //private Blob image;
    @Lob
    private byte[] image;
    private String downloadUrl;

    public Image(Long id, String fileName, String fileType) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
    }
}
