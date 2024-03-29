package lt.techin.springyne.program;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @OneToMany(fetch = FetchType.EAGER,  cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "program_id")
    @OrderBy
    private Set<ProgramSubject> subjects = new HashSet<>();

    private boolean deleted = Boolean.FALSE;

    @LastModifiedDate
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "Europe/Vilnius")
    private LocalDateTime modifiedDate;


    @PrePersist
    private void prePersist() {
        modifiedDate = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        modifiedDate = LocalDateTime.now();
    }
}
