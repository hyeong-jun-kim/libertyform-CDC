package shop.libertyform.cdc.service.cdc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import shop.libertyform.cdc.domain.Choice;
import shop.libertyform.cdc.domain.MultipleChoice;
import shop.libertyform.cdc.domain.mongo.MChoice;
import shop.libertyform.cdc.domain.mongo.MMultipleChoice;
import shop.libertyform.cdc.repository.CommonRepository;
import shop.libertyform.cdc.repository.mongo.MCommonRepository;
import shop.libertyform.cdc.service.CrudCDC;
import shop.libertyform.cdc.service.KafkaProducer;

@Service
public class MultipleChoiceCDCService extends CrudCDC {

    public MultipleChoiceCDCService(CommonRepository<MultipleChoice> commonRepository, MCommonRepository<MMultipleChoice> mCommonRepository
    ,KafkaProducer kafkaProducer) {
        super(commonRepository, mCommonRepository, kafkaProducer);
    }

    @KafkaListener(topics = "source-db.liberty_form-api.multiple_choice", groupId = "foo")
    public void consume(@Payload(required = false) String data) throws JsonProcessingException, IllegalAccessException {
        if (data == null) // delete 했을 경우
            return;

        afterMap = getAfterMap(data);

        if(afterMap == null){ // 삭제했을 경우
            beforeMap = getBeforeMap(data);
            long id = Long.parseLong(beforeMap.get("id").toString());

            // CDC 동작 (REMOVE)
            MultipleChoice multipleChoice = new MultipleChoice();
            removeEntity(id, multipleChoice);
            return;
        }

        String op = afterMap.get("op").toString();

        Long choiceId = getLongValue("choice_id");
        Long multipleChoiceResponseId = getLongValue("multiple_choice_response_id");

        // MYSQL
        MultipleChoice multipleChoice = MultipleChoice.builder()
                .choiceId(choiceId)
                .multipleChoiceResponseId(multipleChoiceResponseId)
                .build();

        setBaseEntity(multipleChoice);

        // CDC 동작 (INSERT & UPDATE)
        insertOrCreateEntity(op, multipleChoice);

        // MONGO DB
        MMultipleChoice mMultipleChoice = MMultipleChoice.builder()
                .choiceId(choiceId)
                .multipleChoiceResponseId(multipleChoiceResponseId)
                .build();

        setBaseEntity(mMultipleChoice);

        // MongoDB CD (INSERT)
        mongoInsert(op, mMultipleChoice);

        // Kafka Topic (Druid)
        sendTopicMessage("libertyform.multiple_choice", multipleChoice);
    }
}
