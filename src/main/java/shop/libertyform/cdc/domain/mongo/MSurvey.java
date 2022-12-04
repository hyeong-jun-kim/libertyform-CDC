package shop.libertyform.cdc.domain.mongo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import shop.libertyform.cdc.domain.BaseEntity;
import shop.libertyform.cdc.domain.Member;
import shop.libertyform.cdc.domain.Question;
import shop.libertyform.cdc.domain.Response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
@Getter
public class MSurvey extends BaseEntity {
    private long memberId;

    private String code;

    private String name;

    private String description;

    private String thumbnailImg;

    private LocalDate expirationDate;
}
