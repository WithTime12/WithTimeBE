package org.withtime.be.withtimebe.domain.date.entity;

import jakarta.persistence.*;
import lombok.*;
import org.withtime.be.withtimebe.domain.date.entity.enums.PlaceCategoryType;
import org.withtime.be.withtimebe.global.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "place_category")
public class PlaceCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_category_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", nullable = false)
    private PlaceCategoryType categoryType;
}
