package org.withtime.be.withtimebe.domain.date.entity;

import jakarta.persistence.*;
import lombok.*;

import org.withtime.be.withtimebe.domain.dateplace.entity.DatePlace;
import org.withtime.be.withtimebe.global.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "item")
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_place_id", nullable = false)
    private DatePlace datePlace;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price")
    private String price;

    @Column(name = "image", length = 500)
    private String image;
}
