package com.bandit.domain.board.converter;

import com.bandit.domain.board.dto.promotion.PromotionResponse.*;
import com.bandit.domain.board.entity.Promotion;
import com.bandit.domain.member.converter.MemberConverter;
import com.bandit.domain.music.converter.MusicConverter;
import com.bandit.domain.ticket.converter.GuestConverter;
import com.bandit.domain.ticket.entity.Guest;
import com.bandit.global.util.ImageUtil;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class PromotionConverter {
    public static PromotionDetailDto toDetailDto(Promotion promotion) {
        return PromotionDetailDto.builder()
                .promotionId(promotion.getId())
                .title(promotion.getTitle())
                .team(promotion.getTeam())
                .content(promotion.getContent())
                .maxAudience(promotion.getMaxAudience())
                .date(promotion.getShowDate())
                .entranceFee(promotion.getEntranceFee())
                .location(promotion.getShowLocation())
                .account(promotion.getAccount())
                .accountHolder(promotion.getAccountHolder())
                .startTime(promotion.getStartTime())
                .endTime(promotion.getEndTime())
                .bankName(promotion.getBankName())
                .refundInfo(promotion.getRefundInfo())
                .writer(MemberConverter.toResponse(promotion.getWriter()))
                .musicList(promotion.getMusicList().stream()
                        .map(MusicConverter::toViewDto).collect(Collectors.toList()))
                .imageList(promotion.getBoardImageList().stream()
                        .map(ImageUtil::appendUri).collect(Collectors.toList()))
                .build();
    }

    public static PromotionSummaryDto toSummaryDto(Promotion promotion) {
        return PromotionSummaryDto.builder()
                .promotionId(promotion.getId())
                .team(promotion.getTeam())
                .title(promotion.getTitle())
                .date(promotion.getShowDate())
                .startTime(promotion.getStartTime())
                .endTime(promotion.getEndTime())
                .entranceFee(promotion.getEntranceFee())
                .location(promotion.getShowLocation())
                .writer(MemberConverter.toResponse(promotion.getWriter()))
                .thumbnail(ImageUtil.appendUri(promotion.getBoardImageList()))
                .build();
    }

    public static PromotionListDto toListDto(Page<Promotion> paginationPromotion) {
        return PromotionListDto.builder()
                .promotionList(paginationPromotion.getContent().stream()
                        .map(PromotionConverter::toSummaryDto)
                        .collect(Collectors.toList()))

                .build();
    }

    public static GuestPromotionSummaryDto toGuestPromotionSummaryDto(Guest guest) {
        return GuestPromotionSummaryDto.builder()
                .promotion(toSummaryDto(guest.getPromotion()))
                .guest(GuestConverter.toViewDto(guest))
                .build();
    }
    public static GuestPromotionListDto toGuestPromotionListDto(Page<Guest> paginationGuest) {
        return GuestPromotionListDto.builder()
                .promotionList(paginationGuest.getContent().stream()
                        .map(PromotionConverter::toGuestPromotionSummaryDto)
                        .collect(Collectors.toList()))
                .totalCount(paginationGuest.getTotalElements())
                .build();
    }

    public static void setPromotionLikeInDto(PromotionSummaryDto dto, long count, boolean isLiked) {
        dto.getBoardLikeInfo().setCount(count);
        dto.getBoardLikeInfo().setLiked(isLiked);
    }
    public static void setPromotionLikeInDto(PromotionDetailDto dto, long count, boolean isLiked) {
        dto.getBoardLikeInfo().setCount(count);
        dto.getBoardLikeInfo().setLiked(isLiked);
    }
}
