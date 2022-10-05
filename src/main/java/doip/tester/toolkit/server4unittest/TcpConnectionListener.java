package doip.tester.toolkit.server4unittest;

public interface TcpConnectionListener {
	
	public void onDataReceived(TcpConnection4UnitTest conn, byte[] data);

	public void onConnectionClosed(TcpConnection4UnitTest conn);

}
