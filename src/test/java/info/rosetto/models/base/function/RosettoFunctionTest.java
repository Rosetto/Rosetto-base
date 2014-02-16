package info.rosetto.models.base.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.utils.base.Values;

import org.junit.Test;


public class RosettoFunctionTest {
    
    
    @SuppressWarnings("serial")
    @Test
    public void インスタンス化テスト() throws Exception {
        RosettoFunction f1 = new RosettoFunction("f1") {
            @Override
            protected RosettoValue run(ExpandedArguments args) {return Values.VOID;}
        };
        assertThat(f1.getName(), is("f1"));
        assertThat(f1.getArguments().size(), is(0));
        
        RosettoFunction f2 = new RosettoFunction("f2",
                "hoge", "huga", "foo=bar") {
            @Override
            protected RosettoValue run(ExpandedArguments args) {return Values.VOID;}
        };
        assertThat(f2.getName(), is("f2"));
        assertThat(f2.getArguments().size(), is(3));
        
        RosettoFunction f3 = new RosettoFunction("f3", 
                (String[])null) {
            @Override
            protected RosettoValue run(ExpandedArguments args) {return Values.VOID;}
        };
        assertThat(f3.getName(), is("f3"));
        assertThat(f3.getArguments().size(), is(0));
    }
//    
//    private String bufferForExecTest;
//    @Test
//    public void execで関数を実行できる() throws Exception {
//        bufferForExecTest = null;
//        RosettoFunction f1 = new RosettoFunction("f1") {
//            @Override
//            protected RosettoValue run(ExpandedArguments args) {
//                bufferForExecTest = "f1";
//                for(Entry<String, String> e:args.getMap().entrySet()) {
//                    bufferForExecTest += " " + e.getKey() + "=" + e.getValue();
//                }
//                return Values.VOID;
//            }
//        };
//        f1.exec();
//        assertThat(bufferForExecTest, is("f1"));
//        
//        RosettoFunction f2 = new RosettoFunction(new FunctionName("test", "f2")) {
//            @Override
//            protected RosettoValue run(ExpandedArguments args) {
//                bufferForExecTest = "f2";
//                for(Entry<String, String> e:args.getMap().entrySet()) {
//                    bufferForExecTest += " " + e.getKey() + "=" + e.getValue();
//                }
//                return Values.VOID;
//            }
//        };
//        f2.exec("hoge=fuga");
//        assertThat(bufferForExecTest, is("f2 hoge=fuga"));
//        
//        RosettoFunction f3 = new RosettoFunction(
//                new FunctionName("this.is.some.long.package.function","f3"), 
//                "foo=bar") {
//            @Override
//            protected RosettoValue run(ExpandedArguments args) {
//                bufferForExecTest = "f3";
//                for(Entry<String, String> e:args.getMap().entrySet()) {
//                    bufferForExecTest += " " + e.getKey() + "=" + e.getValue();
//                }
//                return Values.VOID;
//            }
//        };
//        f3.exec(new RosettoArguments("hoge=fuga ika=tako"));
//        assertThat(bufferForExecTest, is("f3 foo=bar hoge=fuga ika=tako"));
//        
//        RosettoFunction f4 = new RosettoFunction(
//                new FunctionName("test","f4")) {
//            @Override
//            protected RosettoValue run(ExpandedArguments args) {
//                bufferForExecTest = "f4";
//                return Values.VOID;
//            }
//        };
//        f4.exec((RosettoArguments) null);
//        assertThat(bufferForExecTest, is("f4"));
//        
//        RosettoFunction f5 = new RosettoFunction(
//                new FunctionName("test","f5")) {
//            @Override
//            protected RosettoValue run(ExpandedArguments args) {
//                bufferForExecTest = "f5";
//                return Values.VOID;
//            }
//        };
//        f5.exec((String) null);
//        assertThat(bufferForExecTest, is("f5"));
//    }
//    
//    @Test
//    public void getFunctionInfoで形式通りの関数情報が返る() throws Exception {
//        //function testfunc(arg1, arg2=10) <example.test.testfunc>
//        RosettoFunction testfunc = new RosettoFunction(
//                new FunctionName("example.test", "testfunc"),
//                "arg1", "arg2=10") {
//            @Override
//            protected RosettoValue run(ExpandedArguments args) {return Values.VOID;}
//        };
//        assertThat(testfunc.getFunctionInfo(), 
//            is("function testfunc(arg1, arg2=10) <example.test.testfunc>"));
//        //toStringと等しい
//        assertThat(testfunc.getFunctionInfo(), 
//                is(testfunc.toString()));
//    }
//
//    @Test
//    public void getNameObjectへの短縮メソッドが等しい値を返す() throws Exception {
//        RosettoFunction testfunc = new RosettoFunction(
//                new FunctionName("example.test", "testfunc"),
//                "arg1", "arg2=10") {
//            @Override
//            protected RosettoValue run(ExpandedArguments args) {return Values.VOID;}
//        };
//        FunctionName name = testfunc.getNameObject();
//        assertThat(name.getFullName(), is(testfunc.getFullName()));
//        assertThat(name.getShortName(), is(testfunc.getShortName()));
//        assertThat(name.getPackage(), is(testfunc.getPackage()));
//    }
//    
//    @Test
//    public void デフォルト値付き引数の後に通常引数を指定するとエラー() {
//        try {
//            new RosettoFunction(
//                    new FunctionName("test.function", "f1"),
//                    "arg1", "arg2=10", "arg3") {
//                @Override
//                public RosettoValue run(ExpandedArguments args) {return Values.VOID;}
//            };
//            fail();
//        } catch(Exception e) {
//            assertThat(e, instanceOf(IllegalArgumentException.class));
//        }
//    }
//    
//    @Test
//    public void RuntimeArgumentsから引数の情報を取得できる() throws Exception {
//        RosettoFunction f2 = new RosettoFunction(
//                new FunctionName("test", "f2"),
//                "hoge", "huga", "foo=bar") {
//            @Override
//            protected RosettoValue run(ExpandedArguments args) {
//                assertThat(args.get("foo"), is("bar"));
//                assertThat(args.get("hoge"), is("ika"));
//                assertThat(args.containsKey("huga"), is(true));
//                assertThat(args.containsKey("piyo"), is(false));
//                return Values.VOID;
//            }
//        };
//        Contexts.getFunction().initialize();
//        Contexts.getFunction().registerStatic(f2);
//        f2.exec("hoge=ika huga=tako");
//        Contexts.getFunction().initialize();
//    }
//    
//    @Test
//    public void toActionでアクションに変換できる() throws Exception {
//        FunctionName name1 = new FunctionName("test.function", "sut1");
//        RosettoFunction sut1 = new RosettoFunction(name1, "foo=bar", "baz=%hoge") {
//            @Override
//            protected RosettoValue run(ExpandedArguments args) {return Values.VOID;}
//        };
//        ActionCall result1 = new ActionCall(name1);
//        assertThat(sut1.toAction().toString(), is(result1.toString()));
//        
//        FunctionName name2 = new FunctionName("test.function", "sut2");
//        String arg2 = "hoge=fuga";
//        RosettoFunction sut2 = new RosettoFunction(name2, "foo=bar", "baz=%hoge") {
//            @Override
//            protected RosettoValue run(ExpandedArguments args) {return Values.VOID;}
//        };
//        ActionCall result2 = new ActionCall(name2, arg2);
//        assertThat(sut2.toAction(arg2).toString(), is(result2.toString()));
//        
//        FunctionName name3 = new FunctionName("test.function", "sut3");
//        RosettoArguments arg3 = new RosettoArguments("hoge=foobar");
//        RosettoFunction sut3 = new RosettoFunction(name3, "foo=bar", "baz=%hoge") {
//            @Override
//            protected RosettoValue run(ExpandedArguments args) {return Values.VOID;}
//        };
//        ActionCall result3 = new ActionCall(name3, arg3);
//        assertThat(sut3.toAction(arg3).toString(), is(result3.toString()));
//    }

}
