// Driver for parser

package myparser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import java_cup.runtime.Symbol;

class Main {

  static boolean do_debug_parse = false;

  static public void main(String[] args) throws IOException {
      File infile = new File("D:\\Assignment\\Mini-Project\\myparser_proj\\myparser\\myparser\\input.txt");
      FileInputStream fin = null;
      try
      {
          fin = new FileInputStream(infile);
          
          /* create a parsing object */
          System.out.println("Scanning Input");
          parser parser_obj = new parser(new Yylex(fin));

          Symbol parse_tree = null;
          if (do_debug_parse)
              parse_tree = parser_obj.debug_parse();
          else
              parse_tree = parser_obj.parse();
          System.out.println("Parse successful");
          Node root = (Node) parse_tree.value; 
          if (root != null) { 
              System.out.println("Parse Tree:"); 
              root.printTree(""); 
          }
          SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
          List<String> errors = semanticAnalyzer.analyze(root);
          if (errors.isEmpty())
          {
              System.out.println("No semantic errors");
              CodeGenerator codeGenerator = new CodeGenerator();
              String outputProgram = codeGenerator.generate(root);
              try {
                  Files.writeString(Path.of("D:\\Assignment\\Mini-Project\\myparser_proj\\myparser\\myparser\\GeneratedProgram.java"), outputProgram);
                  System.out.println("File saved successfully.");
              } catch (IOException e){
                  e.printStackTrace();
              }
          } else {
              for (String e : errors){
                  System.out.println(e);
              }
          }
          fin.close();
      }
      catch(FileNotFoundException e)
      {
          System.out.println("File " + infile.getAbsolutePath() +
                  " could not be found on filesystem");
      }
      catch(IOException ioe)
      {
          System.out.println("Exception while reading the file" + ioe);
      }
      catch (Exception e) {
        /* do cleanup here -- possibly rethrow e */
      } finally {
	/* do close out here */
      }
  }
}

