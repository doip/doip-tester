package doip.tester.toolkit;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import doip.library.timer.NanoTimer;
import doip.library.util.Helper;
import doip.tester.toolkit.event.DoipEvent;

public class Wait {
	
	private static Logger logger = LogManager.getLogger(Wait.class);
	
	/**
	 * Waits that a specific number of DoIP events are stored in a event list.
	 * 
	 * @param events The list of events.
	 * 
	 * @param numberOfEvents The minimum number of events that shall be available
	 *                       in the event list.
	 *                        
	 * @param timeoutms Timeout in milliseconds
	 * 
	 * @return Returns true if the list contains at least the specific number of
	 *         of events. If the list does not contain the specific number of
	 *         event it returns false.
	 *         
	 * @throws InterruptedException Will be thrown if sleep function will
	 *                              get interrupted.
	 */
	public static DoipEvent waitForEvents(List<DoipEvent> events, int numberOfEvents, long timeoutms) throws InterruptedException {
		DoipEvent event = null;
		try {
			logger.trace(">>> public boolean waitForEvents(List<DoipEvent> events, int numberOfEvents, long timeout)");
			
			logger.debug("Number of events at function entry: " + events.size());
			NanoTimer timer = new NanoTimer();
			long targetTime = timeoutms * 1000000;
			timer.reset();
			while (timer.getElapsedTime() < targetTime && events.size() < numberOfEvents) {
				try {
					Thread.sleep(0, 1000);
				} catch (InterruptedException e) {
					logger.error("Unexpected " + e.getClass().getName() + " in waitForEvents(...)");
					throw logger.throwing(e);
				}
			}
			
			logger.debug("Number of events at function exit: " + events.size());
			if (events.size() >= numberOfEvents) {
				event =  events.get(numberOfEvents - 1);
			} 
		} finally {
			logger.trace("<<< public boolean waitForEvents(List<DoipEvent> events, int numberOfEvents, long timeout)");
		}
		return event;
	}
}
