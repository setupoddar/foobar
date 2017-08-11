package com.flipkart.mdm.dal.dao;


import com.flipkart.mdm.model.User;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@Slf4j
public class UserDAO extends GenericDAO<User> {

    @Inject
    protected UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public User findByName(String userId) {
        Criteria criteria = criteria();
        criteria.add(Restrictions.eq("name", userId));
        return uniqueResult(criteria);
    }
}
