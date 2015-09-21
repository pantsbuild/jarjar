package org.pantsbuild.jarjar;

import java.io.File;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BadPackagesTest extends IntegrationTestBase {

  /**
   * Creates a jar file with a single class, compiled in java 7.
   *
   * A duplicate version of the class in java 6 is created in a "oldversion" directory, and included
   * in the same jar.
   *
   * This targets a bug where jarjar will move classes which are not in a folder according to their
   * package name BACK into the appropriate folder, which can potentially cause
   * DuplicateJarEntryExcetions, in the very specific case where two versions of a class exist in
   * the same jar, with one version stored in a separate directory.
   *
   * This bug only occurs if the classes have bytecode compiled for different java versions.
   *
   * In general, this situation should never happen and is reflective of corrupted jars, but
   * unfortunately there are some examples of these jars in the wild
   * (eg http://mvnrepository.com/artifact/com.sun.xml.bind/jaxb-xjc/2.2). These can get pulled in
   * as transitive dependencies when folks use lots of opensource software without them even
   * knowing, causing jar shading to explode.
   *
   * @return
   * @throws IOException
   */
  private File createJarWithOldVersion() throws IOException {
    String className = "org.pantsbuild.jarjar.fake.Foobar";
    String basePath = className.replaceAll("[.]", "/");
    String sourcePath = basePath + ".java";
    String binaryPath = basePath + ".class";

    Map<String, String> files = new HashMap<String, String>();
    files.put(sourcePath, basicJavaFile(className));

    File folder = createTree(files);
    assertTrue(tryCompile(folder, "-source", "6", "-target", "6", sourcePath));

    File srcFile = new File(folder + File.separator + binaryPath);
    File dstFile = new File(folder + File.separator + "oldversion" + File.separator + binaryPath);
    dstFile.getParentFile().mkdirs();
    srcFile.renameTo(dstFile);

    assertTrue(tryCompile(folder, "-source", "7", "-target", "7", sourcePath));

    for (String file : new FileTree(folder)) {
      if (file.endsWith(".java")) {
        new File(folder.getAbsolutePath() + File.separator + file).delete();
      }
    }

    return createJar(folder);
  }

  @Test
  public void testJarWithOldVersionsPackage() throws Exception {
    File jar = createJarWithOldVersion();
    List<String> entries = getJarEntries(jar);
    assertTrue("Unshaded jar does not have new version of Foobar.",
        entries.contains("org/pantsbuild/jarjar/fake/Foobar.class"));
    assertTrue("Unshaded jar does not have old version of Foobar.",
        entries.contains("oldversion/org/pantsbuild/jarjar/fake/Foobar.class"));

    File shadedJar = shadeJar(jar);
    entries = getJarEntries(shadedJar);
    assertTrue("Shaded jar does not have new version of Foobar.",
        entries.contains("org/pantsbuild/jarjar/fake/Foobar.class"));
    assertTrue("Shaded jar does not have old version of Foobar.",
        entries.contains("oldversion/org/pantsbuild/jarjar/fake/Foobar.class"));
  }

}
