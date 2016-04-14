package se.dewire.studs2016template;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by niro on 03/03/16.
 *
 * THIS CLASS IS NOT MEANT TO BE MODIFIED IN ANY WAY!
 *
 * The purpose of this class is only to provide the connection
 * to the contest server for the contest participants.
 *
 */

public abstract class DewireContestConnection {

    private static String SERVER_ADDR = "studs16.dewi.re";
    private static int PORT = 1337;

    public final String name;
    private WebSocketClient mWebSocketClient;

    public DewireContestConnection(String name){
        this.name = name;
        connectWebSocket();
    }

    private void connectWebSocket() {
        if(mWebSocketClient==null || !mWebSocketClient.getConnection().isOpen()){
            URI uri;
            try {
                uri = new URI("ws://"+SERVER_ADDR+":"+PORT);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return;
            }

            mWebSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    Log.i("Websocket", "Opened");
                    mWebSocketClient.send("["+name+"§register§.]");
                    onConnectionOpened();
                }

                @Override
                public void onMessage(String s) {}

                @Override
                public void onClose(int i, String s, boolean b) {
                    onConnectionClosed();
                }

                @Override
                public void onError(Exception e) {
                    throw new IllegalStateException(e.getMessage());
                }
            };
        }
        mWebSocketClient.connect();
    }

    /**
     * This method is used to update the text on the server as your user. Please note
     * that no special symbols (/\<>@#§&[]{}) are allowed, since this are not part of
     * the contest and could create problems with the server.
     *
     * @param str string to type as your user
     */
    public void sendMessage(String str) {
        if(mWebSocketClient!=null && mWebSocketClient.getConnection().isOpen()){
            mWebSocketClient.send("["+name+"§updatetext§"+str+"]");
        }else{
            throw new IllegalStateException("You can't upload text when unconnected!");
        }
    }

    /**
     * Returns true if the server is initialized and in the open ready state.
     * @return boolean connected
     */
    public boolean isConnected(){
        if(mWebSocketClient==null){
            return false;
        }else if(mWebSocketClient.getReadyState() == WebSocket.READYSTATE.OPEN){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Method used to manually disconnect the device from the server.
     */
    public void disconnect(){
        mWebSocketClient.close();
    }

    /**
     * Provide method to be called when the connection to the server opens
     */
    public abstract void onConnectionClosed();

    /**
     * Provide method to be called when the connection to the server closes
     */
    public abstract void onConnectionOpened();
}
