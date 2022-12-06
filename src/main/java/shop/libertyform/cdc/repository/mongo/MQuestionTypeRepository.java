package shop.libertyform.cdc.repository.mongo;

import org.springframework.stereotype.Repository;
import shop.libertyform.cdc.domain.mongo.MQuestion;
import shop.libertyform.cdc.domain.mongo.MQuestionType;

@Repository
public interface MQuestionTypeRepository extends MCommonRepository<MQuestionType>{

}
