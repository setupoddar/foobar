package com.flipkart.mdm.model;

import com.flipkart.mdm.model.enums.TaskStatus;
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
@Table(name = "QC_STATUS")
public class QCFSN implements GenericModel {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "TREND_ID", nullable = false, unique = false)
    private Trend trend;

    @Column(name="FSNS", nullable = false, unique = false)
    private String fsns;

    @Column(name = "STATUS", nullable = false, unique = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name="FINAL_FSNS", nullable = true, unique = false)
    private String finalFsn;

}
