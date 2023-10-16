package config;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.Normalizer;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Crous {

    public static String lowerAccent(String str) {
        String norma = Normalizer.normalize(str, Normalizer.Form.NFD);
        norma = norma.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        System.out.println(norma.toLowerCase());
        return norma.toLowerCase();
    }

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

        StringBuilder rp = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                rp.append(line);
            }
        }

        JSONObject response = new JSONObject(rp.toString());
        JSONArray fienarray = response.getJSONObject("data").getJSONArray("restos");

        String crous = "Resto U' Médreville";
        EmbedBuilder ailfinal = new EmbedBuilder();
        EmbedBuilder ailfinalerror = new EmbedBuilder();
        ailfinal.addField("Localisation", crous, false);
        ailfinalerror.addField("Localisation", crous, false);

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        Date datel = new Date();
        String date = s.format(datel);
        int finaly = 0;

        //Menu
        JSONArray fim = response.getJSONObject("data").getJSONArray("restos");
        for (int i = 0; i < fim.length(); i++) {
            if (fim.getJSONObject(i).getString("title").equals(crous)) {
//                System.out.println(fim.getJSONObject(i).getString("title"));
                finaly = i;
                break;
            }
        }

        JSONObject fi = response.getJSONObject("data").getJSONArray("restos").getJSONObject(finaly);
//        System.out.println(fi.get("title"));
//        System.out.println(fi.get("thumbnail_url"));
        ailfinal.setFooter("Resto U' Médreville");
        ailfinalerror.setFooter("Resto U' Médreville");
        ailfinal.setThumbnail(fi.get("thumbnail_url").toString());
        ailfinalerror.setThumbnail(fi.get("thumbnail_url").toString());
        JSONArray fiaki = fi.getJSONArray("menus");

        //Trouver date
        int finali = 0;
//        System.out.println(fiaki.length());
        boolean menuUpload = false;
        for (int i = 0; i < fiaki.length(); i++) {
//            System.out.println(fiaki.getJSONObject(i).getString("date"));
            if (fiaki.getJSONObject(i).getString("date").equals(date)) {
                menuUpload = true;
                ailfinal.addField("Date", fiaki.getJSONObject(i).getString("date"), false);
                ailfinalerror.addField("Date", fiaki.getJSONObject(i).getString("date"), false);
//                System.out.println("trouve");
                finali = i;
//                System.out.println(finali);
                break;
            }
        }

        if (!menuUpload) {
//            System.out.println("Sortie d'action");
            EmbedBuilder sortieAction = new EmbedBuilder();
            sortieAction.setTitle("Résultat non trouvé");
            sortieAction.setColor(Color.RED);
            sortieAction.setDescription("Le menu n'a pas encore été mis en ligne");
            sortieAction.setFooter("Resto U' Médreville");
            return sortieAction.build();
        }


        JSONObject fia = fi.getJSONArray("menus").getJSONObject(finali);

        //Deja date
        JSONObject fiak = fia.getJSONArray("meal").getJSONObject(0);
        JSONObject fiakr = fiak.getJSONArray("foodcategory").getJSONObject(0);
        JSONArray foodArray = fiakr.getJSONArray("dishes");
        ailfinal.setColor(Color.red);
        ailfinalerror.setColor(Color.red);


        boolean entree = false;
        boolean plats = false;
        boolean vg = false;
        boolean accompagnement = false;
        boolean dessert = false;

        try {
            for (int i = 0; i < foodArray.length(); i++) {
                String current = foodArray.getJSONObject(i).getString("name");
                current = current.replaceAll(" - ", "");

                //Module
                if (lowerAccent(current).contains("entree") && !entree) {
                    String entstr = current;

                    while (!lowerAccent(foodArray.getJSONObject(i + 1).getString("name")).contains("plats")) {
                        i++;
                        String plusun = foodArray.getJSONObject(i).getString("name");
                        entstr += "\n" + plusun;
                    }
                    ailfinal.addField("Entrées", entstr, false);
                    entree = true;


                } else if (lowerAccent(current).contains("plats") && !plats) {
                    String str = current;

                    while (!lowerAccent(foodArray.getJSONObject(i + 1).getString("name")).contains("vg")) {
                        i++;
                        String plusun = foodArray.getJSONObject(i).getString("name");
                        str += "\n" + plusun;
                    }
                    ailfinal.addField("Plats", str, false);
                    plats = true;


                } else if (lowerAccent(current).contains("vg") && !vg) {
                    String str = current;

                    while (!lowerAccent(foodArray.getJSONObject(i + 1).getString("name")).contains("garniture")) {
                        i++;
                        String plusun = foodArray.getJSONObject(i).getString("name");
                        str += "\n" + plusun;
                    }
                    ailfinal.addField("VG", str, false);
                    vg = true;


                } else if (lowerAccent(current).contains("garniture") && !accompagnement) {
                    String str = current;

                    while (!lowerAccent(foodArray.getJSONObject(i + 1).getString("name")).contains("dessert")) {
                        i++;
                        String plusun = foodArray.getJSONObject(i).getString("name");
                        str += "\n" + plusun;
                    }
                    ailfinal.addField("Accompagnements", str, false);
                    accompagnement = true;

                } else if (lowerAccent(current).contains("dessert") && !dessert) {
                    String str = current;

                    while (i < foodArray.length()-1) {
                        i++;
                        String plusun = foodArray.getJSONObject(i).getString("name");
                        str += "\n" + plusun;
                    }
                    ailfinal.addField("Desserts", str, false);
                    dessert = true;
                }
            }

            connection.disconnect();

            if (entree && plats && vg && accompagnement && dessert){
                return ailfinal.build();
            } else {
                String sli = "";
                for (int i = 0; i < foodArray.length(); i++) {
                    String temp = foodArray.getJSONObject(i).getString("name");
                    temp = temp.replaceAll(" - ", "");
                    if (!temp.equals("")){
                        System.out.println(temp);
                        sli += temp+"\n\n";
                    }
                }
                String strNew = sli.substring(0, sli.length()-2);
                ailfinalerror.addField("Repas", strNew, false);
                return ailfinalerror.build();
            }

        } catch (Exception e){
            String sli = "";
            for (int i = 0; i < foodArray.length(); i++) {
                String temp = foodArray.getJSONObject(i).getString("name");
                temp = temp.replaceAll(" - ", "");
                if (!temp.equals("")){
                    System.out.println(temp);
                    sli += temp+"\n\n";
                }
            }
            String strNew = sli.substring(0, sli.length()-2);
            ailfinalerror.addField("Repas", strNew, false);
            return ailfinalerror.build();
        }






    }
}


