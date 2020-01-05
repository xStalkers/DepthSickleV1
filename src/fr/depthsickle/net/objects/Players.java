package fr.depthsickle.net.objects;

public class Players {

    private final String name;
    private boolean toggle;
    private int used;
    private String mod;

    public Players(String name, boolean toggle, int used, String mod) {
        this.name = name;
        this.toggle = toggle;
        this.used = used;
        this.mod = mod;
    }

    public String getName() {
        return name;
    }

    public boolean isToggle() {
        return toggle;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public String getMod() {
        return mod;
    }

    public void setMod(String mod) {
        this.mod = mod;
    }

}
