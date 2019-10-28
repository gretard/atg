package edu.ktu.atg.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sampullara.cli.Args;

import edu.ktu.atg.common.models.OptionsRequest;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        final OptionsRequest obj = new OptionsRequest();
        Args.parseOrExit(obj, new String[] { "-c", "a,b,c"});
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(obj));
    }
}
