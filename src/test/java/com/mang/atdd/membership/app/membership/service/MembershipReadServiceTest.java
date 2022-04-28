package com.mang.atdd.membership.app.membership.service;

import com.mang.atdd.membership.app.enums.MembershipType;
import com.mang.atdd.membership.app.membership.dto.MembershipDetailResponse;
import com.mang.atdd.membership.app.membership.entity.Membership;
import com.mang.atdd.membership.app.membership.repository.MembershipRepository;
import com.mang.atdd.membership.exception.MembershipErrorResult;
import com.mang.atdd.membership.exception.MembershipException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class MembershipReadServiceTest {

    @InjectMocks
    private MembershipReadService target;
    @Mock
    private MembershipRepository membershipRepository;

    private final String userId = "userId";
    private final Integer point = 10000;
    private final Long membershipId = -1L;

    @Test
    public void 멤버십상세조회실패_존재하지않음() {
        // given
        doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class, () -> target.getMembership(membershipId, userId));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    public void 멤버십상세조회실패_본인이아님() {
        // given
        doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class, () -> target.getMembership(membershipId, "notowner"));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    public void 멤버십상세조회성공() {
        // given
        doReturn(Optional.of(membership())).when(membershipRepository).findById(membershipId);

        // when
        final MembershipDetailResponse result = target.getMembership(membershipId, userId);

        // then
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(result.getPoint()).isEqualTo(point);
    }

    @Test
    public void 멤버십목록조회() {
        // given
        doReturn(Arrays.asList(
                Membership.builder().build(),
                Membership.builder().build(),
                Membership.builder().build()
        )).when(membershipRepository).findAllByUserId(userId);

        // when
        final List<MembershipDetailResponse> result = target.getMembershipList(userId);

        // then
        assertThat(result.size()).isEqualTo(3);
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
