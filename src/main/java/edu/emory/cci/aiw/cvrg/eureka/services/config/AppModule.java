/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.services.config;

import com.google.inject.AbstractModule;
import javax.mail.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.google.inject.TypeLiteral;
import com.google.inject.jndi.JndiIntegration;
import com.google.inject.servlet.SessionScoped;

import edu.emory.cci.aiw.cvrg.eureka.services.dao.AnalyticsServiceRoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.AuthorizedUserDao;

import org.eurekaclinical.protempa.client.EurekaClinicalProtempaClient;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaRoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaUserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.AuthorizedRoleEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;

import org.eurekaclinical.standardapis.dao.RoleDao;
import org.eurekaclinical.standardapis.dao.UserDao;
import org.eurekaclinical.standardapis.dao.UserTemplateDao;
import org.eurekaclinical.standardapis.entity.RoleEntity;
import org.eurekaclinical.standardapis.entity.UserEntity;

/**
 * Configure all the non-web related binding for Guice.
 *
 * @author hrathod
 *
 */
class AppModule extends AbstractModule {

    private final EtlClientProvider etlClientProvider;
    
    AppModule(EtlClientProvider inEtlClientProvider) {
        this.etlClientProvider = inEtlClientProvider;
    }

    @Override
    protected void configure() {
        bind(new TypeLiteral<UserDao<? extends UserEntity<? extends RoleEntity>>>() {}).to(JpaUserDao.class);
        bind(AnalyticsServiceRoleDao.class).to(JpaRoleDao.class); 
        bind(new TypeLiteral<RoleDao<AuthorizedRoleEntity>>() {}).to(JpaRoleDao.class);
        bind(AuthorizedUserDao.class).to(JpaUserDao.class);

        bind(Context.class).to(InitialContext.class);
        bind(Session.class).toProvider(
                JndiIntegration.fromJndi(Session.class,
                        "java:comp/env/mail/Session"));

        bind(EurekaClinicalProtempaClient.class).toProvider(this.etlClientProvider).in(SessionScoped.class);
    }

}
