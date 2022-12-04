package shop.libertyform.cdc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import shop.libertyform.cdc.domain.BaseEntity;
import shop.libertyform.cdc.domain.status.BaseStatus;
import shop.libertyform.cdc.repository.CommonRepository;
import shop.libertyform.cdc.repository.mongo.MCommonRepository;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public abstract class CrudCDC<E extends BaseEntity, M extends BaseEntity> {
    private CommonRepository<E> commonRepository;

    private MCommonRepository<M> mCommonRepository;

    protected Map<String, Object> afterMap;
    protected Map<String, Object> beforeMap;

    @Autowired
    public CrudCDC(CommonRepository commonRepository, MCommonRepository mCommonRepository) {
        this.commonRepository = commonRepository;
        this.mCommonRepository = mCommonRepository;
    }

    // 생성 및 업데이트 CDC
    public void insertOrCreateEntity(String op, E entity) {
        String className = entity.getClass().getName();

        switch (op) {
            case "c": // 생성
                commonRepository.save(entity);
                System.out.println(className + " " + entity.getId() + "번 객체가 저장되었습니다.");
                break;
            case "u": // 업데이트
                commonRepository.update(entity);
                System.out.println(entity.getId() + "번 객체가 업데이트 되었습니다.");
                break;
        }
    }

    // 몽고 DB 생성 및 업데이트
    public void mongoInsert(String op, M entity) {
        String className = entity.getClass().getName();

        switch (op) {
            case "c": // 생성
                mCommonRepository.save(entity);
                System.out.println(className + " " + entity.getId() + "번 객체가 저장되었습니다.");
                break;
            case "u": // 업데이트
                commonRepository.update(entity);
                System.out.println(entity.getId() + "번 객체가 업데이트 되었습니다.");
                break;
        }
    }

    // 삭제 CDC
    public void removeEntity(long id, E entity) {
        String className = entity.getClass().getName();

        // MYSQL
        commonRepository.removeById(id, entity);

        // MONGO
        mCommonRepository.deleteById(id);

        System.out.println(className + " " + id + "번 객체가 삭제되었습니다.");

    }

    public Map<String, Object> getAfterMap(String data) throws JsonProcessingException {
        System.out.println("data = " + data);

        // JSON으로 변환
        JSONObject json = new JSONObject(data);

        // payload body를 가져옴
        JSONObject payload = json.getJSONObject("payload");

        // op (DML 명령어) 확인
        String op = payload.get("op").toString();

        // 변경 후 데이터 가져오기
        String after = payload.get("after").toString();

        if (after.equals("null")) // delete 했을 경우
            return null;

        // 변경된 데이터를 Map으로 변경해서 편리하게 key에 접근
        Map<String, Object> res = new ObjectMapper().readValue(after, HashMap.class);

        // op 값 저장
        res.put("op", op);

        return res;
    }

    public Map<String, Object> getBeforeMap(String data) throws JsonProcessingException {
        System.out.println("data = " + data);

        JSONObject json = new JSONObject(data);

        JSONObject payload = json.getJSONObject("payload");

        // 변경 전 데이터 가져오기
        String before = payload.get("before").toString();

        // 변경 전 데이터를 Map으로 변경해서 편리하게 key에 접근
        Map<String, Object> res = new ObjectMapper().readValue(before, HashMap.class);

        return res;
    }

    // 상속 엔티티 클래스 정보 저장
    public <T extends BaseEntity> void setBaseEntity(T baseEntity){
        long id = Long.parseLong(afterMap.get("id").toString());
        BaseStatus status = getBaseStatusValue("status");
        LocalDateTime createdAt = getLocalDateTimeValue("created_at");
        LocalDateTime updatedAt = getLocalDateTimeValue("updated_at");

        baseEntity.setBaseEntity(id, status, createdAt, updatedAt);
    }

    public LocalDateTime parseLocalDateTime(Object localDateTime) {
        long dateTime = Long.parseLong(localDateTime.toString());
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTime), ZoneId.systemDefault());
    }

    public String getStringValue(String key){
        Object value = afterMap.get(key);

        if(value == null){
            return null;
        }else{
            return value.toString();
        }
    }

    public Long getLongValue(String key){
        Object value = afterMap.get(key);

        if(value == null){
            return null;
        }else{
            return Long.parseLong(value.toString());
        }
    }

    public BaseStatus getBaseStatusValue(String key){
        Object value = afterMap.get(key);

        if(value == null){
            return null;
        }else{
            return BaseStatus.valueOf(value.toString());
        }
    }

    public LocalDateTime getLocalDateTimeValue(String key){
        Object value = afterMap.get(key);

        if(value == null){
            return null;
        }else{
            return parseLocalDateTime(afterMap.get("updated_at"));
        }
    }
}
