package com.fbxmtjqj.membership.membership.service;

import com.fbxmtjqj.membership.common.exception.ErrorCode;
import com.fbxmtjqj.membership.common.exception.ServerException;
import com.fbxmtjqj.membership.membership.model.dto.MembershipAddResponse;
import com.fbxmtjqj.membership.membership.model.entity.Membership;
import com.fbxmtjqj.membership.membership.model.enums.MembershipType;
import com.fbxmtjqj.membership.membership.repository.MembershipRepository;
import com.fbxmtjqj.membership.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MembershipService {

    private final PointService ratePointService;
    private final MembershipRepository membershipRepository;

    public MembershipAddResponse addMembership(final String userId, final MembershipType membershipType, final Integer point) {
        final Membership result = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);
        if (result != null) {
            throw new ServerException(ErrorCode.DUPLICATED_MEMBERSHIP_REGISTER);
        }

        final Membership membership = Membership.builder()
                .userId(userId)
                .point(point)
                .membershipType(membershipType)
                .build();

        final Membership savedMembership = membershipRepository.save(membership);

        return MembershipAddResponse.builder()
                .id(savedMembership.getId())
                .membershipType(savedMembership.getMembershipType())
                .build();
    }

    public void removeMembership(final Long membershipId, final String userId) {
        final Optional<Membership> optionalMembership = membershipRepository.findById(membershipId);
        final Membership membership = optionalMembership.orElseThrow(() -> new ServerException(ErrorCode.MEMBERSHIP_NOT_FOUND));
        if (!booleanEqualUserId(membership, userId)) {
            throw new ServerException(ErrorCode.NOT_MEMBERSHIP_OWNER);
        }

        membershipRepository.deleteById(membershipId);
    }

    public void accumulateMembershipPoint(final Long membershipId, final String userId, final int amount) {
        final Optional<Membership> optionalMembership = membershipRepository.findById(membershipId);
        final Membership membership = optionalMembership.orElseThrow(() -> new ServerException(ErrorCode.MEMBERSHIP_NOT_FOUND));
        if (!booleanEqualUserId(membership, userId)) {
            throw new ServerException(ErrorCode.NOT_MEMBERSHIP_OWNER);
        }

        final int additionalAmount = ratePointService.calculateAmount(amount);

        membership.setPoint(additionalAmount + membership.getPoint());
    }

    private boolean booleanEqualUserId(Membership membership, String userId) {
        return membership.getUserId().equals(userId);
    }
}
