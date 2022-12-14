package shop.libertyform.cdc.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import shop.libertyform.cdc.domain.BaseEntity;

@Component
public class CommonRepository<E extends BaseEntity> {
    E entity;

    @PersistenceContext
    EntityManager em;

    public <E> E findById(Class<E> entity, long id){
        return em.find(entity, id);
    }

    @Transactional
    public <E> E save(E entity) {
        em.persist(entity);
        return entity;
    }

    @Transactional
    public <E> E update(E entity) {
        return em.merge(entity);
    }

    @Transactional
    public void removeById(long id, E entity) {
        BaseEntity delEntity = em.find(entity.getClass(), id);
        em.remove(delEntity);
    }
}