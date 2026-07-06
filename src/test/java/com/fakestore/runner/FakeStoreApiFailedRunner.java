package com.fakestore.runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    features = "@target/rerun.txt",
    glue = { "com.fakestore.common.steps" }
)
public class FakeStoreApiFailedRunner {

    static {
        System.setProperty("serenity.project.name", "FakeStore API Automation");
    }
}