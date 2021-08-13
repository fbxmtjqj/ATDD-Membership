package com.mang.atdd.membership.app.membership.controller;

import com.mang.atdd.membership.app.common.DefaultRestController;
import com.mang.atdd.membership.app.enums.MembershipType;
import com.mang.atdd.membership.app.membership.dto.MembershipAddRequest;
import com.mang.atdd.membership.app.membership.dto.MembershipAddResponse;
import com.mang.atdd.membership.app.membership.dto.MembershipDetailResponse;
import com.mang.atdd.membership.app.membership.dto.MembershipPointRequest;
import com.mang.atdd.membership.app.membership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.mang.atdd.membership.app.membership.constants.MembershipConstants.USER_ID_HEADER;

@RestController
@RequiredArgsConstructor
public class MembershipController extends DefaultRestController {

    private final MembershipService membershipService;

    @PostMapping("/api/v1/membership")
    public ResponseEntity<MembershipAddResponse> addMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Valid final MembershipAddRequest membershipRequest) {

        final MembershipAddResponse membershipResponse = membershipService.addMembership(userId, membershipRequest.getMembershipType(), membershipRequest.getPoint());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(membershipResponse);
    }

    @GetMapping("/api/v1/membership/list")
    public ResponseEntity<List<MembershipDetailResponse>> getMembershipList(
            @RequestHeader(USER_ID_HEADER) final String userId) {

        return ResponseEntity.ok(membershipService.getMembershipList(userId));
    }

    @GetMapping("/api/v1/membership")
    public ResponseEntity<MembershipDetailResponse> getMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestParam final MembershipType membershipType) {

        return ResponseEntity.ok(membershipService.getMembership(userId, membershipType));
    }

    @DeleteMapping("/api/v1/membership/{id}")
    public ResponseEntity<Void> getMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long id) {

        membershipService.removeMembership(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/membership/{id}/accumulate")
    public ResponseEntity<Void> accumulateMembershipPoint(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long id,
            @RequestBody @Valid final MembershipPointRequest pointRequest) {

        membershipService.accumulateMembershipPoint(id, userId, pointRequest.getPoint());
        return ResponseEntity.noContent().build();
    }

}
