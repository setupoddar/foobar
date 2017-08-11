package com.flipkart.mdm.dal.dao;


import com.flipkart.mdm.model.Role;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@Slf4j
public class RoleDAO extends GenericDAO<Role> {

    @Inject
    protected RoleDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Role getByName(String roleId) {
        Criteria criteria = criteria();
        criteria.add(Restrictions.eq("name", roleId));
        return uniqueResult(criteria);
    }
}
