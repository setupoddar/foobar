package com.flipkart.mdm.model;

import com.flipkart.mdm.model.enums.TaskStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by setu.poddar on 10/08/17.
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "TASK")
public class Task implements GenericModel{

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Column(name = "STATUS", nullable = false, unique = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false, unique = false)
    private User user;

    @Column(name = "TREND_ID", nullable = false, unique = false)
    private String trendId;
}
