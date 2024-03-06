package rs.raf.demo.entities;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(schema = "status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column
    @NonNull
    private String status;

    public Status() {
    }

    public Status(String status) {
        this.status = status;
    }
}
