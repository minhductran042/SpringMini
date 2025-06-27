package com.minhductran.tutorial.minhductran.model;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "completed")
    private boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Khóa ngoại tham chiếu đến bảng users
    private User user;

    @Override
    public String toString() {
        return "Todo{" + 
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                '}';
    }
}
