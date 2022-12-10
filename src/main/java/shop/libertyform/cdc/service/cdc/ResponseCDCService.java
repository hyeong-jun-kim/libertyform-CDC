package shop.libertyform.cdc.service.cdc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import shop.libertyform.cdc.domain.Choice;
import shop.libertyform.cdc.domain.Response;
import shop.libertyform.cdc.domain.mongo.MChoice;
import shop.libertyform.cdc.domain.mongo.MResponse;
import shop.libertyform.cdc.repository.CommonRepository;
import shop.libertyform.cdc.repository.mongo.MCommonRepository;
import shop.libertyform.cdc.service.CrudCDC;
import shop.libertyform.cdc.service.KafkaProducer;

@Service
public class ResponseCDCService extends CrudCDC {

    public ResponseCDCService(CommonRepository<Response> commonRepository, MCommonRepository<MResponse> mCommonRepository
    , KafkaProducer kafkaProducer) {
        super(commonRepository, mCommonRepository, kafkaProducer);
    }

    @KafkaListener(topics = "source-db.liberty_form-api.response", groupId = "foo")
    public void consume(@Payload(required = false) String data) throws JsonProcessingException, IllegalAccessException {
        if (data == null) // delete 했을 경우
            return;

        afterMap = getAfterMap(data);

        if(afterMap == null){ // 삭제했을 경우
            beforeMap = getBeforeMap(data);
            long id = Long.parseLong(beforeMap.get("id").toString());

            // CDC 동작 (REMOVE)
            Response response = new Response();
            removeEntity(id, response);
            return;
        }

        String op = afterMap.get("op").toString();

        Long memberId = getLongValue("member_id");
        Long surveyId = getLongValue("survey_id");

        // MYSQL
        Response response = Response.builder()
                .memberId(memberId)
                .surveyId(surveyId)
                .build();

        setBaseEntity(response);

        // CDC 동작 (INSERT & UPDATE)
        insertOrCreateEntity(op, response);

        // MONGO DB
        MResponse mResponse = MResponse.builder()
                .memberId(memberId)
                .surveyId(surveyId)
                .build();

        setBaseEntity(mResponse);

        // MongoDB CD (INSERT)
        mongoInsert(op, mResponse);

        // Kafka Topic (Druid)
        sendTopicMessage("libertyform.response", response);
    }
}
