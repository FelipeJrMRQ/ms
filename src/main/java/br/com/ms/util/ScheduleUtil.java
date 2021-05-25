package br.com.ms.util;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

@ApplicationScoped
public class ScheduleUtil implements Serializable {

	private static final long serialVersionUID = -6711809756096409570L;
	private static SchedulerFactory schedulerFactory;
	private static Scheduler scheduler;
	private static Trigger trigger;

	/**
	 * Enviar pareamtros Segundo Minutos Hora (s m h ) exemplo (0 0/30 * * * ?)
	 * executa a cada 30 minutos
	 * 
	 */
	private static void createSchedule(JobDetail jb, String cronTime, String cronGroup, String triggerName) {
		try {
			schedulerFactory = new StdSchedulerFactory();
			scheduler = schedulerFactory.getScheduler();
			JobDetail job = jb;
			trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, cronGroup).withSchedule(CronScheduleBuilder.cronSchedule(cronTime)).build();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public static void start(JobDetail jb, String cronTime, String cronGroup, String triggerName) throws Exception {
		try {
			createSchedule(jb, cronTime, cronGroup, triggerName);
			scheduler.start();
		} catch (Exception e) {
			throw e;
		}
	}

	public static void pause() throws Exception {
		try {
			scheduler.standby();
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static void shutdown() {
		try {
			scheduler.shutdown();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
