package doip.tester.toolkit.server4unittest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

import doip.library.net.TcpServer;
import doip.library.net.TcpServerListener;
import doip.library.net.TcpServerThread;
import doip.library.util.Helper;
import doip.logging.LogManager;
import doip.logging.Logger;

public class TcpServer4UnitTest implements TcpServerListener, TcpConnectionListener {

	private static Logger logger = LogManager.getLogger(TcpServer4UnitTest.class);
	
	private ServerSocket tcpSocket = null;
	
	private TcpServerThread tcpServerThread = null;
	
	private static int connectionCounter = 1;
	
	private LinkedList<TcpConnection4UnitTest> tcpConnectionList = new LinkedList<TcpConnection4UnitTest>();
	
	public void start(int port) throws IOException {
		String function = "public void start()";
		try {
			logger.trace(">>> " + function);
			
			logger.info("Create new TcpServerThread with name 'TCP-SERV'");
			tcpServerThread = new TcpServerThread("TCP-SERV");
			tcpServerThread.addListener(this);
			logger.info("Create TCP server socket on port " + port);
			tcpSocket = Helper.createTcpServerSocket(null, port);
			logger.info("Start TcpServerThread");
			tcpServerThread.start(tcpSocket);
	
		} catch (IOException e) {
			logger.fatal("Unexpected " + e.getClass().getName() + " in start()");
			logger.fatal(Helper.getExceptionAsString(e));
			throw e;
		} finally {
			logger.trace("<<< " + function);
		}
	}
	
	public void stop() {
		String function = "public void stop()";
		try {
			logger.trace(">>> " + function);
			if (tcpServerThread != null) {
				logger.info("Stop TCP server thread");
				tcpServerThread.stop();
				tcpServerThread = null;
			}
	
		} finally {
			logger.trace("<<< " + function);
		}
	}

	@Override
	public void onConnectionAccepted(TcpServer tcpServer, Socket socket) {
		String function = "public void onConnectionAccepted(TcpServer tcpServer, Socket socket)";
		try {
			logger.info(">>> " + function);
			logger.info("New TCP connection established");
			logger.info("Set TCP no delay on connection socket");
			socket.setTcpNoDelay(true);
			logger.info("Create new instance of TcpConnection4UnitTest for TCP connection");
			TcpConnection4UnitTest conn = new TcpConnection4UnitTest("TCP-RECV-" + connectionCounter, 64);
			connectionCounter++;
			conn.addListener(this);
			this.tcpConnectionList.add(conn);
			logger.info("Start thread for new TCP connection");
			conn.start(socket);
			
		} catch (SocketException e) {
			String error = "Unexpected " + e.getClass().getName();
			logger.error(error);
			logger.error(Helper.getExceptionAsString(e));
			// We can't throw exception because of signature of inherited method
		} finally {
			logger.info("<<< " + function);
		}
	}

	@Override
	public void onConnectionClosed(TcpConnection4UnitTest tcpConnection) {
		logger.trace(">>> public void onConnectionClosed(DoipTcpConnection doipTcpConnection)");
		logger.info("TCP connection has been closed");
		tcpConnection.removeListener(this);
		tcpConnectionList.remove(tcpConnection);
		logger.trace("<<< public void onConnectionClosed(DoipTcpConnection doipTcpConnection)");
	
	}

	@Override
	public void onDataReceived(TcpConnection4UnitTest conn, byte[] data) {
		
	}
}
