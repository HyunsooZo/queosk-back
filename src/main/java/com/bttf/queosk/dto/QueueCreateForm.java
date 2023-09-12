package com.bttf.queosk.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


public class QueueCreateForm {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel(value = "웨이팅 등록 Request")
    public static class Request {
        @Min(value = 1, message = "식사인원은 1명 이상이어야 합니다.")
        @Max(value = 100, message = "식사인원은 6명 이하이어야 합니다.")
        private Long numberOfParty;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel(value = "웨이팅 등록 Response")
    public static class Response {
        private Long userQueueIndex;
        private Long queueRemaining;

        public static Response of(QueueIndexDto queueIndexDto) {
            return Response.builder()
                    .queueRemaining(queueIndexDto.getQueueRemaining())
                    .userQueueIndex(queueIndexDto.getUserQueueIndex())
                    .build();
        }
    }
}

