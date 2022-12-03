package shop.libertyform.cdc.repository.mongo;

import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import shop.libertyform.cdc.domain.BaseEntity;

@NoRepositoryBean
public interface MCommonRepository<E extends BaseEntity> extends MongoRepository<E, Long> {
}
