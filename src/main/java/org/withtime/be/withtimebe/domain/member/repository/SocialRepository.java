package org.withtime.be.withtimebe.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.member.entity.Social;
import org.withtime.be.withtimebe.domain.member.entity.enums.SocialType;

import java.util.Optional;

public interface SocialRepository extends JpaRepository<Social, Long> {
    Optional<Social> findByProviderIdAndSocialType(String providerId, SocialType socialType);
}
