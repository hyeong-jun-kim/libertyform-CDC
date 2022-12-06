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
@Document
@Builder
@Getter
public class MQuestion extends BaseEntity {
    private Long surveyId;

    private Long questionTypeId;

    private Integer number;

    private String name;

    private String description;

    private String questionImgUrl;

    private Boolean answerRequired;

}
