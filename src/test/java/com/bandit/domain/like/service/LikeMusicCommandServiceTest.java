package com.bandit.domain.like.service;


import com.bandit.domain.board.dto.promotion.PromotionRequest;
import com.bandit.domain.board.repository.PromotionRepository;
import com.bandit.domain.board.service.promotion.PromotionCommandService;
import com.bandit.domain.board.service.promotion.PromotionQueryService;
import com.bandit.domain.like.entity.LikeMusic;
import com.bandit.domain.like.repository.LikeMusicRepository;
import com.bandit.domain.like.service.like_music.LikeMusicCommandService;
import com.bandit.domain.member.dto.MemberRequest.MemberRegisterDto;
import com.bandit.domain.member.entity.Member;
import com.bandit.domain.member.service.MemberCommandService;
import com.bandit.domain.member.service.MemberQueryService;
import com.bandit.domain.music.dto.MusicRequest;
import com.bandit.domain.music.entity.Music;
import com.bandit.domain.music.repository.MusicRepository;
import com.bandit.global.config.test.DatabaseCleanUp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class LikeMusicCommandServiceTest {
    //Service
    @Autowired
    LikeMusicCommandService likeMusicCommandService;
    @Autowired
    PromotionCommandService promotionCommandService;
    @Autowired
    PromotionQueryService promotionQueryService;
    @Autowired
    MemberCommandService memberCommandService;
    @Autowired
    MemberQueryService memberQueryService;
    @Autowired
    DatabaseCleanUp databaseCleanUp;
    //Repository
    @Autowired
    LikeMusicRepository likeMusicRepository;
    @Autowired
    PromotionRepository promotionRepository;
    @Autowired
    MusicRepository musicRepository;
    //Entity & dto & else
    Member hostMember;
    Member guestMember;
    Long promotionId;


    @BeforeEach
    void setup() {
        MemberRegisterDto hostDto = MemberRegisterDto.builder()
                .nickname("hostNickname")
                .profileImg("hostProfile")
                .kakaoEmail("hostKakaoEmail")
                .name("host")
                .build();
        MemberRegisterDto guestDto = MemberRegisterDto.builder()
                .nickname("guestNickname")
                .profileImg("guestProfile")
                .kakaoEmail("guestKakaoEmail")
                .name("guest")
                .build();
        Long hostId = memberCommandService.registerMember(hostDto);
        Long guestId = memberCommandService.registerMember(guestDto);

        hostMember = memberQueryService.getByMemberId(hostId);
        guestMember = memberQueryService.getByMemberId(guestId);

        List<MusicRequest> setList = new ArrayList<>();
        setList.add(MusicRequest.builder().title("title1").artist("artist1").isOpen(true).build());
        setList.add(MusicRequest.builder().title("title2").artist("artist2").isOpen(true).build());
        setList.add(MusicRequest.builder().title("title3").artist("artist3").isOpen(false).build());

        PromotionRequest request = PromotionRequest.builder()
                .maxAudience(3)
                .content("content")
                .team("team")
                .title("title")
                .musicList(setList)
                .build();
        promotionId = promotionCommandService.createPromotion(hostMember, request);
    }

    @AfterEach
    void cleanUp() {
        databaseCleanUp.truncateAllEntity();
    }

    @Test
    @Transactional
    @DisplayName("게스트가 셑리스트에 좋아요를 누릅니다.")
    void LikeSetList() {
        //given
        List<Music> musicList = musicRepository.findByPromotionId(promotionId);
        log.info("size = {}", musicList.size());
        //when
        Long likeId = likeMusicCommandService.likeMusic(musicList.get(0).getId(), guestMember);
        //then
        Optional<LikeMusic> optionalLike = likeMusicRepository.findById(likeId);
        assertThat(optionalLike).isPresent();
    }

    @Test
    @Transactional
    @DisplayName("게스트가 셑리스트에 좋아요를 누르고 취소합니다.")
    void testMethodName() {
        //given
        List<Music> musicList = musicRepository.findByPromotionId(promotionId);
        log.info("size = {}", musicList.size());
        musicList.forEach(music -> likeMusicCommandService.likeMusic(music.getId(),guestMember));
        //when
        likeMusicCommandService.unlikeMusic(musicList.get(0).getId(), guestMember);
        //then
        List<LikeMusic> all = likeMusicRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
    }

}