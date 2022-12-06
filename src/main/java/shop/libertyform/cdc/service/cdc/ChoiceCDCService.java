package shop.libertyform.cdc.service.cdc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import shop.libertyform.cdc.domain.Choice;
import shop.libertyform.cdc.domain.Survey;
import shop.libertyform.cdc.domain.mongo.MChoice;
import shop.libertyform.cdc.domain.mongo.MSurvey;
import shop.libertyform.cdc.repository.CommonRepository;
import shop.libertyform.cdc.repository.mongo.MCommonRepository;
import shop.libertyform.cdc.service.CrudCDC;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class ChoiceCDCService extends CrudCDC {

    public ChoiceCDCService(CommonRepository<Choice> commonRepository, MCommonRepository<MChoice> mCommonRepository) {
        super(commonRepository, mCommonRepository);
    }

    @KafkaListener(topics = "source-db.liberty_form-api.choice", groupId = "foo")
    public void consume(@Payload(required = false) String data) throws JsonProcessingException {
        if (data == null) // delete 했을 경우
            return;

        afterMap = getAfterMap(data);

        if(afterMap == null){ // 삭제했을 경우
            beforeMap = getBeforeMap(data);
            long id = Long.parseLong(beforeMap.get("id").toString());

            // CDC 동작 (REMOVE)
            Choice choice = new Choice();
            removeEntity(id, choice);
            return;
        }

        String op = afterMap.get("op").toString();

        Long questionId = getLongValue("question_id");

        Integer number = getIntegerValue("number");

        String name = getStringValue("name");

        // MYSQL
        Choice choice = Choice.builder()
                .questionId(questionId)
                .name(name)
                .number(number)
                .build();


        setBaseEntity(choice);

        // CDC 동작 (INSERT & UPDATE)
        insertOrCreateEntity(op, choice);

        // MONGO DB
        MChoice mchoice = MChoice.builder()
                .questionId(questionId)
                .name(name)
                .number(number)
                .build();

        setBaseEntity(mchoice);

        // MongoDB CD (INSERT)
        mongoInsert(op, mchoice);
    }
}
