package daikon.split;

import utilMDE.*;
import org.apache.oro.text.regex.*;
import daikon.*;
import daikon.split.misc.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.util.logging.Logger;
import jtb.ParseException;

/**
 * This class contains static method read_spinfofile( spinfofilename ),
 * which creates Splitters from a .spinfo file.
 **/
public class SplitterFactory {

  private static Perl5Matcher re_matcher = new Perl5Matcher();
  private static Perl5Compiler re_compiler = new Perl5Compiler();

  public static final Logger debug =
    Logger.getLogger("daikon.split.SplitterFactory");

  /** The directory in which the java files for the splitter will be made. */
  // This must not be set in a static block, which happens before the
  // Configuration object has had a chance to possibly set
  // dkconfig_delete_splitters_on_exit.
  private static String tempdir;

  /**
   * Boolean. Specifies whether or not the temporary Splitter files
   * should be deleted on exit.
   **/
  public static boolean dkconfig_delete_splitters_on_exit = true;

  // It would be better for SplitterFactory to use PrintWriters (or
  // PrintStreams) backed by ByteArrayOutputStreams, rather than
  // StringBuffers; that would eliminate the line separator ugliness.
  private static String lineSep = System.getProperty("line.separator");

  /**
   * guid is a counter that increments every time a file is written.  It is
   * used to ensure that every file written has a unique name.
   */
  private static int guid = 0;

  /// Methods

  /**
   * Reads the Splitter info.
   * @param infofile <filename>.spinfo
   * @param all_ppts a PptMap for the Ppts of this run of Daikon.
   * @return an array of arrays of SplitterObjects for each Ppt.
   **/
  public static SplitterObject[][] read_spinfofile(File infofile, PptMap all_ppts)
    throws IOException, FileNotFoundException {
    if (tempdir == null) {
      tempdir = createTempDir();
    }
    SpinfoFileParser fileParser = new SpinfoFileParser(infofile, tempdir);
    SplitterObject[][] splitterObjects = fileParser.getSplitterObjects();
    StatementReplacer statementReplacer = fileParser.getReplacer();
    for (int i = 0; i < splitterObjects.length; i++) {
      if (splitterObjects[i].length != 0) {
        PptTopLevel ppt = findPpt(splitterObjects[i][0].getPptName(), all_ppts);
        if (ppt != null) {
          loadSplitters(splitterObjects[i], ppt, statementReplacer);
        }
      }
    }
    if (! dkconfig_delete_splitters_on_exit) {
      System.out.println("Splitters for this run created in " + tempdir);
    }
    return splitterObjects;
  }

  // Accessible for the purpose of testing.
  public static String getTempDir() {
    if (tempdir == null) {
      tempdir = createTempDir();
    }
    return tempdir;
  }

  private static void printAll(PptMap map) {
    System.out.println("start");
    Iterator it = map.pptIterator();
    while (it.hasNext()) {
      PptTopLevel ppt = (PptTopLevel) it.next();
      System.out.println("PPT: " + ppt.name());
    }
  }


  /**
   * Writes, compiles, and loads the splitter java files for each
   * splitterObject in splitterObjects.
   * @param splitterObjects are the splitterObjects for ppt
   * @param ppt the Ppt for these splitterObjects
   * @param statementReplacer a StatementReplacer for the replace statements
   *  to be used in these splitterObjects.
   */
  private static void loadSplitters(SplitterObject[] splitterObjects,
                                    PptTopLevel ppt,
                                    StatementReplacer statementReplacer)
    throws IOException {
    for (int i = 0; i < splitterObjects.length; i++) {
      SplitterObject splitObj = splitterObjects[i];
      String fileName = getFileName(splitObj.getPptName());
      StringBuffer file;
      try {
        SplitterJavaSource splitterWriter =
          new SplitterJavaSource(splitObj,
                                 splitObj.getPptName(),
                                 fileName,
                                 ppt.var_infos,
                                 statementReplacer);
        file = splitterWriter.getFileText();
      } catch (ParseException e) {
        System.out.println("Error in SplitterFactory while writing splitter java file for: " + lineSep +
                           splitObj.condition() + " cannot be parsed.");
        continue;
      }
      String fileAddress = tempdir + fileName;
      splitObj.setClassName(fileName);
      try {
        BufferedWriter writer = UtilMDE.BufferedFileWriter(fileAddress + ".java");
        if (dkconfig_delete_splitters_on_exit) {
          (new File (fileAddress + ".java")).deleteOnExit();
          (new File (fileAddress + ".class")).deleteOnExit();
          }
        writer.write(file.toString());
        writer.flush();
      } catch (IOException ioe) {
        System.out.println("Error while writing Splitter file: " +
                           fileAddress);
        debugPrintln(ioe.toString());
      }
    }
    List /*String*/ fileNames = new ArrayList();
    for (int i = 0; i < splitterObjects.length; i++) {
      fileNames.add(splitterObjects[i].getFullSourcePath());
    }
    FileCompiler.compileFiles(fileNames);
    SplitterLoader loader = new SplitterLoader();
    for (int i = 0; i < splitterObjects.length; i++) {
      splitterObjects[i].load(loader);
    }
  }



