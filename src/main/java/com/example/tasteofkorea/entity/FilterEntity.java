package com.example.tasteofkorea.entity;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "filter")
@Getter
@Setter
public class FilterEntity {

    @Id
    private Long id;

    @Column(name = "fil_num", length = 100, nullable = true)
    private String filNum;

    // 예: 알레르기 항목들
    @Column(nullable = true)
    private Long Egg;

    @Column(nullable = true)
    private Long Milk;

    @Column(nullable = true)
    private Long Buckwheat;

    @Column(nullable = true)
    private Long Peanut;

    @Column(nullable = true)
    private Long Soybean;

    @Column(nullable = true)
    private Long Wheat;

    @Column(nullable = true)
    private Long Fish;

    @Column(nullable = true)
    private Long Crab;

    @Column(nullable = true)
    private Long Shrimp;

    @Column(nullable = true)
    private Long Pork;

    @Column(nullable = true)
    private Long Peach;

    @Column(nullable = true)
    private Long Tomato;

    @Column(nullable = true)
    private Long Sulfites;

    @Column(nullable = true)
    private Long Walnut;

    @Column(nullable = true)
    private Long Chicken;

    @Column(nullable = true)
    private Long Beef;

    @Column(nullable = true)
    private Long Squid;

    @Column(name = "Bivalves_and_abalone", nullable = true)
    private Long BivalvesAndAbalone;

    @Column(name = "Pine_nut", nullable = true)
    private Long PineNut;
}