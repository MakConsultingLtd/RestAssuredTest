package stepDef;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)

@CucumberOptions (
    dryRun = false,
    features = "src/test/resources/features",
    glue = "stepDef",
    format = { "pretty", "json:target/cucumber.json"},
    tags = {"~@ignore", "@API"}
)

public class CucumberTest {
}

