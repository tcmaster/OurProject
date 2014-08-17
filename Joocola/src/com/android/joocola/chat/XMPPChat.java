package com.android.joocola.chat;

import java.util.Collection;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * XMPP的工具类，定义了聊天所需的各种方法
 * 
 * @author lixiaosong
 * 
 */
public class XMPPChat {
	/**
	 * 要连接的端口号
	 */
	private int PORT = 5222;
	/**
	 * 远程服务器地址
	 */
	private String REMOTE_HOST = "182.92.187.129";
	/**
	 * 服务名
	 */
	private String SERVICE_NAME = "talk.joocola.com";
	/**
	 * 本类的单例对象
	 */
	private static XMPPChat chatService;
	/**
	 * XMPP的连接
	 */
	private XMPPConnection connection;
	/**
	 * 心跳服务，开启的话不断发送心跳包，需及时关闭
	 */
	private HeartService service;
	/**
	 * 下面几个字段代表本用户当前的几个状态 ONLINE，QME，BUSY，LEAVE，INVISIBLE,OFFLINE
	 */
	public static final int ONLINE = 0;
	public static final int QME = 1;
	public static final int BUSY = 2;
	public static final int LEAVE = 3;
	public static final int INVISIBLE = 4;
	public static final int OFFLINE = 5;
	static {
		try {
			Class.forName("org.jivesoftware.smack.ReconnectionManager");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	synchronized public static XMPPChat getInstance() {
		if (chatService == null)
			chatService = new XMPPChat();
		return chatService;
	}

	private XMPPChat() {
		getConnection();
	}

	public XMPPConnection getConnection() {
		if (connection == null)
			openConnection();
		return connection;
	}

	private boolean openConnection() {
		if (null == connection || !connection.isAuthenticated()) {
			try {
				ConnectionConfiguration config = new ConnectionConfiguration(
						REMOTE_HOST, PORT);
				XMPPConnection.DEBUG_ENABLED = true;
				config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
				config.setSendPresence(false); // 状态设为离线，目的为了取离线消息
				config.setReconnectionAllowed(true);
				config.setDebuggerEnabled(true);
				Log.v("lixiaosong", config.getServiceName());
				Log.v("lixiaosong", config.getHostAddresses().toString());
				connection = new XMPPConnection(config);
				connection.connect();
				configureConnection(ProviderManager.getInstance());
				return true;
			} catch (XMPPException e) {
				e.printStackTrace();
			}
			return false;
		}
		return false;
	}

	/**
	 * 
	 * 关闭连接
	 */
	public void closeConnection() {
		if (connection != null) {
			if (connection.isConnected())
				connection.disconnect();
			connection = null;
		}
	}

	/**
	 * 注册
	 * 
	 * @param account
	 *            注册的账号
	 * @param password
	 *            注册的密码
	 * @return 1注册成功，0服务器没有返回结果，2账号已经被使用，3注册失败
	 */
	public String register(String account, String password, String name) {
		if (connection == null)
			return "0";
		Registration registration = new Registration();
		registration.setType(IQ.Type.SET);
		registration.setUsername(account);
		registration.setPassword(password);
		registration.setTo(connection.getServiceName());
		registration.addAttribute("android", "geolo_createUser_android");
		registration.addAttribute("name", name);
		PacketFilter filter = new AndFilter(new PacketIDFilter(
				registration.getPacketID()), new PacketTypeFilter(IQ.class));
		PacketCollector collector = connection.createPacketCollector(filter);
		connection.sendPacket(registration);
		IQ result = (IQ) collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());
		collector.cancel();
		if (result == null) {
			Log.v("lixiaosong", "服务器无响应");
			return "0";
		} else if (result.getType() == IQ.Type.RESULT) {
			Log.v("lixiaosong", "注册成功");
			return "1";
		} else if (result.getType() == IQ.Type.ERROR) {
			if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
				Log.v("lixiaosong", "该账号已被注册");
				return "2";
			} else {
				Log.v("lixiaosong", "注册失败");
				return "3";
			}
		}
		return "1";
	}

