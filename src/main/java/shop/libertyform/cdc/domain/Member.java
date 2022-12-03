package shop.libertyform.cdc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.libertyform.cdc.domain.type.MemberType;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
public class Member extends BaseEntity {
    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private MemberType member_type;

    // 연관 관계 편의 메서드
    public Member(String email, String password, String name){
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
