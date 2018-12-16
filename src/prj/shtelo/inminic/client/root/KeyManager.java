package prj.shtelo.inminic.client.root;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {
    private int count = 256;

    private boolean[] keys = new boolean[count], lastKeys = new boolean[count];
    private boolean[] move = new boolean[2];
    private boolean moveStop = false;

    private boolean zoomUp = false, zoomDown = false, resetZoom = false;
    private boolean toggleHUD = false;
    private boolean jumping = false;
    private boolean testing = false;

    public void tick() {
        move[0] = keys[KeyEvent.VK_A];
        move[1] = keys[KeyEvent.VK_D];

        zoomDown = keys[KeyEvent.VK_OPEN_BRACKET];
        zoomUp = keys[KeyEvent.VK_CLOSE_BRACKET];
        resetZoom = !lastKeys[KeyEvent.VK_EQUALS] && keys[KeyEvent.VK_EQUALS];
        toggleHUD = !lastKeys[KeyEvent.VK_BACK_QUOTE] && keys[KeyEvent.VK_BACK_QUOTE];
        jumping = keys[KeyEvent.VK_SPACE];
        testing = keys[KeyEvent.VK_P];

        moveStop = lastKeys[KeyEvent.VK_A] && !keys[KeyEvent.VK_A] || lastKeys[KeyEvent.VK_D] && !keys[KeyEvent.VK_D];

        lastKeys = keys.clone();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            keys[e.getKeyCode()] = true;
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
}
