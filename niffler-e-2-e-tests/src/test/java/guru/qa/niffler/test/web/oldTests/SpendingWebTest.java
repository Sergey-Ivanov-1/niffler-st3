package guru.qa.niffler.test.web.oldTests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.test.web.BaseWebTest;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class SpendingWebTest extends BaseWebTest {
	private static final String USERNAME = "ivanov";
	private static final String PASSWORD = "12345678";
	private static final String CATEGORY = "День рождения";
	private static final String DESCRIPTION = "Погулять в кафе";
	private static final double AMOUNT = 10000.00;

	//ivanov, irina - with friends
	//ivanov146, marina invitation send
	//sofia, mihail invitation received

	static {
		Configuration.browser = "chrome";
		Configuration.browserSize = "1980x1024";
	}

	@BeforeEach
	void doLogin() {
		Selenide.open("http://127.0.0.1:3000/main");
		$("a[href*='redirect']").click();
		$("input[name='username']").setValue(USERNAME);
		$("input[name='password']").setValue(PASSWORD);
		$("button[type='submit']").click();
	}

	@Category(
			username = USERNAME,
			category = CATEGORY
	)
	@ApiLogin()
	@Spend(
			username = USERNAME,
			description = DESCRIPTION,
			category = CATEGORY,
			amount = AMOUNT,
			currency = CurrencyValues.RUB
	)
	@Test
	@AllureId("100")
	void spendingShouldBeDeletedAfterDeleteAction(SpendJson createdSpend) {
		$(".spendings__content tbody")
				.$$("tr")
				.find(text(createdSpend.getDescription()))
				.$("td")
				.scrollTo()
				.click();

		Allure.step(
				"Check spending",
				() -> $(byText("Delete selected")).click());

		Allure.step(
				"Check spending",
				() -> $(".spendings__content tbody")
						.$$("tr")
						.shouldHave(size(0)));
	}
}
