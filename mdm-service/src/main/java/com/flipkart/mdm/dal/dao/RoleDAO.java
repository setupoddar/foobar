package com.flipkart.mdm.dal.dao;


import com.flipkart.mdm.model.User;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

@Slf4j
public class RoleDAO extends GenericDAO<User> {

    @Inject
    protected RoleDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

}
