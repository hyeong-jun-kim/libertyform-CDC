package shop.libertyform.cdc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.libertyform.cdc.domain.status.ResponseStatus;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class SurveyManagement extends BaseEntity {
    private Long memberId;

    private Long contactId;

    private Long surveyId;

    private String code;

    private LocalDate expiredDate;

    @Enumerated(EnumType.STRING)
    private ResponseStatus responseStatus = ResponseStatus.PENDING;
}
