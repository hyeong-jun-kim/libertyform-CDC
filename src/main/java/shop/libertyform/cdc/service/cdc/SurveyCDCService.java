package shop.libertyform.cdc.service.cdc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import shop.libertyform.cdc.domain.Survey;
import shop.libertyform.cdc.domain.mongo.MSurvey;
import shop.libertyform.cdc.repository.CommonRepository;
import shop.libertyform.cdc.repository.mongo.MCommonRepository;
import shop.libertyform.cdc.service.CrudCDC;
import shop.libertyform.cdc.service.KafkaProducer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class SurveyCDCService extends CrudCDC {

    public SurveyCDCService(CommonRepository<Survey> commonRepository, MCommonRepository<MSurvey> mCommonRepository
    , KafkaProducer kafkaProducer) {
        super(commonRepository, mCommonRepository, kafkaProducer);
    }

    @KafkaListener(topics = "source-db.liberty_form-api.survey", groupId = "foo")
    public void consume(@Payload(required = false) String data) throws JsonProcessingException, IllegalAccessException {
        if (data == null) // delete 했을 경우
            return;

        afterMap = getAfterMap(data);

        if(afterMap == null){ // 삭제했을 경우
            beforeMap = getBeforeMap(data);
            long id = Long.parseLong(beforeMap.get("id").toString());

            // CDC 동작 (REMOVE)
            Survey survey = new Survey();
            removeEntity(id, survey);
            return;
        }

        String op = afterMap.get("op").toString();

        Long memberId = getLongValue("member_id");

        String code = getStringValue("code");
        String description = getStringValue("description");
        String name = getStringValue("name");
        String thumbnailImg = getStringValue("thumbnailImg");

        LocalDate expirationDate = getDateValue("expiration_date");

        // MYSQL
        Survey survey = Survey.builder()
                .memberId(memberId)
                .code(code)
                .description(description)
                .name(name)
                .thumbnailImg(thumbnailImg)
                .expirationDate(expirationDate)
                .build();

        setBaseEntity(survey);

        // CDC 동작 (INSERT & UPDATE)
        insertOrCreateEntity(op, survey);

        // MONGO DB
        MSurvey mSurvey = MSurvey.builder()
                .memberId(memberId)
                .code(code)
                .description(description)
                .name(name)
                .thumbnailImg(thumbnailImg)
                .expirationDate(expirationDate)
                .build();

        setBaseEntity(mSurvey);

        // MongoDB CD (INSERT)
        mongoInsert(op, mSurvey);

        // Kafka Topic (Druid)
        sendTopicMessage("libertyform.survey", survey);
    }
}
