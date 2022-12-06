package shop.libertyform.cdc.service.cdc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import shop.libertyform.cdc.domain.Choice;
import shop.libertyform.cdc.domain.Question;
import shop.libertyform.cdc.domain.mongo.MChoice;
import shop.libertyform.cdc.domain.mongo.MQuestion;
import shop.libertyform.cdc.repository.CommonRepository;
import shop.libertyform.cdc.repository.mongo.MCommonRepository;
import shop.libertyform.cdc.service.CrudCDC;

@Service
public class QuestionCDCService extends CrudCDC {

    public QuestionCDCService(CommonRepository<Question> commonRepository, MCommonRepository<MQuestion> mCommonRepository) {
        super(commonRepository, mCommonRepository);
    }

    @KafkaListener(topics = "source-db.liberty_form-api.question", groupId = "foo")
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

        Long surveyId = getLongValue("survey_id");
        Long questionTypeId = getLongValue("question_id");

        Integer number = getIntegerValue("number");

        Boolean answerRequired = getBooleanValue("answerRequired");

        String name = getStringValue("name");
        String description = getStringValue("description");
        String questionImgUrl = getStringValue("question_img_url");

        // MYSQL
        Question question = Question.builder()
                .surveyId(surveyId)
                .questionTypeId(questionTypeId)
                .name(name)
                .answerRequired(answerRequired)
                .number(number)
                .description(description)
                .questionImgUrl(questionImgUrl)
                .build();


        setBaseEntity(question);

        // CDC 동작 (INSERT & UPDATE)
        insertOrCreateEntity(op, question);

        // MONGO DB
        MQuestion mQuestion = MQuestion.builder()
                .surveyId(surveyId)
                .questionTypeId(questionTypeId)
                .name(name)
                .answerRequired(answerRequired)
                .number(number)
                .description(description)
                .questionImgUrl(questionImgUrl)
                .build();

        setBaseEntity(mQuestion);

        // MongoDB CD (INSERT)
        mongoInsert(op, mQuestion);
    }
}
