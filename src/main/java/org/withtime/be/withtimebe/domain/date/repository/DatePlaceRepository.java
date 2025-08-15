package org.withtime.be.withtimebe.domain.date.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.withtime.be.withtimebe.domain.date.entity.DatePlace;

import java.util.List;

public interface DatePlaceRepository extends JpaRepository<DatePlace, Long> {


    @Query("select d from DatePlace d" +
            " where d.lotNumberAddress like %:keyword1%" +
            " and d.lotNumberAddress like %:keyword2%" +
            " and d.lotNumberAddress like %:keyword3%")
    List<DatePlace> findByAddressContainingAll(
            @Param("keyword1") String keyword1,
            @Param("keyword2") String keyword2,
            @Param("keyword3") String keyword3
    );
}
