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
import shop.libertyform.cdc.domain.status.ResponseStatus;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Document
public class MSurveyManagement extends BaseEntity {
    private Long memberId;

    private Long contactId;

    private Long surveyId;

    private String code;

    private LocalDate expiredDate;

    @Enumerated(EnumType.STRING)
    private ResponseStatus responseStatus = ResponseStatus.PENDING;
}
