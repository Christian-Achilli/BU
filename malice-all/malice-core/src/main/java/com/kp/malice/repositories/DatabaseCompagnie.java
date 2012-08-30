package com.kp.malice.repositories;

import com.kp.malice.entities.persisted.CompPtf;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;

public class DatabaseCompagnie {

    public CompPtf find(long id) {
        return (CompPtf) HibernateSessionFactoryUtil.getSession().get(CompPtf.class, id);
    }

}
