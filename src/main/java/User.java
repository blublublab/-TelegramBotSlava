public class User {
    private String name;
    private long ID;
    private int messagesCount;
    public User(String name, long ID) {
        this.name = name;
        this.ID = ID;
    }

    public User(String name, long ID, int messagesCount) {
        this.name = name;
        this.ID = ID;
        this.messagesCount = messagesCount;
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public int getMessagesCount() {
        return messagesCount;
    }

    public void setMessagesCount(int messagesCount) {
        this.messagesCount = messagesCount;
    }
}
