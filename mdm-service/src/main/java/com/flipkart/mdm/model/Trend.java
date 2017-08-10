package com.flipkart.mdm.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by setu.poddar on 10/08/17.
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "TREND")
public class Trend implements GenericModel{

    @Id
    @GenericGenerator(name = "id", strategy = "com.flipkart.mdm.model.idgenerator.TrendIdGenerator")
    @GeneratedValue(generator = "id")
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME", nullable = false, unique = false)
    private String title;

    @Column(name = "VERTICAL", nullable = false, unique = false)
    private String vertical;

    @Column(name = "MUST_HAVE", nullable = false, unique = false)
    private String mustHave;

    @Column(name = "MUST_NOT_HAVE", nullable = false, unique = false)
    private String mustNotHave;

    @Column(name = "START_DATE", nullable = false, unique = false)
    private Date startDate;

    @Column(name = "END_DATE", nullable = false, unique = false)
    private Date endDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "TREND_IMAGES",
            joinColumns = {@JoinColumn(name = "TREND_ID")},
            inverseJoinColumns = {@JoinColumn(name = "IMAGE_ID")})
    private Set<Images> images = new HashSet();

    @OneToMany(mappedBy = "trend")
    private Set<Task> tasks = new HashSet();


}
