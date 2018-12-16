package prj.shtelo.inminic.client.state;

public class StateManager {
    private State state;

    public StateManager(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
