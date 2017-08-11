package com.flipkart.mdm.dal.dao;


import com.flipkart.mdm.model.Task;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

@Slf4j
public class TaskDAO extends GenericDAO<Task> {

    @Inject
    protected TaskDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Task> getAll(String userId) {
        Criteria criteria = criteria();
        criteria.add(Restrictions.eq("user.id", userId));
        return list(criteria);
    }
}
