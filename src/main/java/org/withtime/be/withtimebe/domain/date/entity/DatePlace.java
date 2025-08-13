package org.withtime.be.withtimebe.domain.date.entity;

import jakarta.persistence.*;
import lombok.*;

import org.withtime.be.withtimebe.domain.date.entity.enums.PlaceType;
import org.withtime.be.withtimebe.global.common.BaseEntity;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "date_place")
public class DatePlace extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_place_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image", length = 500)
    private String image;

    @Column(name = "tel")
    private String tel;

    @Column(name = "average_price")
    private Integer averagePrice;

    @Column(name = "information", columnDefinition = "TEXT")
    private String information;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "road_name_address")
    private String roadNameAddress;

    @Column(name = "lot_number_address")
    private String lotNumberAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "place_type")
    private PlaceType placeType;

    @OneToMany(mappedBy = "datePlace", cascade = CascadeType.ALL)
    private List<DatePlacePlaceCategory> placeCategories;
}
