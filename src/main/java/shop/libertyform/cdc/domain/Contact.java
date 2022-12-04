package shop.libertyform.cdc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
public class Contact extends BaseEntity {
    private long memberId;

    private String email;

    private String name;

    private String relationship;

    // 편의 메서드
    public Contact(String email, String name, String relationship){
        this.email = email;
        this.name = name;
        this.relationship = relationship;
    }
}
