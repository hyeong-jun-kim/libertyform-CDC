package shop.libertyform.cdc.domain.mongo;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import shop.libertyform.cdc.domain.BaseEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Document
public class MQuestionType extends BaseEntity {
    private String name;

    private Boolean hasChoices;
}
