package com.flipkart.mdm.dal.dao;


import com.flipkart.mdm.model.CuratedFSN;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@Slf4j
public class CuratedFSNDAO extends GenericDAO<CuratedFSN> {

    @Inject
    protected CuratedFSNDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public CuratedFSN findByTaskId(String id) {
        Criteria criteria = criteria();
        criteria.add(Restrictions.eq("taskId.id", id));
        return uniqueResult(criteria);
    }
}
