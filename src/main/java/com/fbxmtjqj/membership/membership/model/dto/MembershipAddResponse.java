package com.fbxmtjqj.membership.membership.model.dto;

import com.fbxmtjqj.membership.membership.model.enums.MembershipType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MembershipAddResponse {

    private final Long id;
    private final MembershipType membershipType;

}
