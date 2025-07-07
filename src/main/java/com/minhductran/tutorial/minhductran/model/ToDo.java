package com.minhductran.tutorial.minhductran.model;

import com.minhductran.tutorial.minhductran.utils.ToDoStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "to_dos")
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tu dong tang id khi tao moi entity
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
//    @JdbcTypeCode(SqlTypes.NAMED_ENUM) // Su dung để lưu trữ enum trong cơ sở dữ liệu
    private ToDoStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Khóa ngoại tham chiếu đến bảng users
    private User user;

    @Column(name = "created_at")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
