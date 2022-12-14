package shop.libertyform.cdc.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class QuestionType extends BaseEntity {
    private String name;

    private Boolean hasChoices;
}
