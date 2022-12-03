package shop.libertyform.cdc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class MultipleChoice extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "multipleChoiceResponseId")
    private MultipleChoiceResponse multipleChoiceResponse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "choiceId")
    private Choice choice;
}
