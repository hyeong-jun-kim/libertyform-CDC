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
    private Long responseId;

    private Long questionId;

    @Enumerated(EnumType.STRING)
    private TextType textType;

    private String value;
}
