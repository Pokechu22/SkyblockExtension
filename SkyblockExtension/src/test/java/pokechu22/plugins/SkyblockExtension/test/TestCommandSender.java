package pokechu22.plugins.SkyblockExtension.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

/**
 * A dummy CommandSender.
 *
 * @author Pokechu22
 *
 */
public class TestCommandSender implements CommandSender {
	
	private String name;
	private List<String> messagesRecieved;
	
	public TestCommandSender(String name) {
		this.name = name;
		messagesRecieved = new ArrayList<String>();
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name,
			boolean value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name,
			boolean value, int ticks) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasPermission(String name) {
		return true;
	}

	@Override
	public boolean hasPermission(Permission perm) {
		return true;
	}

	@Override
	public boolean isPermissionSet(String name) {
		return false;
	}

	@Override
	public boolean isPermissionSet(Permission perm) {
		return false;
	}

	@Override
	public void recalculatePermissions() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isOp() {
		return true;
	}

	@Override
	public void setOp(boolean value) {
		//Don't do anything.
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Server getServer() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void sendMessage(String message) {
		messagesRecieved.add(message);
	}

	@Override
	public void sendMessage(String[] messages) {
		for (String message : messages) {
			this.sendMessage(message);
		}
	}

	public List<String> getMessages() {
		return new ArrayList<String>(messagesRecieved);
	}
}
