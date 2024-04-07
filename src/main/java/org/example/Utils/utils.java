package org.example.Utils;

import jade.core.AID;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class utils {
    public static List<AID> convertToAIDList(String input) {
        List<AID> aidList = new ArrayList<>();
        Pattern pattern = Pattern.compile(":name \"(.*?)\"");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String agentName = matcher.group(1);
            AID aid = new AID(agentName, AID.ISGUID);
            aidList.add(aid);
        }
        return aidList;
    }
}
