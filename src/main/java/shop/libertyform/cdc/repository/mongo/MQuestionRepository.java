package shop.libertyform.cdc.repository.mongo;

import org.springframework.stereotype.Repository;
import shop.libertyform.cdc.domain.mongo.MNumericResponse;
import shop.libertyform.cdc.domain.mongo.MQuestion;

@Repository
public interface MQuestionRepository extends MCommonRepository<MQuestion>{

}
