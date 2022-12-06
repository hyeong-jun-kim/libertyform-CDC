package shop.libertyform.cdc.repository.mongo;

import org.springframework.stereotype.Repository;
import shop.libertyform.cdc.domain.mongo.MContact;
import shop.libertyform.cdc.domain.mongo.MMemberContact;

@Repository
public interface MMemberContactRepository extends MCommonRepository<MMemberContact>{

}
