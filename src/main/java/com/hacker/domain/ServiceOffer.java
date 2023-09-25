package com.hacker.domain;

import com.hacker.domain.enums.ServiceOfferType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "_service_offer")
public class ServiceOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_offer_id")
    @Setter(AccessLevel.NONE)
    private Integer id;

    private String systemCheck;

    private String offerA;

    private String offerB;

    private String offerC;

    private String offerD;

    private String offerE;

    @Enumerated(EnumType.STRING)
    private ServiceOfferType type;

    @ManyToOne
    @JoinColumn(name = "company_offer_id")
    private Company company;
}