package lt.techin.springyne.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "GROUP_TABLE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Column(name="group_year")
    private String year;

    private int students;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="modified_date")
    private LocalDateTime modifiedDate;

    private boolean deleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "program_id")
    private Program program;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @PrePersist
    private void prePersist() {
        modifiedDate = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        modifiedDate = LocalDateTime.now();
    }

}