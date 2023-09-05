package com.bttf.queosk.controller;

import com.bttf.queosk.config.JwtTokenProvider;
import com.bttf.queosk.dto.*;
import com.bttf.queosk.service.ImageService;
import com.bttf.queosk.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@Api(tags = "Menu API", description = "메뉴 관련 API")
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final ImageService imageService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/menus")
    @ApiOperation(value = "식당 메뉴 목록 추가", notes = "점주가 본인 식당의 메뉴목록을 추가합니다.")
    public ResponseEntity<Void> createMenu(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Valid @RequestBody MenuCreationForm.Request menuCreationRequest) {

        Long restaurantId = jwtTokenProvider.getIdFromToken(token);

        menuService.createMenu(restaurantId, menuCreationRequest);

        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping("/{restaurantId}/menus")
    @ApiOperation(value = "식당 메뉴 목록 조회", notes = "점주 또는 고객이 대상 식당의 메뉴목록을 조회합니다.")
    public ResponseEntity<MenuListForm.Response> getMenu(
            @PathVariable Long restaurantId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        List<MenuDto> menuList = menuService.getMenu(restaurantId);

        return ResponseEntity.status(OK).body(MenuListForm.Response.of(menuList));
    }

    @PutMapping("/menus/{menuId}")
    @ApiOperation(value = "식당 메뉴정보 수정", notes = "점주가 본인 식당의 메뉴정보를 수정합니다.")
    public ResponseEntity<Void> updateMenuInfo(
            @PathVariable Long menuId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody MenuUpdateForm.Request menuUpdateRequest) {

        Long restaurantId = jwtTokenProvider.getIdFromToken(token);

        menuService.updateMenuInfo(restaurantId, menuId, menuUpdateRequest);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PutMapping("/menus/{menuId}/status")
    @ApiOperation(value = "식당 메뉴 주문가능여부 수정", notes = "점주가 본인 식당의 메뉴상태를 수정합니다.")
    public ResponseEntity<Void> updateMenuStatus(
            @PathVariable Long menuId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody MenuStatusForm.Request menuStatusRequest) {

        Long restaurantId = jwtTokenProvider.getIdFromToken(token);

        menuService.updateMenuStatus(restaurantId, menuId, menuStatusRequest);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PostMapping("/menus/image")
    @ApiOperation(value = "메뉴 이미지 등록", notes = "메뉴 이미지를 업로드하고 경로를 반환합니다.")
    public ResponseEntity<ImageUrlForm.Response> uploadImage(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody MultipartFile imageFile) throws IOException {

        Long restaurantId = jwtTokenProvider.getIdFromToken(token);

        String url = imageService.saveFile(
                imageFile,
                "/restaurant/" + restaurantId + "/menu/" + UUID.randomUUID().toString().substring(0, 7)
        );

        return ResponseEntity.status(OK).body(ImageUrlForm.Response.of(url));
    }

    @PostMapping("/menus/{menuId}/image")
    @ApiOperation(value = "메뉴 이미지 변경", notes = "메뉴 이미지를 변경합니다.")
    public ResponseEntity<Void> updateImage(
            @PathVariable Long menuId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody MultipartFile imageFile) throws IOException {

        Long restaurantId = jwtTokenProvider.getIdFromToken(token);

        String url = imageService.saveFile(
                imageFile,
                "/restaurant/" + restaurantId + "/menu/" + UUID.randomUUID().toString().substring(0, 7)
        );

        menuService.updateImage(restaurantId, menuId, url);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @DeleteMapping("/menus/{menuId}")
    @ApiOperation(value = "메뉴 삭제", notes = "점주 본인 매장의 메뉴를 삭제합니다.")
    public ResponseEntity<Void> deleteMenu(
            @PathVariable Long menuId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        Long restaurantId = jwtTokenProvider.getIdFromToken(token);

        menuService.deleteMenu(restaurantId, menuId);

        return ResponseEntity.status(NO_CONTENT).build();
    }
}
