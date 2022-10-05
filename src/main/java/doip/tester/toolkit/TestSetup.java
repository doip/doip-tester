package doip.tester.toolkit;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import doip.junit.InitializationError;
import doip.library.properties.EmptyPropertyValue;
import doip.library.properties.MissingProperty;
import doip.library.properties.MissingSystemProperty;

/**
 * Contains all utilities to perform tests for a DoIP gateway.
 * It makes implementation of test cases much more easy.
 */
public class TestSetup {

	/**
	 * log4j logger
	 */
	private static Logger logger = LogManager.getLogger(TestSetup.class);
	
	/**
	 * Test configuration with parameters for all tests.
	 */
	private TestConfig config = null; 
	
	/**
	 * List of TCP connections to the gateway
	 */
	private Vector<TesterTcpConnection> tcpConnections = 
			new Vector<TesterTcpConnection>();
	
	/**
	 * Module for udp communication for a tester
	 */
	private TesterUdpCommModule testerUdpCommModule = null;
	
	
	public TestSetup() {
		tcpConnections.ensureCapacity(8);
	}
	
	/**
	 * Initializes the test setup
	 * @return Returns true if initialization was successful.
	 * @throws Exception 
	 * @throws EmptyPropertyValue 
	 * @throws MissingProperty 
	 * @throws IOException 
	 */
	public boolean initialize() throws InitializationError {
		String function = "public boolean initialize(String filename)";
		try {
			if (logger.isTraceEnabled()) {
				logger.trace(">>> " + function);
			}
			
			logger.debug("Initialize the test setup");
	
			this.config = new TestConfig();
			logger.debug("Create UDP socket");
			this.testerUdpCommModule = new TesterUdpCommModule(this.config);
			DatagramSocket socket = new DatagramSocket();
			logger.debug("Start thread which listens on data from UDP socket");
			this.testerUdpCommModule.start(socket);
			
			logger.debug("Test setup has been completely initialized.");
			return true;
			
	
		} catch (IOException e) {
			throw logger.throwing(new InitializationError(e));
		} catch (MissingProperty e) {
			throw logger.throwing(new InitializationError(e));
		} catch (EmptyPropertyValue e) {
			throw logger.throwing(new InitializationError(e));
		} catch (MissingSystemProperty e) {
			throw logger.throwing(new InitializationError(e));
		} finally {
			if (logger.isTraceEnabled()) {
				logger.trace("<<< public boolean initialize(String filename)");
			}
		}
	}
	
	/**
	 * Uninitializes the test setup
	 * @return
	 */
	public boolean uninitialize() {
		String function = "public boolean uninitialize()";
		try {
			if (logger.isTraceEnabled()) {
				logger.trace(">>> public boolean uninitialize()");
			}
	
			if (this.tcpConnections != null) {
				for (DoipTcpConnectionWithEventCollection conn : this.tcpConnections) {
					conn.stop();																	
				}
				this.tcpConnections.clear();
			}
			
			if (this.testerUdpCommModule != null) {
				this.testerUdpCommModule.stop();
				this.testerUdpCommModule = null;
			}
			
			this.config = null;
		
			return true;

		} finally {
			if (logger.isTraceEnabled()) {
				logger.trace("<<< public boolean uninitialize()");
			}
		}
	}
	
	/**
	 * Creates a new TCP connection to the DoIP gateway.
	 * @return The new TCP connection
	 * @throws IOException 
	 */
	public TesterTcpConnection createTesterTcpConnection() throws IOException {
		try {
			if (logger.isTraceEnabled()) {
				logger.trace(">>> public DoipTcpConnectionWithEventCollection createDoipTcpConnectionWithEventCollection()");
			}
		
			TesterTcpConnection conn = new TesterTcpConnection(config);
			this.tcpConnections.add(conn);
			logger.info("Connect to host with IP address " + config.getTargetAddress() + " and port number " + config.getTargetPort());
			long before = System.nanoTime();
			Socket socket = new Socket(config.getTargetAddress(), config.getTargetPort());
			long after = System.nanoTime();
			long duration = after - before;
			logger.info("Connection established. It took " + duration + " ns to establish the connection.");
			socket.setTcpNoDelay(true);
			conn.start(socket);
			return conn;
		
		} finally {
			if (logger.isTraceEnabled()) {
				logger.trace("<<< public DoipTcpConnectionWithEventCollection createDoipTcpConnectionWithEventCollection()");
			}
		}
	}
	
	/**
	 * Removes a TCP connection.
	 * @param conn
	 */
	public void removeDoipTcpConnectionTest(DoipTcpConnectionWithEventCollection conn) {
		try {
			if (logger.isTraceEnabled()) {
				logger.trace(">>> public void removeDoipTcpConnectionTest(TestDoipTcpConnection conn)");
			}
			
			conn.stop();
			this.tcpConnections.remove(conn);
		
		} finally {
			if (logger.isTraceEnabled()) {
				logger.trace("<<< public void removeDoipTcpConnectionTest(TestDoipTcpConnection conn)");
			}
		}
	}
	
	public TesterUdpCommModule getTesterUdpCommModule() {
		return this.testerUdpCommModule;
	}
	
	public TestConfig getConfig() {
		return this.config;
	}
}
