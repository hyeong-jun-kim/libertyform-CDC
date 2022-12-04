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
    private long responseId;

    private long questionId;

    private NumericType numericType;

    private int value;
}
