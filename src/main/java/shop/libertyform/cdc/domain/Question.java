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
    private long surveyId;

    private long questionTypeId;

    private int number;

    private String name;

    private String description;

    private String questionImgUrl;

    private boolean answerRequired;

}
