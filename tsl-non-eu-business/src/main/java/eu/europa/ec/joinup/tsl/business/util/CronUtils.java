/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager non-EU
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 * 
 * This file is part of the "DIGIT-TSL - Trusted List Manager non-EU" project.
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package eu.europa.ec.joinup.tsl.business.util;

import java.util.Date;

import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;

public class CronUtils {

    public static Date getDateFromExpression(final Date checkDateb, String cronExpression) {
        CronTrigger trigger = new CronTrigger(cronExpression);
        TriggerContext context = new TriggerContext() {

            @Override
            public Date lastScheduledExecutionTime() {
                return checkDateb;
            }

            @Override
            public Date lastActualExecutionTime() {
                return checkDateb;
            }

            @Override
            public Date lastCompletionTime() {
                return checkDateb;
            }
        };
        Date nextExecution = trigger.nextExecutionTime(context);
        return nextExecution;
    }
}
