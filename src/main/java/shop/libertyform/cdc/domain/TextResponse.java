package shop.libertyform.cdc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.libertyform.cdc.domain.type.TextType;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class TextResponse extends BaseEntity {
    private long responseId;

    private long questionId;

    @Enumerated(EnumType.STRING)
    private TextType textType;

    private String value;
}
