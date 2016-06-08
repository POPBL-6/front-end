import controllers.AdvancedTabControllerTest;
import controllers.tableutils.DataTypeTableCellTest;
import controllers.tableutils.SubscribedTableCellTest;
import controllers.tableutils.TimestampTableCellTest;
import controllers.validators.ValidTypeValidatorTests;
import model.DataUtilsCreationTests;
import model.DataUtilsIdentificationTests;
import model.DataUtilsIncorrectNumbersTest;
import model.FXModelTest;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import utils.JavaFXThreadingRule;

/**
 * Created by Gorka Olalde on 3/6/16.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( {DataTypeTableCellTest.class,
                SubscribedTableCellTest.class,
                TimestampTableCellTest.class,
                AdvancedTabControllerTest.class,
                FXModelTest.class,
                DataUtilsCreationTests.class,
                DataUtilsIdentificationTests.class,
                DataUtilsIncorrectNumbersTest.class,
                ValidTypeValidatorTests.class})
public class AllTestsSuite {

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
}