  /**
   * Finds the PptTopLevel for ppt_name.
   * @return The PptTopLevel for the Ppt given in ppt_name or if none
   *   can be found, it returns a random PptTopLevel.
   */
  private static PptTopLevel findPpt(String ppt_name, PptMap all_ppts) {

    PptTopLevel ppt = findPptHelper(ppt_name, all_ppts);
    if (ppt == null) {
      // try with the OBJECT program point
      ppt = findPptHelper("OBJECT", all_ppts);
    }
    if (ppt == null) {
      // We just get a random program point (the first) from the pptMap.
      // Hopefully we can find the variables that we need at this ppt
      Iterator pptIter = all_ppts.pptIterator();
      if (pptIter.hasNext()) {
        ppt = (PptTopLevel)pptIter.next();
      }
    }
    return ppt;
  }


  /**
   * Find a program point in Daikon whose name matches "ppt_name".
   * ppt_name is usually of the form "MethodName.functionName."
   **/
  private static PptTopLevel findPptHelper(String ppt_name, PptMap all_ppts) {
    Object exact_result = all_ppts.get(ppt_name);
    if (exact_result != null) {
      return (PptTopLevel) exact_result;
    }
    // look for corresponding EXIT ppt. This is because the exit ppt usually has
    // more relevant variables in scope (eg. return, hashcodes) than the enter.
    int index = ppt_name.indexOf("OBJECT");
    if (index == -1) {
      // Didn't find "OBJECT" suffix; add ".*EXIT".
      ppt_name = qm(ppt_name) + ".*EXIT";
    } else {
      // Found "OBJECT" suffix.
      if (ppt_name.length() > 6) {
        ppt_name = ppt_name.substring(0, index-1) + ":::OBJECT";
      }
    }
    Pattern ppt_pattern;
    try {
      ppt_pattern = re_compiler.compile(ppt_name);
    } catch (MalformedPatternException e) {
      e.printStackTrace();
      throw new Error("Error in regexp ppt_name: " + ppt_name + lineSep
                      + e.toString());
    }
    Iterator ppt_itor = all_ppts.pptIterator();
    while (ppt_itor.hasNext()) {
      String name = ((PptTopLevel)ppt_itor.next()).name;
      if (re_matcher.contains(name, ppt_pattern)) {
        // return more than one? do more than one match??
        // XXX In particular, we get in trouble here if the method
        // name we want is a prefix of other method names. For
        // instance, if there's both a frob and a frobAll method, both
        // Pkg.frob(int):::EXIT8 and Pkg.frobAll(int[])::EXIT12 could
        // match /Pkg.frob.*EXIT/, and it's luck of the draw as to
        // which one we get. A workaround is to put "Pkg.frob(",
        // rather than just "Pkg.frob", in your .spinfo file. -smcc
        return all_ppts.get(name);
      }
    }
    return null;
  }


  /**
   * Returns a file name for a splitter file to be used with a Ppt
   * with the name, ppt_name.  The file name is ppt_name with all
   * characters which are invalid for use in a java file name (such
   * as ".") replaced with "_".  Then "_guid" is append to the end.
   * For example if ppt_name is "myPackage.myClass.someMethod" and
   * guid = 12, then the following would be returned:
   * "myPackage_myClass_someMethod_12".
   * @param ppt_name the name of the Ppt for which the splitter
   *  java file is going to be used with.
   */
  private static String getFileName(String ppt_name) {
    String splitterName = clean(ppt_name);
    splitterName = splitterName + "_" + guid;
    guid++;
    return splitterName;
  }

  /**
   * Cleans str by replacing all characters that are not
   * valid java indentifier parts with "_".
   * @param str the string to be cleaned.
   * @return str with all non java indentifier parts replaced
   *  with "_".
   */
  private static String clean(String str) {
    char[] cleaned = str.toCharArray();
    for (int i=0; i < cleaned.length; i++) {
      char c = cleaned[i];
      if (! Character.isJavaIdentifierPart(c)) {
        cleaned[i] = '_';
      }
    }
    return new String(cleaned);
  }


  /**
   * Creates the temporary directory in which splitter files will
   * be stored.
   * @return the name of the temporary directory. This is where
   *  the Splitters are created.
   **/
  private static String createTempDir() {
    try {
      File tmpDir = UtilMDE.createTempDir("daikon", "split");
      if (dkconfig_delete_splitters_on_exit) {
        tmpDir.deleteOnExit();
      }
      return tmpDir.getPath() + File.separator;
    } catch (IOException e) {
      debugPrintln(e.toString());
    }
    return ""; // Use current directory
  }

  /**
   * Used to abbreviate the re_compiler.quotemeta(exp) command.
   * @return re_compiler.quotemeta(exp).
   */
  private static String qm(String exp) {
    return re_compiler.quotemeta(exp);
  }

  /**
   * Print out a message if the debugPptSplit variable is set to "true."
   **/
  private static void debugPrintln(String s) {
    Global.debugSplit.fine(s);
  }


}
