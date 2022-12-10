package shop.libertyform.cdc.service.cdc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import shop.libertyform.cdc.domain.Choice;
import shop.libertyform.cdc.domain.SingleChoiceResponse;
import shop.libertyform.cdc.domain.mongo.MChoice;
import shop.libertyform.cdc.domain.mongo.MSingleChoiceResponse;
import shop.libertyform.cdc.repository.CommonRepository;
import shop.libertyform.cdc.repository.mongo.MCommonRepository;
import shop.libertyform.cdc.service.CrudCDC;
import shop.libertyform.cdc.service.KafkaProducer;

@Service
public class SingleChoiceResponseCDCService extends CrudCDC {

    public SingleChoiceResponseCDCService(CommonRepository<SingleChoiceResponse> commonRepository, MCommonRepository<MSingleChoiceResponse> mCommonRepository
    , KafkaProducer kafkaProducer) {
        super(commonRepository, mCommonRepository, kafkaProducer);
    }

    @KafkaListener(topics = "source-db.liberty_form-api.single_choice_response", groupId = "foo")
    public void consume(@Payload(required = false) String data) throws JsonProcessingException, IllegalAccessException {
        if (data == null) // delete 했을 경우
            return;

        afterMap = getAfterMap(data);

        if(afterMap == null){ // 삭제했을 경우
            beforeMap = getBeforeMap(data);
            long id = Long.parseLong(beforeMap.get("id").toString());

            // CDC 동작 (REMOVE)
            MSingleChoiceResponse mSingleChoiceResponse = new MSingleChoiceResponse();
            removeEntity(id, mSingleChoiceResponse);
            return;
        }

        String op = afterMap.get("op").toString();

        Long questionId = getLongValue("question_id");
        Long choiceId = getLongValue("choice_id");
        Long responseId = getLongValue("response_id");

        // MYSQL
        SingleChoiceResponse singleChoiceResponse = SingleChoiceResponse.builder()
                .questionId(questionId)
                .choiceId(choiceId)
                .responseId(responseId)
                .build();

        setBaseEntity(singleChoiceResponse);

        // CDC 동작 (INSERT & UPDATE)
        insertOrCreateEntity(op, singleChoiceResponse);

        // MONGO DB
        MSingleChoiceResponse mSingleChoiceResponse = MSingleChoiceResponse.builder()
                .questionId(questionId)
                .choiceId(choiceId)
                .responseId(responseId)
                .build();

        setBaseEntity(mSingleChoiceResponse);

        // MongoDB CD (INSERT)
        mongoInsert(op, mSingleChoiceResponse);

        // Kafka Topic (Druid)
        sendTopicMessage("libertyform.single_choice_response", singleChoiceResponse);
    }
}
