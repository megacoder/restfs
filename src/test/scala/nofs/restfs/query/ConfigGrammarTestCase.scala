package nofs.restfs.query

import ast.{ApplyWebMethodOperation, ApplyTransformationOperation, FSRuleStm, AddTranslatorStm}
import org.junit.{Assert, Test, After, Before}

class ConfigGrammarTestCase {
  @Before def Setup() = {
  }

  @After def TearDown() = {
  }

  @Test def TestAddTranslator1() : Unit = {
    val g = new ConfigGrammar();
    val stms = g.Parse("ADD XFORM FROM \"json\" TO \"xml\" WITH \"foo\"");
    Assert.assertEquals(1, stms.size)
    val stm = stms(0).asInstanceOf[AddTranslatorStm]

    Assert.assertEquals("json", stm.from())
    Assert.assertEquals("xml", stm.to())
    Assert.assertEquals("foo", stm.withFile())

    return Assert.assertTrue(true);
  }

  @Test def TestAddTranslator2() : Unit = {
    val g = new ConfigGrammar();
    val stms = g.Parse(
      "ADD XFORM FROM \"json\" TO \"xml\" WITH \"foo\"\n" +
      "ADD XFORM FROM \"jpeg\" TO \"png\" WITH \"/bar/baz\"" );
    Assert.assertEquals(2, stms.size)
    val stm1 = stms(0).asInstanceOf[AddTranslatorStm]
    val stm2 = stms(1).asInstanceOf[AddTranslatorStm]

    Assert.assertEquals("json", stm1.from())
    Assert.assertEquals("xml", stm1.to())
    Assert.assertEquals("foo", stm1.withFile())

    Assert.assertEquals("jpeg", stm2.from())
    Assert.assertEquals("png", stm2.to())
    Assert.assertEquals("/bar/baz", stm2.withFile())

    return Assert.assertTrue(true);
  }

  @Test def TestFSRuleAndXformGet_WithOAuth() : Unit = {
    val g = new ConfigGrammar();
    val stms = g.Parse(
      "MKNOD(\"/foo/bar\")\n" +
      "XFORM FROM \"json\" TO \"xml\"\n" +
      "GET FROM \"http://foo/bar\" USING OAUTH TOKEN \"/auth/baz\"");
    Assert.assertEquals(1, stms.size)
    val stm1 = stms(0).asInstanceOf[FSRuleStm]

    Assert.assertEquals("MKNOD", stm1.action().action())
    Assert.assertEquals("/foo/bar", stm1.action().fileName())

    Assert.assertEquals(2, stm1.operations().size)

    val xform = stm1.operations()(0).asInstanceOf[ApplyTransformationOperation]
    val getop = stm1.operations()(1).asInstanceOf[ApplyWebMethodOperation]

    Assert.assertEquals("json", xform.fromType())
    Assert.assertEquals("xml", xform.toType())

    Assert.assertEquals("get", getop.method())
    Assert.assertEquals("http://foo/bar", getop.url())
    Assert.assertEquals("/auth/baz", getop.token())

    return Assert.assertTrue(true);
  }

  @Test def TestFSRuleAndXformGet() : Unit = {
    val g = new ConfigGrammar();
    val stms = g.Parse(
      "MKNOD(\"/foo/bar\")\n" +
      "XFORM FROM \"json\" TO \"xml\"\n" +
      "GET FROM \"http://foo/bar\"");
    Assert.assertEquals(1, stms.size)
    val stm1 = stms(0).asInstanceOf[FSRuleStm]

    Assert.assertEquals("MKNOD", stm1.action().action())
    Assert.assertEquals("/foo/bar", stm1.action().fileName())

    Assert.assertEquals(2, stm1.operations().size)

    val xform = stm1.operations()(0).asInstanceOf[ApplyTransformationOperation]
    val getop = stm1.operations()(1).asInstanceOf[ApplyWebMethodOperation]

    Assert.assertEquals("json", xform.fromType())
    Assert.assertEquals("xml", xform.toType())

    Assert.assertEquals("get", getop.method())
    Assert.assertEquals("http://foo/bar", getop.url())
    Assert.assertEquals("", getop.token())

    return Assert.assertTrue(true);
  }

