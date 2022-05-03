package com.fbxmtjqj.membership.membership.service;

import com.fbxmtjqj.membership.common.exception.ErrorCode;
import com.fbxmtjqj.membership.common.exception.ServerException;
import com.fbxmtjqj.membership.membership.model.dto.MembershipAddResponse;
import com.fbxmtjqj.membership.membership.model.entity.Membership;
import com.fbxmtjqj.membership.membership.model.enums.MembershipType;
import com.fbxmtjqj.membership.membership.repository.MembershipRepository;
import com.fbxmtjqj.membership.point.service.RatePointService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {

    @InjectMocks
    private MembershipService target;
    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private RatePointService ratePointService;

    private final String userId = "userId";
    private final MembershipType membershipType = MembershipType.NAVER;
    private final Integer point = 10000;
    private final Long membershipId = -1L;

    @Test
    public void 멤버십적립실패_존재하지않음() {
        // given
        doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);

        // when
        final ServerException result = assertThrows(ServerException.class, () -> target.accumulateMembershipPoint(membershipId, userId, 10000));

        // then
        assertThat(result.getErrorResult()).isEqualTo(ErrorCode.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    public void 멤버십적립실패_본인이아님() {
        // given
        final Membership membership = membership();
        doReturn(Optional.of(membership)).when(membershipRepository).findById(membershipId);

        // when
        final ServerException result = assertThrows(ServerException.class, () -> target.accumulateMembershipPoint(membershipId, "notowner", 10000));

        // then
        assertThat(result.getErrorResult()).isEqualTo(ErrorCode.NOT_MEMBERSHIP_OWNER);
    }

    @Test
    public void 멤버십적립성공() {
        // given
        final Membership membership = membership();
        doReturn(Optional.of(membership)).when(membershipRepository).findById(membershipId);

        // when
        target.accumulateMembershipPoint(membershipId, userId, 10000);

        // then
    }

    @Test
    public void 멤버십삭제실패_존재하지않음() {
        // given
        doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);

        // when
        final ServerException result = assertThrows(ServerException.class, () -> target.removeMembership(membershipId, userId));

        // then
        assertThat(result.getErrorResult()).isEqualTo(ErrorCode.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    public void 멤버십삭제실패_본인이아님() {
        // given
        final Membership membership = membership();
        doReturn(Optional.of(membership)).when(membershipRepository).findById(membershipId);

        // when
        final ServerException result = assertThrows(ServerException.class, () -> target.removeMembership(membershipId, "notowner"));

        // then
        assertThat(result.getErrorResult()).isEqualTo(ErrorCode.NOT_MEMBERSHIP_OWNER);
    }

    @Test
    public void 멤버십삭제성공() {
        // given
        final Membership membership = membership();
        doReturn(Optional.of(membership)).when(membershipRepository).findById(membershipId);

        // when
        target.removeMembership(membershipId, userId);

        // then
    }

    @Test
    public void 멤버십등록실패_이미존재함() {
        // given
        doReturn(Membership.builder().build()).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);

        // when
        final ServerException result = assertThrows(ServerException.class, () -> target.addMembership(userId, membershipType, point));

        // then
        assertThat(result.getErrorResult()).isEqualTo(ErrorCode.DUPLICATED_MEMBERSHIP_REGISTER);
    }

    @Test
    public void 멤버십등록성공() {
        // given
        doReturn(null).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);
        doReturn(membership()).when(membershipRepository).save(any(Membership.class));

        // when
        final MembershipAddResponse result = target.addMembership(userId, membershipType, point);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);

        // verify
        verify(membershipRepository, times(1)).findByUserIdAndMembershipType(userId, membershipType);
        verify(membershipRepository, times(1)).save(any(Membership.class));
    }

    private Membership membership() {
        return Membership.builder()
                .id(-1L)
                .userId(userId)
                .point(point)
                .membershipType(MembershipType.NAVER)
                .build();
    }

}
