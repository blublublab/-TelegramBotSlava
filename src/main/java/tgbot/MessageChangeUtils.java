package tgbot;

import java.io.UnsupportedEncodingException;
import java.util.*;

public  class MessageChangeUtils {



    public static String transliterate( String messageToTransl){
        StringBuilder sbTransliterate = new StringBuilder();
        for (int i = 0; i < messageToTransl.length(); i++) {
            char ch = Character.toLowerCase(messageToTransl.charAt(i));
            if (transliterateMap.containsKey(ch)) {
                sbTransliterate.append(transliterateMap.get(ch));
            } else {
                sbTransliterate.append(ch); //char
            }

        }
      return sbTransliterate.toString();

    };

    private static Map<Character, String> transliterateMap = new HashMap<Character, String>() {
        {
            put('а', "a");
            put('б', "b");
            put('в', "v");
            put('г', "g");
            put('д', "d");
            put('е', "e");
            put('ж', "zh");
            put('з', "z");
            put('и', "i");
            put('й', "j");
            put('к', "k");
            put('л', "l");
            put('м', "m");
            put('н', "n");
            put('о', "o");
            put('п', "p");
            put('р', "r");
            put('с', "s");
            put('т', "t");
            put('у', "u");
            put('ф', "f");
            put('х', "x");
            put('ц', "c");
            put('ч', "ch");
            put('ш', "sh");
            put('щ', "shh");
            put('ы', "y");
            put('э', "e");
            put('ю', "yu");
            put('я', "ya");
        }

    };
    public static String cropToRequest(String message) throws UnsupportedEncodingException {
        List<String> wordsArrayList = new ArrayList<>(Arrays.asList(message.split(" ")));
        wordsArrayList.remove(0);
        wordsArrayList.remove(0);
        if(message.contains("активность")){
            wordsArrayList.remove(2);
        }
        StringBuilder stringBuilderRequest = new StringBuilder();
        for (int i = 0, j = wordsArrayList.size(); i < wordsArrayList.size(); i++) {

            stringBuilderRequest.append(wordsArrayList.get(i));

               if (wordsArrayList.size() > (j - (i + 1))) {
                   j--;
                   stringBuilderRequest.append(" ");
               }
           }
         ;

          stringBuilderRequest.deleteCharAt(stringBuilderRequest.length()- 1);
          String result = stringBuilderRequest.toString().replaceAll("[^( [A-Z])^[А-ЯЁ][-А-яЁё]\\w+\\s]", "");
        return result;
    }


}