  @Test def TestFSRuleAndGet() : Unit = {
    val g = new ConfigGrammar();
    val stms = g.Parse(
      "MKNOD(\"/foo/bar\")\n" +
      "GET FROM \"http://foo/bar\"");
    Assert.assertEquals(1, stms.size)
    val stm1 = stms(0).asInstanceOf[FSRuleStm]

    Assert.assertEquals("MKNOD", stm1.action().action())
    Assert.assertEquals("/foo/bar", stm1.action().fileName())

    Assert.assertEquals(1, stm1.operations().size)

    val getop = stm1.operations()(0).asInstanceOf[ApplyWebMethodOperation]

    Assert.assertEquals("get", getop.method())
    Assert.assertEquals("http://foo/bar", getop.url())
    Assert.assertEquals("", getop.token())

    return Assert.assertTrue(true);
  }

  @Test def TestFSRuleAndDelete() : Unit = {
    val g = new ConfigGrammar();
    val stms = g.Parse(
      "MKNOD(\"/foo/bar\")\n" +
      "DELETE FROM \"http://foo/bar\"");
    Assert.assertEquals(1, stms.size)
    val stm1 = stms(0).asInstanceOf[FSRuleStm]

    Assert.assertEquals("MKNOD", stm1.action().action())
    Assert.assertEquals("/foo/bar", stm1.action().fileName())

    Assert.assertEquals(1, stm1.operations().size)

    val getop = stm1.operations()(0).asInstanceOf[ApplyWebMethodOperation]

    Assert.assertEquals("delete", getop.method())
    Assert.assertEquals("http://foo/bar", getop.url())
    Assert.assertEquals("", getop.token())

    return Assert.assertTrue(true);
  }

  @Test def TestFSRuleAndPut() : Unit = {
    val g = new ConfigGrammar();
    val stms = g.Parse(
      "CLOSED(\"/foo/bar\")\n" +
      "PUT TO \"http://foo/bar\"");
    Assert.assertEquals(1, stms.size)
    val stm1 = stms(0).asInstanceOf[FSRuleStm]

    Assert.assertEquals("CLOSED", stm1.action().action())
    Assert.assertEquals("/foo/bar", stm1.action().fileName())

    Assert.assertEquals(1, stm1.operations().size)

    val getop = stm1.operations()(0).asInstanceOf[ApplyWebMethodOperation]

    Assert.assertEquals("put", getop.method())
    Assert.assertEquals("http://foo/bar", getop.url())
    Assert.assertEquals("", getop.token())

    return Assert.assertTrue(true);
  }

  @Test def TestAddTranslatorAndFSRuleAndXformGet() : Unit = {
    val g = new ConfigGrammar();
    val stms = g.Parse(
      "ADD XFORM FROM \"json\" TO \"xml\" WITH \"foo\"\n" +
      "MKNOD(\"/foo/bar\")\n" +
      "XFORM FROM \"json\" TO \"xml\"\n" +
      "GET FROM \"http://foo/bar\" USING OAUTH TOKEN \"/auth/baz\"");
    Assert.assertEquals(2, stms.size)
    val stm1 = stms(0).asInstanceOf[AddTranslatorStm]
    val stm2 = stms(1).asInstanceOf[FSRuleStm]

    Assert.assertEquals("json", stm1.from())
    Assert.assertEquals("xml", stm1.to())
    Assert.assertEquals("foo", stm1.withFile())

    Assert.assertEquals("MKNOD", stm2.action().action())
    Assert.assertEquals("/foo/bar", stm2.action().fileName())

    Assert.assertEquals(2, stm2.operations().size)

    val xform = stm2.operations()(0).asInstanceOf[ApplyTransformationOperation]
    val getop = stm2.operations()(1).asInstanceOf[ApplyWebMethodOperation]

    Assert.assertEquals("json", xform.fromType())
    Assert.assertEquals("xml", xform.toType())

    Assert.assertEquals("get", getop.method())
    Assert.assertEquals("http://foo/bar", getop.url())
    Assert.assertEquals("/auth/baz", getop.token())

    return Assert.assertTrue(true);
  }
}