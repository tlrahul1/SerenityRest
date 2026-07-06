package com.fakestore.runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    plugin = {
        "pretty",
        "rerun:target/rerun.txt"   // Failed scenarios
    },
    features = "src/test/resources/features/ProductAPI",
    glue = {"com.fakestore.common.steps"}
)
public class FakeStoreApiRunner {
	static {
        System.setProperty("serenity.project.name", "FakeStore API Automation");
    }
}
