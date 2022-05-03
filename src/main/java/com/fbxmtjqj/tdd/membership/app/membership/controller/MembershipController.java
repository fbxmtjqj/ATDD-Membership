package com.fbxmtjqj.tdd.membership.app.membership.controller;

import com.fbxmtjqj.tdd.membership.app.membership.dto.MembershipAddResponse;
import com.fbxmtjqj.tdd.membership.app.membership.dto.MembershipDetailResponse;
import com.fbxmtjqj.tdd.membership.app.membership.dto.MembershipRequest;
import com.fbxmtjqj.tdd.membership.app.membership.service.MembershipReadService;
import com.fbxmtjqj.tdd.membership.app.membership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fbxmtjqj.tdd.membership.app.membership.constants.MembershipConstants.USER_ID_HEADER;
import static com.fbxmtjqj.tdd.membership.app.membership.validation.ValidationGroups.MembershipAccumulateMarker;
import static com.fbxmtjqj.tdd.membership.app.membership.validation.ValidationGroups.MembershipAddMarker;

@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;
    private final MembershipReadService membershipReadService;

    @PostMapping("/api/v1/memberships")
    public ResponseEntity<MembershipAddResponse> addMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Validated(MembershipAddMarker.class) final MembershipRequest membershipRequest) {

        final MembershipAddResponse membershipResponse = membershipService.addMembership(userId, membershipRequest.getMembershipType(), membershipRequest.getPoint());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(membershipResponse);
    }

    @GetMapping("/api/v1/memberships")
    public ResponseEntity<List<MembershipDetailResponse>> getMembershipList(
            @RequestHeader(USER_ID_HEADER) final String userId) {

        return ResponseEntity.ok(membershipReadService.getMembershipList(userId));
    }

    @GetMapping("/api/v1/memberships/{id}")
    public ResponseEntity<MembershipDetailResponse> getMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long id) {

        return ResponseEntity.ok(membershipReadService.getMembership(id, userId));
    }

    @DeleteMapping("/api/v1/memberships/{id}")
    public ResponseEntity<Void> removeMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long id) {

        membershipService.removeMembership(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/memberships/{id}/accumulate")
    public ResponseEntity<Void> accumulateMembershipPoint(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long id,
            @RequestBody @Validated(MembershipAccumulateMarker.class) final MembershipRequest membershipRequest) {

        membershipService.accumulateMembershipPoint(id, userId, membershipRequest.getPoint());
        return ResponseEntity.noContent().build();
    }

}
