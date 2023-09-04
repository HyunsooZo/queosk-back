package com.bttf.queosk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

public class UpdateReviewForm {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @NotBlank
        private String subject;
        private String content;
        @DecimalMin(value= "0.0")
        @DecimalMax(value= "5.0")
        private Double rate;
    }

}
