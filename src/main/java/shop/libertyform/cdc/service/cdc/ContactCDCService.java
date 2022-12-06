package shop.libertyform.cdc.service.cdc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import shop.libertyform.cdc.domain.Choice;
import shop.libertyform.cdc.domain.Contact;
import shop.libertyform.cdc.domain.mongo.MChoice;
import shop.libertyform.cdc.domain.mongo.MContact;
import shop.libertyform.cdc.repository.CommonRepository;
import shop.libertyform.cdc.repository.mongo.MCommonRepository;
import shop.libertyform.cdc.service.CrudCDC;

@Service
public class ContactCDCService extends CrudCDC {

    public ContactCDCService(CommonRepository<Contact> commonRepository, MCommonRepository<MContact> mCommonRepository) {
        super(commonRepository, mCommonRepository);
    }

    @KafkaListener(topics = "source-db.liberty_form-api.contact", groupId = "foo")
    public void consume(@Payload(required = false) String data) throws JsonProcessingException {
        if (data == null) // delete 했을 경우
            return;

        afterMap = getAfterMap(data);

        if(afterMap == null){ // 삭제했을 경우
            beforeMap = getBeforeMap(data);
            long id = Long.parseLong(beforeMap.get("id").toString());

            // CDC 동작 (REMOVE)
            Contact contact = new Contact();
            removeEntity(id, contact);
            return;
        }

        String op = afterMap.get("op").toString();

        // 필드 값 가져오기
        Long memberId = getLongValue("member_id");

        String name = getStringValue("name");
        String email = getStringValue("email");
        String relationship = getStringValue("relationship");

        // MYSQL
        Contact contact = Contact.builder()
                .memberId(memberId)
                .email(email)
                .name(name)
                .relationship(relationship)
                .build();

        setBaseEntity(contact);

        // CDC 동작 (INSERT & UPDATE)
        insertOrCreateEntity(op, contact);

        // MONGO DB
        MContact mContact = MContact.builder()
                .memberId(memberId)
                .email(email)
                .name(name)
                .relationship(relationship)
                .build();

        setBaseEntity(mContact);

        // MongoDB CD (INSERT)
        mongoInsert(op, mContact);
    }
}
