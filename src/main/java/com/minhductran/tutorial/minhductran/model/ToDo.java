package com.minhductran.tutorial.minhductran.model;

import com.minhductran.tutorial.minhductran.utils.ToDoStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "to_dos")
public class ToDo extends AbstractEntity {

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

}
