  
/**
 * PacketWrapper - ProtocolLib wrappers for Minecraft packets
 * Copyright (C) dmulloy2 <http://dmulloy2.net>
 * Copyright (C) Kristian S. Strangeland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPacketOutTransaction extends AbstractPacket {
	public static final PacketType TYPE =
			PacketType.Play.Server.TRANSACTION;

	private int windowId;
	private short actionNumber;
	private boolean accepted;
	private boolean isListening = false;

	public WrapperPacketOutTransaction(final int windowId, final short actionNumber, final boolean accepted) {
		super(new PacketContainer(TYPE), TYPE);
		this.windowId = windowId;
		this.actionNumber = actionNumber;
		this.accepted = accepted;
	}

	public WrapperPacketOutTransaction(PacketContainer packet) {
		super(packet, TYPE);
		isListening = true;
	}

	/**
	 * Get the Window ID.
	 *
	 * @return Get Window ID
	 */
	public int getWindowId() {
		if (isListening) {
			return handle.getIntegers().read(0);
		} else {
			return windowId;
		}
	}

	/**
	 * Get the action number.
	 *
	 * @return Get Action Number
	 */
	public short getActionNumber() {
		if (isListening) {
			return handle.getShorts().read(0);
		} else {
			return actionNumber;
		}
	}
	/**
	 * Has the transaction packet been accepted?
	 *
	 * @return Is Accepted
	 */
	public boolean isAccepted() {
		if (isListening) {
			return handle.getBooleans().read(0);
		} else {
			return accepted;
		}
	}

	/**
	 * Retrieve entity ID.
	 * 
	 * @return The current EID
	 */

}