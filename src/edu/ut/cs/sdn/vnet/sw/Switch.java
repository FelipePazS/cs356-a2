package edu.ut.cs.sdn.vnet.sw;

import net.floodlightcontroller.packet.Ethernet;
import edu.ut.cs.sdn.vnet.Device;
import edu.ut.cs.sdn.vnet.DumpFile;
import edu.ut.cs.sdn.vnet.Iface;

int MAX_TTL = 15; // time in seconds that marks the lifespan of an entry in the table

/**
 * @author Aaron Gember-Jacobson
 */
public class Switch extends Device
{	
	/**
	 * Creates a router for a specific host.
	 * @param host hostname for the router
	 */
	public Switch(String host, DumpFile logfile)
	{
		super(host,logfile);
	}

	/**
	 * Handle an Ethernet packet received on a specific interface.
	 * @param etherPacket the Ethernet packet that was received
	 * @param inIface the interface on which the packet was received
	 */
	public void handlePacket(Ethernet etherPacket, Iface inIface)
	{
		System.out.println("*** -> Received packet: " +
                etherPacket.toString().replace("\n", "\n\t"));
		MACAddress dest_mac = etherPacket.getDestinationMAC();
		MACAddress src_mac = etherPacket.getSourceMAC(); 
		
	}
}

class BridgeEntry {
	MACAddress MacAddr; // destination
	Iface interface; // interface to send the package to
	float TTL; // time left for this entry to live

	public BridgeEntry(MACAddress MacAddr,Iface interface){
		this.MacAddr = macAddress;
		this.interface = interface;
		TTL = MAX_TTL;
	}
}

class Table {
	BridgeEntry[] entries;

	void update_entries(MACAddress MAC, Iface interface){
		BridgeEntry b;
		
		for (int i = 0; i < entries.length; i ++){
			b = entries[i];
			if (b.macAddress.equals(MAC)){
				// found the entry with this mac address
				b.interface = interface;
				b.TTL = MAX_TTL;
				return;
			}
		}
		// didn't found entry in the table, append the new one
		BridgeEntry entry = new BridgeEntry(MAC, interface);
		temp = Arrays.copyOf(entries, entries.length + 1);
		temp[temp.length - 1] = entry;
		entries = temp;
	}

}