package prj.shtelo.inminic.client.root;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {
    private int count = 256;

    private boolean[] keys = new boolean[count], lastKeys = new boolean[count];

    private String queue = "";

    private boolean shift = false;

    private boolean[] move = new boolean[2];
    private boolean zoomUp = false, zoomDown = false, resetZoom = false;
    private boolean toggleHUD = false;
    private boolean jumping = false;
    private boolean testing = false;
    private boolean toggleChatting = false;
    private boolean moveStop = false;
    private boolean backspace = false;

    public void tick() {
        shift = keys[KeyEvent.VK_SHIFT];

        move[0] = keys[KeyEvent.VK_A];
        move[1] = keys[KeyEvent.VK_D];
        zoomDown = keys[KeyEvent.VK_OPEN_BRACKET];
        zoomUp = keys[KeyEvent.VK_CLOSE_BRACKET];
        resetZoom = !lastKeys[KeyEvent.VK_EQUALS] && keys[KeyEvent.VK_EQUALS];
        toggleHUD = !lastKeys[KeyEvent.VK_BACK_QUOTE] && keys[KeyEvent.VK_BACK_QUOTE];
        jumping = keys[KeyEvent.VK_SPACE];
        testing = keys[KeyEvent.VK_P];
        toggleChatting = !lastKeys[KeyEvent.VK_ENTER] && keys[KeyEvent.VK_ENTER];
        backspace = !lastKeys[KeyEvent.VK_BACK_SPACE] && keys[KeyEvent.VK_BACK_SPACE];

        moveStop = lastKeys[KeyEvent.VK_A] && !keys[KeyEvent.VK_A] || lastKeys[KeyEvent.VK_D] && !keys[KeyEvent.VK_D];

        lastKeys = keys.clone();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            keys[e.getKeyCode()] = true;
            if (0x20 <= e.getKeyChar() && e.getKeyChar() < 65535)
                queue += e.getKeyChar();
        } catch (ArrayIndexOutOfBoundsException ignored) {}
    }

    @Override
    public void keyReleased(KeyEvent e) {
        try {
            keys[e.getKeyCode()] = false;
        } catch (ArrayIndexOutOfBoundsException ignored) {}
    }

    public boolean[] getMove() {
        return move;
    }

    public boolean isMoveStop() {
        return moveStop;
    }

    public boolean isZoomUp() {
        return zoomUp;
    }

    public boolean isZoomDown() {
        return zoomDown;
    }

    public boolean isResetZoom() {
        return resetZoom;
    }

    public boolean isToggleHUD() {
        return toggleHUD;
    }

    public boolean isJumping() {
        return jumping;
    }

    public boolean isTesting() {
        return testing;
    }

    public boolean isToggleChatting() {
        return toggleChatting;
    }

    public boolean isBackspace() {
        return backspace;
    }

    public boolean isShift() {
        return shift;
    }

    public String getQueue() {
        String queue = this.queue;
        this.queue = "";
        return queue;
    }

    public void resetQueue() {
        queue = "";
    }
}
