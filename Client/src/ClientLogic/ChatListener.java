package ClientLogic;

/**
 * Created by mlee43 on 2016-03-23.
 */

// TODO: Move to chat gui class
public interface ChatListener {

    void onClickSendMsg(int type, int destUser, String msg);

}
