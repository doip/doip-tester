package doip.tester.toolkit.server4unittest;

import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;

import doip.library.comm.DoipTcpConnectionListener;
import doip.library.net.TcpReceiverListener;
import doip.library.net.TcpReceiverThread;
import doip.logging.LogManager;
import doip.logging.Logger;

public class TcpConnection4UnitTest implements TcpReceiverListener {
	
	private static Logger logger = LogManager.getLogger(TcpConnection4UnitTest.class);

	private TcpReceiverThread tcpReceiverThread = null;
	
	/**
	 * List of listeners which will be notified when new DoIP messages had been
	 * received. A typical listener is a gateway. The gateway will handle the
	 * messages.
	 */
	private LinkedList<TcpConnectionListener> listeners = new LinkedList<TcpConnectionListener>();
	
	/**
	 * The socket on which data will be received
	 */
	private Socket socket = null;
	
		
	public TcpConnection4UnitTest(String tcpReceiverThreadName, int maxByteArraySizeLogging) {
		this.tcpReceiverThread = new TcpReceiverThread(tcpReceiverThreadName, maxByteArraySizeLogging);
	}	
	
	/**
	 * Starts to listen on the TCP socket for incoming data.
	 * 
	 * @param socket
	 * @throws IOException 
	 */
	public void start(Socket socket) {
		if (logger.isTraceEnabled()) {
			logger.trace(">>> public void start(Socket socket)");
		}
		this.socket = socket;
		this.tcpReceiverThread.addListener(this);
		this.tcpReceiverThread.start(socket);
		if (logger.isTraceEnabled()) {
			logger.trace("<<< public void start(Socket socket)");
		}
	}

	/**
	 * Stops to listen on the socket for incoming data.
	 */
	public void stop() {
		if (logger.isTraceEnabled()) {
			logger.trace(">>> public void stop()");
		}
		this.tcpReceiverThread.stop();
		
		/* Give the thread some time to terminate and call the listeners.
		 * The thread will call the function "onSocketClosed(...)".
		 * If it is too fast then function removeListener will remove listener
		 * before it had a chance to call the method onSocketClosed().
		 */
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		};
		this.tcpReceiverThread.removeListener(this);
		if (logger.isTraceEnabled()) {
			logger.trace("<<< public void stop()");
		}
	}

	public void addListener(TcpConnectionListener listener) {
		logger.trace(">>> void addListener(DoipTcpConnectionListener listener)");
		this.listeners.add(listener);
		logger.trace("<<< void addListener(DoipTcpConnectionListener listener)");
	}

	public void removeListener(TcpConnectionListener listener) {
		logger.trace(">>> void removeListener(DoipTcpConnectionListener listener)");
		this.listeners.remove(listener);
		logger.trace("<<< void removeListener(DoipTcpConnectionListener listener)");
	}

	@Override
	public void onDataReceived(byte[] data) {
		
	}

	@Override
	public void onSocketClosed() {
		
	}

}
