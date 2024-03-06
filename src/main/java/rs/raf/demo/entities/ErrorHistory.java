package rs.raf.demo.entities;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(schema = "errors")
public class ErrorHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column
    @NonNull
    private String message;

    @Column
    private Long creationDate;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @NonNull
    private User user;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @NonNull
    private Vacuum vacuum;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @NonNull
    private Status status;

    public ErrorHistory() {
        this.creationDate = new Date().getTime();
    }

    public ErrorHistory(String message, User user, Vacuum vacuum, Status status) {
        this.message = message;
        this.user = user;
        this.vacuum = vacuum;
        this.status = status;
        this.creationDate = new Date().getTime();
    }
}
