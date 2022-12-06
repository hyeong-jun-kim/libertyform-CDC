package shop.libertyform.cdc.domain.mongo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import shop.libertyform.cdc.domain.BaseEntity;
import shop.libertyform.cdc.domain.type.TextType;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Document
public class MTextResponse extends BaseEntity {
    private Long responseId;

    private Long questionId;

    @Enumerated(EnumType.STRING)
    private TextType textType;

    private String value;
}
