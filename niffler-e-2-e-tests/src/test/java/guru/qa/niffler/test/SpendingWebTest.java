package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.category.Category;
import guru.qa.niffler.jupiter.spend.Spend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class SpendingWebTest {
	private static final String USERNAME = "ivanov";
	private static final String PASSWORD = "12345678";
	private static final String CATEGORY = "День рождения";
	private static final String DESCRIPTION = "Погулять в кафе";
	private static final double AMOUNT = 10000.00;


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
			username = "ivanov",
			category = CATEGORY
	)
	@Spend(
			username = USERNAME,
			description = DESCRIPTION,
			category = CATEGORY,
			amount = AMOUNT,
			currency = CurrencyValues.RUB
	)
	@Test
	void spendingShouldBeDeletedAfterDeleteAction(SpendJson createdSpend) {
		$(".spendings__content tbody")
				.$$("tr")
				.find(text(createdSpend.getDescription()))
				.$$("td")
				.first()
				.scrollTo()
				.click();

		$(byText("Delete selected")).click();

		$(".spendings__content tbody")
				.$$("tr")
				.shouldHave(size(0));
	}
}
