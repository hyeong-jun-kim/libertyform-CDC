package shop.libertyform.cdc.service.cdc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import shop.libertyform.cdc.domain.Choice;
import shop.libertyform.cdc.domain.Contact;
import shop.libertyform.cdc.domain.MemberContact;
import shop.libertyform.cdc.domain.mongo.MContact;
import shop.libertyform.cdc.domain.mongo.MMemberContact;
import shop.libertyform.cdc.repository.CommonRepository;
import shop.libertyform.cdc.repository.mongo.MCommonRepository;
import shop.libertyform.cdc.service.CrudCDC;

@Service
public class MemberContactCDCService extends CrudCDC {

    public MemberContactCDCService(CommonRepository<MemberContact> commonRepository, MCommonRepository<MMemberContact> mCommonRepository) {
        super(commonRepository, mCommonRepository);
    }

    @KafkaListener(topics = "source-db.liberty_form-api.member_contact", groupId = "foo")
    public void consume(@Payload(required = false) String data) throws JsonProcessingException {
        if (data == null) // delete 했을 경우
            return;

        afterMap = getAfterMap(data);

        if(afterMap == null){ // 삭제했을 경우
            beforeMap = getBeforeMap(data);
            long id = Long.parseLong(beforeMap.get("id").toString());

            // CDC 동작 (REMOVE)
            MemberContact memberContact = new MemberContact();
            removeEntity(id, memberContact);
            return;
        }

        String op = afterMap.get("op").toString();

        // 필드 값 가져오기
        Long memberId = getLongValue("member_id");
        Long contactId = getLongValue("contact_id");

        // MYSQL
        MemberContact memberContact = MemberContact.builder()
                .memberId(memberId)
                .contactId(contactId)
                .build();

        setBaseEntity(memberContact);

        // CDC 동작 (INSERT & UPDATE)
        insertOrCreateEntity(op, memberContact);

        // MONGO DB
        MMemberContact mMemberContact = MMemberContact.builder()
                .memberId(memberId)
                .contactId(contactId)
                .build();

        setBaseEntity(mMemberContact);

        // MongoDB CD (INSERT)
        mongoInsert(op, mMemberContact);
    }
}
