package com.bol.team5f.lazybot;

import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;

@Slf4j
public class Xmpp {
    private final Config config;

    private XMPPTCPConnection connection;
    private MultiUserChat muc;

    public Xmpp(final Config config) {
        this.config = config;
    }

    public void connect() throws Exception {
        XMPPTCPConnectionConfiguration connectionConfig = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(config.getUser(), config.getPassword())
                .setHost(config.getHost())
                .setPort(config.getPort())
                .setServiceName(config.getHost())
                .build();
        connection = new XMPPTCPConnection(connectionConfig);

        connection.addConnectionListener(new ConnectionListener() {
            @Override
            public void reconnectionSuccessful() {
                log.info("Successfully reconnected to the XMPP server.");

            }

            @Override
            public void reconnectionFailed(Exception arg0) {
                log.warn("Failed to reconnect to the XMPP server.");
            }

            @Override
            public void reconnectingIn(int seconds) {
                log.info("Reconnecting in " + seconds + " seconds.");
            }

            @Override
            public void connectionClosedOnError(Exception arg0) {
                log.warn("Connection to XMPP server was lost.");
            }

            @Override
            public void connected(XMPPConnection xmppConnection) {
                log.info("connected");
            }

            @Override
            public void authenticated(XMPPConnection xmppConnection, boolean b) {
                log.info("authenticated");
            }

            @Override
            public void connectionClosed() {
                log.info("XMPP connection was closed.");

            }
        });
        connection.connect().login();

        MultiUserChatManager mucm = MultiUserChatManager.getInstanceFor(connection);
        muc = mucm.getMultiUserChat(config.getRoom());
        final DiscussionHistory history = new DiscussionHistory();
        history.setMaxStanzas(1);
        muc.join(config.getNickname(), config.getPassword(), history, 5000);
    }

    MultiUserChat getMuc() {
        return muc;
    }

    void disconnect() {
        connection.disconnect();
    }
}
