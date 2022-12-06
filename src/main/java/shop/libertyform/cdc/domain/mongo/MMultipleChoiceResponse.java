package shop.libertyform.cdc.domain.mongo;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import shop.libertyform.cdc.domain.BaseEntity;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Document
public class MMultipleChoiceResponse extends BaseEntity {
    private Long responseId;

    private Long questionId;

}
