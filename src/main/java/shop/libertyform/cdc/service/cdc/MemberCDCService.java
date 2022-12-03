package shop.libertyform.cdc.service.cdc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import shop.libertyform.cdc.domain.Member;
import shop.libertyform.cdc.domain.status.BaseStatus;
import shop.libertyform.cdc.domain.type.MemberType;
import shop.libertyform.cdc.repository.CommonRepository;
import shop.libertyform.cdc.service.CrudCDC;

import java.time.LocalDateTime;

@Service
public class MemberCDCService extends CrudCDC {

    public MemberCDCService(CommonRepository<Member> commonRepository) {
        super(commonRepository);
    }

    @KafkaListener(topics = "source-db.liberty_form-api.member", groupId = "foo")
    public void consume(@Payload(required = false) String data) throws JsonProcessingException {
        if (data == null) // delete 했을 경우
            return;

        afterMap = getAfterMap(data);

        if(afterMap == null){ // 삭제했을 경우
            beforeMap = getBeforeMap(data);
            long id = Long.parseLong(beforeMap.get("id").toString());

            // CDC 동작 (REMOVE)
            Member member = new Member();
            removeEntity(id, member);
            return;
        }

        String op = afterMap.get("op").toString();


        String email = getStringValue("email");
        String password = getStringValue("password");
        MemberType member_type = getMemberTypeValue("member_type");
        String name = getStringValue("name");

        Member member = Member.builder()
                .email(email)
                .password(password)
                .member_type(member_type)
                .name(name)
                .build();

        setBaseEntity(member);

        // CDC 동작 (INSERT & UPDATE)
        insertOrCreateEntity(op, member);
    }

    private MemberType getMemberTypeValue(String key){
        Object value = afterMap.get(key);

        if(value == null){
            return null;
        }else{
            return MemberType.valueOf(value.toString());
        }
    }
}
