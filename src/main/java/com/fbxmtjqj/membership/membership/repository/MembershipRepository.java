package com.fbxmtjqj.membership.membership.repository;

import com.fbxmtjqj.membership.membership.model.entity.Membership;
import com.fbxmtjqj.membership.membership.model.enums.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Membership findByUserIdAndMembershipType(final String userId, final MembershipType membershipType);

    List<Membership> findAllByUserId(final String userId);

}
