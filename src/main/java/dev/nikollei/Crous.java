package dev.nikollei;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class Crous {

    public static MessageEmbed getMenu() throws IOException {
        String url = "https://multi.univ-lorraine.fr/graphql";
        String graphQLQuery = "{\"operationName\":\"crous\",\"variables\":{},\"query\":\"query crous {\\n  restos {\\n    title\\n    thumbnail_url\\n    image_url\\n    short_desc\\n    lat\\n    lon\\n    menus {\\n      date\\n      meal {\\n        name\\n        foodcategory {\\n          name\\n          dishes {\\n            name\\n            __typename\\n          }\\n          __typename\\n        }\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n\"}";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(graphQLQuery.getBytes("UTF-8"));
            outputStream.flush();
        }

        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }

        JSONObject response = new JSONObject(stringBuilder.toString());
        JSONArray restos = response.getJSONObject("data").getJSONArray("restos");

        String ruName = "Resto U' Médreville";

        //Builder
        EmbedBuilder embedBuilder = new EmbedBuilder();

        //Update builder
        embedBuilder.addField("Localisation", ruName, false);
        embedBuilder.setFooter(ruName);
        embedBuilder.setColor(Color.red);
        
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date todayDate = new Date();
        String date = sFormat.format(todayDate);
        int findRuVariable = 0;
        
        for (int i = 0; i < restos.length(); i++) {
            if (restos.getJSONObject(i).getString("title").equals(ruName)) {
                findRuVariable = i;
                break;
            }
        }

        JSONObject ruMedreville = response.getJSONObject("data").getJSONArray("restos").getJSONObject(findRuVariable);

        //Update builder
        embedBuilder.setThumbnail(ruMedreville.get("thumbnail_url").toString());
        
        JSONArray menu = ruMedreville.getJSONArray("menus");

        //Find today's menu
        int findTodayMenuVariable = 0;
        boolean isTodayMenu = false;
        for (int i = 0; i < menu.length(); i++) {
            if (menu.getJSONObject(i).getString("date").equals(date)) {
                isTodayMenu = true;
                findTodayMenuVariable = i;

                //Update builder
                embedBuilder.addField("Date", menu.getJSONObject(i).getString("date"), false);

                break;
            }
        }

        //Today's menu not found return
        if (!isTodayMenu) {
            EmbedBuilder notFoundEmbedBuilder = new EmbedBuilder();
            notFoundEmbedBuilder.setTitle("Résultat non trouvé");
            notFoundEmbedBuilder.setColor(Color.RED);
            notFoundEmbedBuilder.setDescription("Le menu n'a pas encore été mis en ligne");
            notFoundEmbedBuilder.setFooter("Resto U' Médreville");
            return notFoundEmbedBuilder.build();
        }


        JSONObject todayMenu = menu.getJSONObject(findTodayMenuVariable);

        JSONArray todayFood = todayMenu.getJSONArray("meal").getJSONObject(0).getJSONArray("foodcategory").getJSONObject(0).getJSONArray("dishes");

        String menuString = "";
        for (int i = 0; i < todayFood.length(); i++) {
            String line = todayFood.getJSONObject(i).get("name").toString();
            if (!Objects.equals(line, "")){
                menuString+= ":white_small_square: "+line+"\r";
            }
        }

        //Update builder
        embedBuilder.addField("Menu", menuString, false);

        connection.disconnect();

        return embedBuilder.build();






    }
}


