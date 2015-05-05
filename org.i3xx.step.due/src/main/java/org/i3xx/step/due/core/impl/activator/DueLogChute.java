package org.i3xx.step.due.core.impl.activator;

/*
 * #%L
 * NordApp OfficeBase :: due
 * %%
 * Copyright (C) 2014 - 2015 I.D.S. DialogSysteme GmbH
 * %%
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
 * #L%
 */


import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DueLogChute implements LogChute {
	
	static Logger logger = LoggerFactory.getLogger(Due.class);
	
	public DueLogChute() {
	}
	
	//
	// Velocity loggiong
	//
	
	public void init(RuntimeServices arg0) throws Exception {
		//does nothing
	}

	public boolean isLevelEnabled(int level) {
		switch(level){
		case ERROR_ID:
			return logger.isErrorEnabled();
		case WARN_ID:
			return logger.isWarnEnabled();
		case INFO_ID:
			return logger.isInfoEnabled();
		case DEBUG_ID:
			return logger.isDebugEnabled();
		case TRACE_ID:
			return logger.isTraceEnabled();
		default:
			return false;
		}
	}

	public void log(int level, String msg) {
		switch(level){
		case ERROR_ID:
			logger.error(msg);
			break;
		case WARN_ID:
			logger.warn(msg);
			break;
		case INFO_ID:
			logger.info(msg);
			break;
		case DEBUG_ID:
			logger.debug(msg);
			break;
		case TRACE_ID:
			logger.trace(msg);
			break;
		default:
		}
	}

	public void log(int level, String msg, Throwable t) {
		switch(level){
		case ERROR_ID:
			logger.error(msg, t);
			break;
		case WARN_ID:
			logger.warn(msg, t);
			break;
		case INFO_ID:
			logger.info(msg, t);
			break;
		case DEBUG_ID:
			logger.debug(msg, t);
			break;
		case TRACE_ID:
			logger.trace(msg, t);
			break;
		default:
		}
	}

}
