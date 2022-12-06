package shop.libertyform.cdc.domain.mongo;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import shop.libertyform.cdc.domain.BaseEntity;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Document
public class MSurveyParticipant extends BaseEntity {
    private Long surveyId;

    private Long memberId;
}
