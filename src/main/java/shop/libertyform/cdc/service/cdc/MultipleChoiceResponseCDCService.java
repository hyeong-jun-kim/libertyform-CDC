package shop.libertyform.cdc.service.cdc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import shop.libertyform.cdc.domain.Choice;
import shop.libertyform.cdc.domain.MultipleChoice;
import shop.libertyform.cdc.domain.MultipleChoiceResponse;
import shop.libertyform.cdc.domain.mongo.MChoice;
import shop.libertyform.cdc.domain.mongo.MMultipleChoiceResponse;
import shop.libertyform.cdc.repository.CommonRepository;
import shop.libertyform.cdc.repository.mongo.MCommonRepository;
import shop.libertyform.cdc.service.CrudCDC;

@Service
public class MultipleChoiceResponseCDCService extends CrudCDC {

    public MultipleChoiceResponseCDCService(CommonRepository<MultipleChoiceResponse> commonRepository, MCommonRepository<MMultipleChoiceResponse> mCommonRepository) {
        super(commonRepository, mCommonRepository);
    }

    @KafkaListener(topics = "source-db.liberty_form-api.multiple_choice_response", groupId = "foo")
    public void consume(@Payload(required = false) String data) throws JsonProcessingException {
        if (data == null) // delete 했을 경우
            return;

        afterMap = getAfterMap(data);

        if(afterMap == null){ // 삭제했을 경우
            beforeMap = getBeforeMap(data);
            long id = Long.parseLong(beforeMap.get("id").toString());

            // CDC 동작 (REMOVE)
            MultipleChoiceResponse multipleChoiceResponse = new MultipleChoiceResponse();
            removeEntity(id, multipleChoiceResponse);
            return;
        }

        String op = afterMap.get("op").toString();

        Long responseId = getLongValue("response_id");
        Long questionId = getLongValue("question_id");

        // MYSQL
        MultipleChoiceResponse multipleChoiceResponse = MultipleChoiceResponse.builder()
                .responseId(responseId)
                .questionId(questionId)
                .build();

        setBaseEntity(multipleChoiceResponse);

        // CDC 동작 (INSERT & UPDATE)
        insertOrCreateEntity(op, multipleChoiceResponse);

        // MONGO DB
        MMultipleChoiceResponse mMultipleChoiceResponse = MMultipleChoiceResponse.builder()
                .responseId(responseId)
                .questionId(questionId)
                .build();

        setBaseEntity(mMultipleChoiceResponse);

        // MongoDB CD (INSERT)
        mongoInsert(op, mMultipleChoiceResponse);
    }
}
