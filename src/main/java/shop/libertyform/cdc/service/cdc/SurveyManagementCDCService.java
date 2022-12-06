package shop.libertyform.cdc.service.cdc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import shop.libertyform.cdc.domain.SurveyManagement;
import shop.libertyform.cdc.domain.mongo.MSurveyManagement;
import shop.libertyform.cdc.domain.status.ResponseStatus;
import shop.libertyform.cdc.repository.CommonRepository;
import shop.libertyform.cdc.repository.mongo.MCommonRepository;
import shop.libertyform.cdc.service.CrudCDC;

import java.time.LocalDate;

@Service
public class SurveyManagementCDCService extends CrudCDC {

    public SurveyManagementCDCService(CommonRepository<SurveyManagement> commonRepository, MCommonRepository<MSurveyManagement> mCommonRepository) {
        super(commonRepository, mCommonRepository);
    }

    @KafkaListener(topics = "source-db.liberty_form-api.survey_management", groupId = "foo")
    public void consume(@Payload(required = false) String data) throws JsonProcessingException {
        if (data == null) // delete 했을 경우
            return;

        afterMap = getAfterMap(data);

        if(afterMap == null){ // 삭제했을 경우
            beforeMap = getBeforeMap(data);
            long id = Long.parseLong(beforeMap.get("id").toString());

            // CDC 동작 (REMOVE)
            SurveyManagement surveyManagement = new SurveyManagement();
            removeEntity(id, surveyManagement);
            return;
        }

        String op = afterMap.get("op").toString();

        Long contactId = getLongValue("contact_id");
        Long memberId = getLongValue("member_id");
        Long survey_id = getLongValue("survey_id");

        String code = getStringValue("code");

        LocalDate expirationDate = getDateValue("expiration_date");

        ResponseStatus responseStatus = getResponseStatusValue("response_status");

        // MYSQL
        SurveyManagement surveyManagement = SurveyManagement.builder()
                .surveyId(survey_id)
                .contactId(contactId)
                .memberId(memberId)
                .code(code)
                .expiredDate(expirationDate)
                .responseStatus(responseStatus)
                .build();

        setBaseEntity(surveyManagement);

        // CDC 동작 (INSERT & UPDATE)
        insertOrCreateEntity(op, surveyManagement);

        // MONGO DB
        MSurveyManagement mSurveyManagement = MSurveyManagement.builder()
                .surveyId(survey_id)
                .contactId(contactId)
                .memberId(memberId)
                .code(code)
                .expiredDate(expirationDate)
                .responseStatus(responseStatus)
                .build();

        setBaseEntity(mSurveyManagement);

        // MongoDB CD (INSERT)
        mongoInsert(op, mSurveyManagement);
    }

    public ResponseStatus getResponseStatusValue(String key){
        Object value = afterMap.get(key);

        if(value == null){
            return null;
        }else{
            return ResponseStatus.valueOf(value.toString());
        }
    }
}
