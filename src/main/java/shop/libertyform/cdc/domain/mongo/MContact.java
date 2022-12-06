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
@Builder
@Document
@Getter
public class MContact extends BaseEntity {
    private Long memberId;

    private String email;

    private String name;

    private String relationship;

    // 편의 메서드
    public MContact(String email, String name, String relationship){
        this.email = email;
        this.name = name;
        this.relationship = relationship;
    }
}
