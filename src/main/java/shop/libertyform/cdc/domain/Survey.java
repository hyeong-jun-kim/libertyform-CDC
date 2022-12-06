package shop.libertyform.cdc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
public class Survey extends BaseEntity {
    private Long memberId;

    private String code;

    private String name;

    private String description;

    private String thumbnailImg;

    private LocalDate expirationDate;
}