	/**
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @return true代表登录成功，false代表登录失败
	 */
	public boolean login(String userName, String password) {
		if (connection != null) {
			try {
				connection.login(userName, password);
				return true;
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 修改用户状态
	 * 
	 * @param code
	 *            状态有ONLINE，QME，BUSY，LEAVE，INVISIBLE,OFFLINE
	 */
	public void setPresence(int code) {
		if (connection == null)
			return;
		Presence presence;
		switch (code) {
		case ONLINE:
			presence = new Presence(Presence.Type.available);
			connection.sendPacket(presence);
			Log.v("state", "设置在线");
			break;
		case QME:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.chat);
			connection.sendPacket(presence);
			Log.v("state", "设置Q我吧");
			System.out.println(presence.toXML());
			break;
		case BUSY:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.dnd);
			connection.sendPacket(presence);
			Log.v("state", "设置忙碌");
			System.out.println(presence.toXML());
			break;
		case LEAVE:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.away);
			connection.sendPacket(presence);
			Log.v("state", "设置离开");
			System.out.println(presence.toXML());
			break;
		case INVISIBLE:
			Roster roster = connection.getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			for (RosterEntry entry : entries) {
				presence = new Presence(Presence.Type.unavailable);
				presence.setPacketID(Packet.ID_NOT_AVAILABLE);
				presence.setFrom(connection.getUser());
				presence.setTo(entry.getUser());
				connection.sendPacket(presence);
				System.out.println(presence.toXML());
			}
			// 向同一用户的其他客户端发送隐身状态
			presence = new Presence(Presence.Type.unavailable);
			presence.setPacketID(Packet.ID_NOT_AVAILABLE);
			presence.setFrom(connection.getUser());
			presence.setTo(StringUtils.parseBareAddress(connection.getUser()));
			connection.sendPacket(presence);
			Log.v("state", "设置隐身");
			break;
		case OFFLINE:
			presence = new Presence(Presence.Type.unavailable);
			connection.sendPacket(presence);
			Log.v("state", "设置离线");
			break;
		default:
			break;
		}
	}

	/**
	 * 加入providers的函数 ASmack在/META-INF缺少一个smack.providers 文件
	 * 
	 * @param pm
	 */
	public void configureConnection(ProviderManager pm) {

		// Private Data Storage
		pm.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());

		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time",
					Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (ClassNotFoundException e) {
			Log.w("TestClient",
					"Can't load class for org.jivesoftware.smackx.packet.Time");
		}

		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());

		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());

		// Chat State
		pm.addExtensionProvider("active",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("composing",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("paused",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("inactive",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("gone",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
				new XHTMLExtensionProvider());

		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());

		// Service Discovery # Items
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());

		// Service Discovery # Info
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());

		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());

		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
				new MUCUserProvider());

		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
				new MUCAdminProvider());

		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
				new MUCOwnerProvider());

		// Delayed Delivery
		pm.addExtensionProvider("x", "jabber:x:delay",
				new DelayInformationProvider());

		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version",
					Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			// Not sure what's happening here.
		}

		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
				new OfflineMessageRequest.Provider());

		// Offline Message Indicator
		pm.addExtensionProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageInfo.Provider());

		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());

		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());

		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());

		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses",
				"http://jabber.org/protocol/address",
				new MultipleAddressesProvider());

		// FileTransfer
		pm.addIQProvider("si", "http://jabber.org/protocol/si",
				new StreamInitiationProvider());

		pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
				new BytestreamsProvider());

		// Privacy
		pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
		pm.addIQProvider("command", "http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider());
		pm.addExtensionProvider("malformed-action",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.MalformedActionError());
		pm.addExtensionProvider("bad-locale",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadLocaleError());
		pm.addExtensionProvider("bad-payload",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadPayloadError());
		pm.addExtensionProvider("bad-sessionid",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadSessionIDError());
		pm.addExtensionProvider("session-expired",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.SessionExpiredError());
	}

	/**
	 * 开启心跳服务
	 */
	public void startHeartService(Context context) {
		Intent service = new Intent(context, HeartService.class);
		context.startService(service);
	}

	public void stopHeartService(Context context) {
		context.stopService(new Intent(context, HeartService.class));
	}
}
