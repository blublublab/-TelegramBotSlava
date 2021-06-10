package tgbot.db;

public class ResultConstructor {

    private String user;
    private boolean isNewToList;
    private String command;
    private int hoursToNext;

    public boolean isNewToList() {
        return isNewToList;
    }

    public void setNewToList(boolean newToList) {
        isNewToList = newToList;
    }

    public ResultConstructor(String user, String command, int hoursToNext, boolean isNewToList) {
        this.user = user;
        this.command = command;
        this.hoursToNext = hoursToNext;
        this.isNewToList = isNewToList;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getTimeToNext() {
        return hoursToNext;
    }

    public void setTimeToNext(int hoursToNext) {
        this.hoursToNext = hoursToNext;
    }
}
