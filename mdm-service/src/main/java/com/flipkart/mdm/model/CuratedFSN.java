package com.flipkart.mdm.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by setu.poddar on 11/08/17.
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "CURATED_RESULT")
public class CuratedFSN implements GenericModel {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @OneToOne
    @JoinColumn(name = "TASK_ID", nullable = false, unique = false)
    private Task taskId;

    @ManyToOne
    @JoinColumn(name = "TREND_ID", nullable = false, unique = false)
    private Trend trend;

    @OneToOne()
    @JoinColumn(name = "USER_ID", nullable = false, unique = false)
    private User user;

    @Column(name="FSNS", nullable = false, unique = false)
    private String fsns;
}
