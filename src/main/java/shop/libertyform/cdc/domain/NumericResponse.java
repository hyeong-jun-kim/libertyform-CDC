package shop.libertyform.cdc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.libertyform.cdc.domain.type.NumericType;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class NumericResponse extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responseId")
    private Response response;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Question question;

    @Enumerated(EnumType.STRING)
    private NumericType numericType;

    private int value;
}
