package config;

import me.maxouxax.multi4j.MultiClient;
import me.maxouxax.multi4j.MultiConfig;
import me.maxouxax.multi4j.exceptions.MultiLoginException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Crous {

    public static MessageEmbed getMenu() throws MultiLoginException, IOException, URISyntaxException, InterruptedException {


        // First step is to create the config instance:
// You can either create a config with hard coded credentials
        MultiConfig config = new MultiConfig();
        config.setUsername("bernar424u");
        config.setPassword("Nicolas200305*");

// Then you can create a client using the builder
        MultiClient multiClient = new MultiClient.Builder().withMultiConfig(config).build();

        multiClient.connect();
        // You can either make a GQL Request manually by

// creating the GQL JSON object
        JSONObject jsonObject = new JSONObject("{\"operationName\":\"crous\",\"variables\":{},\"query\":\"query crous {\\n  restos {\\n    title\\n    thumbnail_url\\n    image_url\\n    short_desc\\n    lat\\n    lon\\n    menus {\\n      date\\n      meal {\\n        name\\n        foodcategory {\\n          name\\n          dishes {\\n            name\\n            __typename\\n          }\\n          __typename\\n        }\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n\"}");

// and getting a JSONObject as a response
        JSONObject response = multiClient.makeGQLRequest(jsonObject);

//        System.out.println(response.toString());

        JSONArray fienarray = response.getJSONObject("data").getJSONArray("restos");


        //Titre
//        for (int i = 0; i < fienarray.length(); i++) {
//            JSONObject fi = response.getJSONObject("data").getJSONArray("restos").getJSONObject(i);
//            System.out.println(fi.get("title"));
//            System.out.println(fi.get("thumbnail_url"));
//        }
        String crous = "Resto U' Médreville";

        EmbedBuilder ailfinal = new EmbedBuilder();
        ailfinal.addField("Localisation", crous, false);

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        Date datel = new Date();
        String date = s.format(datel);
        int finaly = 0;

        //Menu
        JSONArray fim = response.getJSONObject("data").getJSONArray("restos");
        for (int i = 0; i < fim.length(); i++) {
            if (fim.getJSONObject(i).getString("title").equals(crous)) {
                System.out.println(fim.getJSONObject(i).getString("title"));
                finaly = i;
                break;
            }
        }

        JSONObject fi = response.getJSONObject("data").getJSONArray("restos").getJSONObject(finaly);
        System.out.println(fi.get("title"));
        System.out.println(fi.get("thumbnail_url"));
        ailfinal.setThumbnail(fi.get("thumbnail_url").toString());
        JSONArray fiaki = fi.getJSONArray("menus");
//        JSONObject fia = fi.getJSONArray("menus").getJSONObject(0);
        //Trouver date
        int finali = 0;
        System.out.println(fiaki.length());
        for (int i = 0; i < fiaki.length(); i++) {
            System.out.println(fiaki.getJSONObject(i).getString("date"));
            if (fiaki.getJSONObject(i).getString("date").equals(date)) {
                ailfinal.addField("Date", fiaki.getJSONObject(i).getString("date"), false);
                System.out.println("trouve");
                finali = i;
                System.out.println(finali);
                break;
            }
        }
        JSONObject fia = fi.getJSONArray("menus").getJSONObject(finali);

        //Deja date
        JSONObject fiak = fia.getJSONArray("meal").getJSONObject(0);
        JSONObject fiakr = fiak.getJSONArray("foodcategory").getJSONObject(0);
        JSONArray fiakrarray = fiakr.getJSONArray("dishes");
        String sli = "";
        for (int i = 0; i < fiakrarray.length(); i++) {
            String temp = fiakrarray.getJSONObject(i).getString("name");
            System.out.println(temp);
            sli += temp+" - ";
        }
        String strNew = sli.substring(0, sli.length()-2);
        ailfinal.addField("Repas", strNew, false);
        ailfinal.setColor(Color.red);

        return ailfinal.build();


    }
}