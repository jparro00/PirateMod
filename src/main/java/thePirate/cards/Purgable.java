package thePirate.cards;

public interface Purgable {


    public void setPurge(boolean purge);
    public boolean getPurge();

    public boolean queuedForPurge();
    public void setQueuedForPurge(boolean queuedForPurge);


}
