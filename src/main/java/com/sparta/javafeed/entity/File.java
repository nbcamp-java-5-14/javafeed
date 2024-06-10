package com.sparta.javafeed.entity;

import com.sparta.javafeed.dto.S3ResponseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class File extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "newsfeed_id", nullable = false)
    private Newsfeed newsfeed;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String saveName;

    @Column(nullable = false)
    private String url;

    @Column
    private Long size;

    public File(S3ResponseDto responseDto) {
        this.originName = responseDto.getOriginName();
        this.saveName = responseDto.getSaveFileName();
        this.url = responseDto.getUrl();
        this.size = responseDto.getSize();
    }

    public void addNewsfeed(Newsfeed newsfeed) {
        this.newsfeed = newsfeed;
    }
}
