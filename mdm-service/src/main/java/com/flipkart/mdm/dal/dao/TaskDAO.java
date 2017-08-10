package com.flipkart.mdm.dal.dao;


import com.flipkart.mdm.model.Task;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

@Slf4j
public class TaskDAO extends GenericDAO<Task> {

    @Inject
    protected TaskDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

}
