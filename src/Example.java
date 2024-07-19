public class Example {
    class Foo {
        int bar;

        public Foo(int bar) {
            this.bar = bar;
        }

        public int baz() {
            return bar * 2;
        }
    }

    public Example() {
        Foo foo = new Foo(5);
        System.out.println(foo.baz());

        FooData fooData = new FooData(5);
        System.out.println(FooLogic.baz(fooData));
    }

    final class FooData {
        final int bar;

        public FooData(int bar) {
            this.bar = bar;
        }
    }

    final class FooLogic {
        static int baz(FooData foo) {
            return foo.bar * 2;
        }
    }
}
