package edu.ut.cs.sdn.vnet.sw;

import net.floodlightcontroller.packet.Ethernet;
import edu.ut.cs.sdn.vnet.Device;
import edu.ut.cs.sdn.vnet.DumpFile;
import edu.ut.cs.sdn.vnet.Iface;
import java.util.*;

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

	Table table; 
	public Switch(String host, DumpFile logfile)
	{
		super(host,logfile);
		table = new Table();
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
		MACAddress dst_mac = etherPacket.getDestinationMAC();
		MACAddress src_mac = etherPacket.getSourceMAC(); 
		table.sweep();
		table.update_entries(src_mac, inIface);

		if (table.has_entry(dst_mac)){
			BridgeEntry b = table.get_entry(dst_mac);
		}
		else {
			// broadcast
			for ((Map.Entry<String,Iface> entry : interfaces.entrySet(){
				if(!entry.getValue().toSrting().equals(inIface.toString())){
					sendPacket(etherPacket, entry.getKey());
				}
			}
		}
	}
}

class BridgeEntry {
	MACAddress MacAddr; // destination
	Iface interface; // interface to send the package to
	int update_time;

	public BridgeEntry(MACAddress MacAddr,Iface interface){
		this.MacAddr = macAddress;
		this.interface = interface;
		update_time = ((int) System.currentTimeMillis() / 1000);
	}
}

class Table {
	BridgeEntry[] entries;

	boolean has_entry(MACAddress dest){
		for (int i = 0; i < entries.length; i ++){
			BridgeEntry b = entries[i];
			if (b.MacAddr.equals(dest)){
				return true;
			}
		}
		return false;
	}

	void update_entries(MACAddress MAC, Iface interface){
		BridgeEntry b;
		for (int i = 0; i < entries.length; i ++){
			b = entries[i];
			if (b.macAddress.equals(MAC)){
				// found the entry with this mac address
				b.interface = interface;
				b.update_time = ((int) System.currentTimeMillis() / 1000);
				return;
			}
		}
		// didn't found entry in the table, append the new one
		BridgeEntry entry = new BridgeEntry(MAC, interface);
		temp = Arrays.copyOf(entries, entries.length + 1);
		temp[temp.length - 1] = entry;
		entries = Arrays.copyOf(temp, temp.length);
	}

	BridgeEntry get_entry(MACAddress MacAddr){
		BridgeEntry b;
		for (int i = 0; i < entries.length; i ++){
			b = entries[i];
			if (b.macAddress.equals(MacAddr)){
				return b;
			}
		}
		return b;
	}

	void sweep(){
		Boolean done = false;
		while(!done){
			if (i < entries.length){
				BridgeEntry b = entries[i];
				if ((((int) System.currentTimeMillis() / 1000) - b.update_time) > = 15){
					remove_element(i);
				}
			}
			else {
				done = true;
			}
			i ++;
		}
	}

	void remove_element(int indx){
		BridgeEntry[] temp = new BridgeEntry[entries.length - 1]]
		i = 0;
		x = 0;
		while (i < temp.length){
			if (x == indx){
				x++;
			}
			temp[i] = entries[x];
			x++;
			i ++;
		}
		for (int i = 0; i < entries.length; i ++){
			if (i != indx){
				temp[i] = entries[i];
			}
			else {
				if ( i + 1 < entries.length){
					temp[i] = entries[i + 1];
				}
			}
		}
		entries = Arrays.copyOf(temp, temp.length);
	}

}