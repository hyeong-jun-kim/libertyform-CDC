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
import shop.libertyform.cdc.domain.type.MemberType;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Document(collation = "m_member")
public class MMember extends BaseEntity {
    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private MemberType member_type;

    // 연관 관계 편의 메서드
    public MMember(String email, String password, String name){
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
