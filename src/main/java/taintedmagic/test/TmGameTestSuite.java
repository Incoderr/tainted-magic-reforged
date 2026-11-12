package taintedmagic.test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import taintedmagic.TaintedMagic;

@GameTestHolder(TaintedMagic.MODID)
@PrefixGameTestTemplate(false)
public final class TmGameTestSuite {
    private static final List<TmGameTestSuite> SUITES = new ArrayList<>();
    private final String name;
    private final List<TestCase> tests = new ArrayList<>();
    private boolean isolated;
    private String structure = "test/platform";

    private record TestCase(String name, Consumer<GameTestHelper> body) {
    }

    public TmGameTestSuite(String name) {
        this.name = name;
    }

    public TmGameTestSuite add(String test, Consumer<GameTestHelper> body) {
        tests.add(new TestCase(name + "_" + test, body));
        return this;
    }

    public TmGameTestSuite isolated() {
        isolated = true;
        return this;
    }

    public TmGameTestSuite structure(String structure) {
        this.structure = structure;
        return this;
    }

    public void register(IEventBus modEventBus) {
        SUITES.add(this);
    }

    @GameTestGenerator
    public static List<TestFunction> generate() {
        List<TestFunction> result = new ArrayList<>();
        for (TmGameTestSuite suite : SUITES) {
            for (TestCase test : suite.tests) {
                result.add(new TestFunction(TaintedMagic.MODID + "_" + (suite.isolated ? test.name() : suite.name), TaintedMagic.MODID + "." + test.name(), TaintedMagic.MODID + ":" + suite.structure, 100, 0, true, helper -> test.body().accept(new TmGameTestHelper(helper))));
            }
        }
        return result;
    }
}