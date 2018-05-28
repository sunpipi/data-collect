package topic.pipi;

import topic.pipi.context.TaskHub;
import topic.pipi.db.MysqlConnector;

import java.util.ArrayList;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        MysqlConnector.init();
        List<String> filepathList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            filepathList.add("2018-01-01.csv");
        }

        TaskHub.getInstance(4, 3).execute(filepathList);

    }
}
