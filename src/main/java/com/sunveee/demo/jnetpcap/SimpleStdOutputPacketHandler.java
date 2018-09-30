package com.sunveee.demo.jnetpcap;

import java.util.Date;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.tcpip.Http;

/**
 * 控制台输出报文PacketHandler
 * 
 * @author Wu Yiyang
 * @version 2018-09-30 10:34:14
 */
public class SimpleStdOutputPacketHandler<T> implements PcapPacketHandler<T> {

    @Override
    public void nextPacket(PcapPacket packet, T user) {
        Http http = new Http();
        if (!packet.hasHeader(http)) {
            return;
        }

        System.out.printf("Received packet at %s caplen=%-4d len=%-4d %s\n",
                new Date(packet.getCaptureHeader().timestampInMillis()), packet.getCaptureHeader().caplen(),
                packet.getCaptureHeader().wirelen(), user);

        String contend = packet.toString();
        System.out.println(contend);

    }
}
