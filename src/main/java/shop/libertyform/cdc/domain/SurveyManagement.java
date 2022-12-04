package shop.libertyform.cdc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.libertyform.cdc.domain.status.ResponseStatus;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class SurveyManagement extends BaseEntity {
    private long memberId;

    private long contactId;

    private long surveyId;

    private String code;

    private LocalDate expiredDate;

    @Enumerated(EnumType.STRING)
    private ResponseStatus responseStatus = ResponseStatus.PENDING;
}
