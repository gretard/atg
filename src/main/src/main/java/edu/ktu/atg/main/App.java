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
    public static void main(String[] args) throws Throwable {
        final OptionsRequest request = new OptionsRequest();
        Args.parseOrExit(request, args);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("Will be using: " + gson.toJson(request));
        MainTestsGenerator sut = new MainTestsGenerator();
        sut.generate(request);
    }
}
