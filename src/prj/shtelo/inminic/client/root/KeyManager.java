package prj.shtelo.inminic.client.root;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {
    private int count = 256;

    private boolean[] keys = new boolean[count], lastKeys = new boolean[count];
    private boolean[] move = new boolean[2];
    private boolean moveStop = false;

    private boolean zoomUp = false, zoomDown = false;

    public void tick() {
        move[0] = keys[KeyEvent.VK_A];
        move[1] = keys[KeyEvent.VK_D];

        boolean[] previousKeys = lastKeys.clone();

        zoomDown = !previousKeys[KeyEvent.VK_OPEN_BRACKET] && keys[KeyEvent.VK_OPEN_BRACKET];
        zoomUp = !previousKeys[KeyEvent.VK_CLOSE_BRACKET] && keys[KeyEvent.VK_CLOSE_BRACKET];

        moveStop = previousKeys[KeyEvent.VK_A] && !keys[KeyEvent.VK_A] || previousKeys[KeyEvent.VK_D] && !keys[KeyEvent.VK_D];

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

    public boolean[] getKeys() {
        return keys;
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
}
