package shop.libertyform.cdc.service.cdc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import shop.libertyform.cdc.domain.Choice;
import shop.libertyform.cdc.domain.QuestionType;
import shop.libertyform.cdc.domain.mongo.MChoice;
import shop.libertyform.cdc.domain.mongo.MQuestion;
import shop.libertyform.cdc.domain.mongo.MQuestionType;
import shop.libertyform.cdc.repository.CommonRepository;
import shop.libertyform.cdc.repository.mongo.MCommonRepository;
import shop.libertyform.cdc.service.CrudCDC;

@Service
public class QuestionTypeCDCService extends CrudCDC {

    public QuestionTypeCDCService(CommonRepository<QuestionType> commonRepository, MCommonRepository<MQuestionType> mCommonRepository) {
        super(commonRepository, mCommonRepository);
    }

    @KafkaListener(topics = "source-db.liberty_form-api.question_type", groupId = "foo")
    public void consume(@Payload(required = false) String data) throws JsonProcessingException {
        if (data == null) // delete 했을 경우
            return;

        afterMap = getAfterMap(data);

        if(afterMap == null){ // 삭제했을 경우
            beforeMap = getBeforeMap(data);
            long id = Long.parseLong(beforeMap.get("id").toString());

            // CDC 동작 (REMOVE)
            QuestionType questionType = new QuestionType();
            removeEntity(id, questionType);
            return;
        }

        String op = afterMap.get("op").toString();

        Boolean hasChoices = getBooleanValue("has_choices");

        String name = getStringValue("name");

        // MYSQL
        QuestionType questionType = QuestionType.builder()
                .hasChoices(hasChoices)
                .name(name)
                .build();


        setBaseEntity(questionType);

        // CDC 동작 (INSERT & UPDATE)
        insertOrCreateEntity(op, questionType);

        // MONGO DB
        MQuestionType mQuestionType = MQuestionType.builder()
                .hasChoices(hasChoices)
                .name(name)
                .build();


        setBaseEntity(mQuestionType);

        // MongoDB CD (INSERT)
        mongoInsert(op, mQuestionType);
    }
}
