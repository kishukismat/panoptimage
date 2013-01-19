// This file is part of panoptimage.
//
// panoptimage is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// panoptimage is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with panoptimage.  If not, see <http://www.gnu.org/licenses/>

package org.fereor.panoptimage.util.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

public class WifiDiscovery extends AsyncTask<Void, Long, List<HotSite>> implements DiscoveryListener {
	/** list of ports to scan */
	private int[] ports;
	/** Executor for all async tasks to handle */
	private ExecutorService executor;
	/** DHCP information */
	private DhcpInfo dhcp;
	/** List of addesses found */
	List<HotSite> sites;
	/** Size of loop */
	private Long loopsize;

	/**
	 * Constructor
	 * 
	 * @param wifi wifi manager to gather network information
	 * @param p list of ports to scan
	 */
	public WifiDiscovery(WifiManager wifi, int poolsize, int... p) {
		this.ports = p;
		// Create the thread executor
		executor = Executors.newFixedThreadPool(poolsize);
		// Prepare DHCP info
		dhcp = wifi.getDhcpInfo();
		// Prepare List of available items
		sites = new ArrayList<HotSite>();
		// get max number of addresses
		byte[] nomask = intToInetBytes(~dhcp.netmask);
		long max = 0xffffffff & nomask[0] + 0xffffff & nomask[1] + 0xffff & nomask[2] + 0xff & nomask[3];
		loopsize = max;
		// init discovery
		for (int adi = 0; adi < max; adi++) {
			try {
				// Identify address to check
				int addr = (dhcp.ipAddress & dhcp.netmask) | (adi & 0xff) << 24 | (adi >> 8 & 0xff) << 16
						| (adi >> 16 & 0xff) << 8;
				InetAddress target = InetAddress.getByAddress(intToInetBytes(addr));
				DiscoveryTask t = new DiscoveryTask(this, ports);
				t.executeOnExecutor(executor, target);
			} catch (UnknownHostException e) {
				// Host is unknown : just ignore it
			}
		}
	}

	@Override
	protected List<HotSite> doInBackground(Void... params) {
		while (loopsize > 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}

		return sites;
	}

	@Override
	public void addressFound(List<HotSite> ad) {
		synchronized (loopsize) {
			loopsize--;
			if (ad != null) {
				sites.addAll(ad);
			}
		}
	}

	@Override
	protected void onPostExecute(List<HotSite> result) {
		super.onPostExecute(result);
		executor.shutdown();
		executor = null;
	}

	// -------------------------------------------
	// private methods
	// -------------------------------------------
	/**
	 * Convert an address to readable format
	 * 
	 * @param hostAddress
	 * @return
	 */
	private byte[] intToInetBytes(int hostAddress) {
		byte[] addressBytes = { (byte) (0xff & hostAddress), (byte) (0xff & (hostAddress >> 8)),
				(byte) (0xff & (hostAddress >> 16)), (byte) (0xff & (hostAddress >> 24)) };
		return addressBytes;
	}
}
