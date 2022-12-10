package shop.libertyform.cdc.service.cdc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import shop.libertyform.cdc.domain.Choice;
import shop.libertyform.cdc.domain.NumericResponse;
import shop.libertyform.cdc.domain.mongo.MChoice;
import shop.libertyform.cdc.domain.mongo.MNumericResponse;
import shop.libertyform.cdc.repository.CommonRepository;
import shop.libertyform.cdc.repository.mongo.MCommonRepository;
import shop.libertyform.cdc.service.CrudCDC;
import shop.libertyform.cdc.service.KafkaProducer;

@Service
public class NumericResponseCDCService extends CrudCDC {

    public NumericResponseCDCService(CommonRepository<NumericResponse> commonRepository, MCommonRepository<MNumericResponse> mCommonRepository
    , KafkaProducer kafkaProducer) {
        super(commonRepository, mCommonRepository, kafkaProducer);
    }

    @KafkaListener(topics = "source-db.liberty_form-api.numeric_response", groupId = "foo")
    public void consume(@Payload(required = false) String data) throws JsonProcessingException, IllegalAccessException {
        if (data == null) // delete 했을 경우
            return;

        afterMap = getAfterMap(data);

        if(afterMap == null){ // 삭제했을 경우
            beforeMap = getBeforeMap(data);
            long id = Long.parseLong(beforeMap.get("id").toString());

            // CDC 동작 (REMOVE)
            NumericResponse numericResponse = new NumericResponse();
            removeEntity(id, numericResponse);
            return;
        }

        String op = afterMap.get("op").toString();

        Long questionId = getLongValue("question_id");
        Long responseId = getLongValue("response_id");

        // MYSQL
        NumericResponse numericResponse = NumericResponse.builder()
                .questionId(questionId)
                .responseId(responseId)
                .build();

        setBaseEntity(numericResponse);

        // CDC 동작 (INSERT & UPDATE)
        insertOrCreateEntity(op, numericResponse);

        // MONGO DB
        MNumericResponse mNumericResponse = MNumericResponse.builder()
                .questionId(questionId)
                .responseId(responseId)
                .build();

        setBaseEntity(mNumericResponse);

        // MongoDB CD (INSERT)
        mongoInsert(op, mNumericResponse);

        // Kafka Topic (Druid)
        sendTopicMessage("libertyform.numeric_response", numericResponse);
    }
}
