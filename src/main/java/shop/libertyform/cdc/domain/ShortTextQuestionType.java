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
public class ShortTextQuestionType extends BaseEntity {
    private Long questionId;

    private Integer lengthLimit;

    private String placeHolder;
}
