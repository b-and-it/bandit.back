package com.bandit.domain.like.entity;

import com.bandit.domain.member.entity.Member;
import com.bandit.domain.music.entity.PromotionMusic;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Table(name = "like_music")
public class LikeMusic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_music_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_music_id")
    private PromotionMusic promotionMusic;          //연관관계 depth를 낮추기 위함

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static LikeMusic of(PromotionMusic promotionMusic, Member member) {
        return LikeMusic.builder()
                .member(member)
                .promotionMusic(promotionMusic)
                .build();
    }
}
