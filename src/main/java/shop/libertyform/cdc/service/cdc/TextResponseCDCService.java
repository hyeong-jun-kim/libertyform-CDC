package shop.libertyform.cdc.service.cdc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import shop.libertyform.cdc.domain.Choice;
import shop.libertyform.cdc.domain.TextResponse;
import shop.libertyform.cdc.domain.mongo.MChoice;
import shop.libertyform.cdc.domain.mongo.MTextResponse;
import shop.libertyform.cdc.domain.status.ResponseStatus;
import shop.libertyform.cdc.domain.type.TextType;
import shop.libertyform.cdc.repository.CommonRepository;
import shop.libertyform.cdc.repository.mongo.MCommonRepository;
import shop.libertyform.cdc.service.CrudCDC;

@Service
public class TextResponseCDCService extends CrudCDC {

    public TextResponseCDCService(CommonRepository<TextResponse> commonRepository, MCommonRepository<MTextResponse> mCommonRepository) {
        super(commonRepository, mCommonRepository);
    }

    @KafkaListener(topics = "source-db.liberty_form-api.text_response", groupId = "foo")
    public void consume(@Payload(required = false) String data) throws JsonProcessingException {
        if (data == null) // delete 했을 경우
            return;

        afterMap = getAfterMap(data);

        if(afterMap == null){ // 삭제했을 경우
            beforeMap = getBeforeMap(data);
            long id = Long.parseLong(beforeMap.get("id").toString());

            // CDC 동작 (REMOVE)
            TextResponse textResponse = new TextResponse();
            removeEntity(id, textResponse);
            return;
        }

        String op = afterMap.get("op").toString();

        Long questionId = getLongValue("question_id");
        Long responseId = getLongValue("response_id");

        String value = getStringValue("value");

        TextType textType = getTextTypeValue("text_type");

        // MYSQL
        TextResponse textResponse = TextResponse.builder()
                .questionId(questionId)
                .responseId(responseId)
                .value(value)
                .textType(textType)
                .build();

        setBaseEntity(textResponse);

        // CDC 동작 (INSERT & UPDATE)
        insertOrCreateEntity(op, textResponse);

        // MONGO DB
        MTextResponse mTextResponse = MTextResponse.builder()
                .questionId(questionId)
                .responseId(responseId)
                .value(value)
                .textType(textType)
                .build();

        setBaseEntity(mTextResponse);

        // MongoDB CD (INSERT)
        mongoInsert(op, mTextResponse);
    }

    public TextType getTextTypeValue(String key){
        Object value = afterMap.get(key);

        if(value == null){
            return null;
        }else{
            return TextType.valueOf(value.toString());
        }
    }
}
