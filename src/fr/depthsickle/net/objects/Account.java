package fr.depthsickle.net.objects;

public class Account {

    private final String name;
    private String mode;
    private boolean toggle;

    public Account(String name, String mode, boolean toggle) {
        this.name = name;
        this.mode = mode;
        this.toggle = toggle;
    }

    public String getName() {
        return name;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isToggle() {
        return toggle;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }

}
