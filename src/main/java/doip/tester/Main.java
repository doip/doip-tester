package doip.tester;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class Main {

	public static void main(String[] args) throws SocketException {
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
				.getNetworkInterfaces();
		for (NetworkInterface networkInterface : Collections.list(networkInterfaces)) {
			analyseNetworkInterface(networkInterface);
		}
	}

	private static void analyseNetworkInterface(
			NetworkInterface networkInterface) {
		System.out.println("======================================================");
		System.out.println("Display name: " + networkInterface.getDisplayName());
		System.out.println("Name:         " + networkInterface.getName());
		List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
		for (InterfaceAddress interfaceAddress : interfaceAddresses) {
			System.out.println("------------------------------------------------------");
			System.out.println("    Address:   " + interfaceAddress.getAddress().toString());
			System.out.println("    Broadcast: " + interfaceAddress.getBroadcast());
		}
	}
}
