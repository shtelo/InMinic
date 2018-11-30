package prj.shtelo.inminic.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {
    private int count = 256;

    private boolean[] keys = new boolean[count], lastKeys = new boolean[count];
    private boolean[] move = new boolean[2];

    private boolean jump = false;
    private boolean zoomUp = false, zoomDown = false;

    void tick() {
        move[0] = keys[KeyEvent.VK_A];
        move[1] = keys[KeyEvent.VK_D];

        boolean[] previousKeys = lastKeys.clone();

        jump = !previousKeys[KeyEvent.VK_SPACE] && keys[KeyEvent.VK_SPACE];
        zoomDown = !previousKeys[KeyEvent.VK_OPEN_BRACKET] && keys[KeyEvent.VK_OPEN_BRACKET];
        zoomUp = !previousKeys[KeyEvent.VK_CLOSE_BRACKET] && keys[KeyEvent.VK_CLOSE_BRACKET];

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

    boolean isJump() {
        return jump;
    }

    public boolean isZoomUp() {
        return zoomUp;
    }

    public boolean isZoomDown() {
        return zoomDown;
    }
}
