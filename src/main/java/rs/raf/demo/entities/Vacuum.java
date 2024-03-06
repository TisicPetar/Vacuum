package rs.raf.demo.entities;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(schema = "vacuums")
public class Vacuum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NonNull
    private String name;

    @ManyToOne
    @NonNull
    private Status status;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @NonNull
    private User addedByUser;

    @Column
    @ColumnDefault("true")
    private boolean active = true;

    @Column
    private Long creationDate;

    @Column
    @ColumnDefault("0")
    private int count = 0;

    public Vacuum() {
        this.creationDate = new Date().getTime();
    }

    public Vacuum(String name, Status status, User addedByUser) {
        this.name = name;
        this.status = status;
        this.addedByUser = addedByUser;
        this.creationDate = new Date().getTime();
    }
}
