package shop.libertyform.cdc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
public class Question extends BaseEntity {
    private Long surveyId;

    private Long questionTypeId;

    private Integer number;

    private String name;

    private String description;

    private String questionImgUrl;

    private Boolean answerRequired;
}
