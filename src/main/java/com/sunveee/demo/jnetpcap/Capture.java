package com.sunveee.demo.jnetpcap;

import java.util.ArrayList;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

/**
 * 捕获器
 * 
 * @author Wu Yiyang
 * @version 2018-09-30 10:38:02
 */
public class Capture {

    public static void startCapture() {
        List<PcapIf> alldevs = new ArrayList<PcapIf>();
        StringBuilder errbuf = new StringBuilder();

        /** 1.Get devices **/

        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r == Pcap.ERROR || alldevs.isEmpty()) {
            System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
            return;
        }

        System.out.println("Network devices found:");

        // 迭代输出所有网卡
        int i = 0;
        for (PcapIf device : alldevs) {
            String description = (device.getDescription() != null) ? device.getDescription()
                    : "No description available";
            System.out.printf("#%d: %s [%s]\n", i++, device.getName(), description);
        }

        PcapIf device = alldevs.get(0); // 选择监听的网卡
        System.out.printf("\nChoosing '%s'.\n",
                (device.getDescription() != null) ? device.getDescription() : device.getName());

        /** 2.Open up choosed device **/

        int snaplen = 65536; // 截取长度。最大长度为65535，设置为65536保证不会被截断。
        int promisc = Pcap.MODE_PROMISCUOUS; // 混杂模式
        int timeout = 10 * 1000; // 10 seconds in millis
        Pcap pcap = Pcap.openLive(device.getName(), snaplen, promisc, timeout, errbuf);

        /**
         * TODO 关于使截取长度snaplen生效的问题
         * 
         * @see org.jnetpcap.Pcap#openLive(String, int, int, int, StringBuilder)
         */

        if (pcap == null) {
            System.err.printf("Error while opening device for capture: " + errbuf.toString());
            return;
        }

        /** 3.Enter the loop **/
        pcap.loop(-1, new SimpleStdOutputPacketHandler<String>(), "SunVeee");

        // pcap.close();
    }

}
