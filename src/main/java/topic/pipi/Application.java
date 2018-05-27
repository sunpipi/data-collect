package topic.pipi;

import topic.pipi.context.TaskHub;

import java.util.ArrayList;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        List<String> filepathList = new ArrayList<>();
        for (int i = 0; i < 180; i++) {
            filepathList.add("file" + i);
        }

        TaskHub.getInstance(4, 3).execute(filepathList);

    }
}
