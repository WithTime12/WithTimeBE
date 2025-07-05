package org.withtime.be.withtimebe.domain.weather.entity;

import jakarta.persistence.*;
import lombok.*;
import org.withtime.be.withtimebe.global.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "keyword")
public class Keyword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Long id;

    @Column(name = "name")
    private String name;
}
