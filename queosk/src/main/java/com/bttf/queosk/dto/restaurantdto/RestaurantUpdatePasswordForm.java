package com.bttf.queosk.dto.restaurantdto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestaurantUpdatePasswordForm {
    private String oldPassword;
    private String newPassword;
}
