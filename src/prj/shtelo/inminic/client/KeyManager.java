package prj.shtelo.inminic.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {
    private boolean[] keys = new boolean[256];
    private boolean[] move = new boolean[4];

    void tick() {
        move[0] = keys[KeyEvent.VK_W];
        move[1] = keys[KeyEvent.VK_A];
        move[2] = keys[KeyEvent.VK_S];
        move[3] = keys[KeyEvent.VK_D];
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    public boolean[] getMove() {
        return move;
    }
}
