#!/usr/bin/env python3
import sys
import socket
port = 13500

address = ("localhost", port)
UDPSocket = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)
UDPSocket.bind(address)
while(True):
    data, addr = UDPSocket.recvfrom(4096)
    sys.stdout.write(str(data, "utf-8"))
